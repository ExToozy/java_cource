package ru.t1.java.demo.aop.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.aop.annotation.Metric;
import ru.t1.java.demo.dto.metric.MetricDto;
import ru.t1.java.demo.kafka.producer.KafkaMetricProducer;

import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class MetricAspect {

    private final KafkaMetricProducer metricProducer;

    private final ObjectMapper objectMapper;

    @Around("@annotation(ru.t1.java.demo.aop.annotation.Metric)")
    public Object sendMetricIfTimeExceed(ProceedingJoinPoint pJoinPoint) throws Throwable {

        long beforeTime = System.currentTimeMillis();
        Object result = pJoinPoint.proceed();
        long afterTime = System.currentTimeMillis();

        Optional<Metric> optionalMetricAnnotation = getMetricAnnotation(pJoinPoint);
        optionalMetricAnnotation.ifPresent((metricAnnotation) -> {
                    try {
                        long executionTime = afterTime - beforeTime;
                        long limit = metricAnnotation.executionTimeMillisLimit();
                        if (executionTime > limit) {
                            sendMetricToKafka(pJoinPoint, executionTime);
                        }
                    } catch (Throwable e) {
                        log.error("Error occurred while trying send metric message to kafka: ", e);
                    }
                }
        );
        
        return result;
    }

    private Optional<Metric> getMetricAnnotation(ProceedingJoinPoint pJoinPoint) {
        return Arrays.stream(((MethodSignature) pJoinPoint.getSignature())
                        .getMethod()
                        .getAnnotations())
                .filter(annotation -> annotation instanceof Metric)
                .map(annotation -> (Metric) annotation)
                .findFirst();
    }

    private void sendMetricToKafka(ProceedingJoinPoint pJoinPoint, long executionTime) throws JsonProcessingException {
        MetricDto metric = MetricDto.builder()
                .executionTime(executionTime)
                .methodName(pJoinPoint.toLongString())
                .jsonArgs(objectMapper.writeValueAsString(pJoinPoint.getArgs()))
                .build();

        metricProducer.send(metric);
    }
}
