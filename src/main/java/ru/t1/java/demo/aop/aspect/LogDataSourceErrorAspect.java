package ru.t1.java.demo.aop.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.ErrorLogRepository;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LogDataSourceErrorAspect {

    private final ErrorLogRepository errorLogRepository;

    @AfterThrowing(value = "@annotation(ru.t1.java.demo.aop.annotation.LogDataSourceError)", throwing = "e")
    public void LogDataSourceErrorAfterThrowing(JoinPoint joinPoint, Exception e) {
        log.info("Сохраняем сообщение об ошибке");
        errorLogRepository.save(
                DataSourceErrorLog.builder()
                        .methodSignature(joinPoint.getSignature().toLongString())
                        .stackTrace(getFirstTraceElement(e))
                        .errorMessage(e.getMessage())
                        .build()
        );

    }

    private String getFirstTraceElement(Exception e) {
        return e.getStackTrace()[0].toString();
    }
}
