package ru.t1.java.demo.aop.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.kafka.producer.KafkaErrorLogProducer;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.ErrorLogRepository;

import java.io.PrintWriter;
import java.io.StringWriter;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LogDataSourceErrorAspect {

    private final ErrorLogRepository errorLogRepository;

    private final KafkaErrorLogProducer errorLogProducer;

    @AfterThrowing(
            value = "@annotation(ru.t1.java.demo.aop.annotation.LogDataSourceError)",
            throwing = "e"
    )
    public void logDataSourceErrorAfterThrowing(JoinPoint joinPoint, Exception e) throws JsonProcessingException {
        log.info("Сохраняем сообщение об ошибке");

        DataSourceErrorLog errorLog = DataSourceErrorLog.builder()
                .methodSignature(joinPoint.getSignature().toLongString())
                .stackTrace(toStackTraceString(e))
                .errorMessage(e.getMessage())
                .build();

        try {
            errorLogProducer.send(errorLog);
        } catch (Exception exception) {
            log.error("Error occurred while trying send error log message to kafka: ", e);
            log.info("Trying save error log to db");
            errorLogRepository.save(errorLog);
        }
    }


    private String toStackTraceString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
