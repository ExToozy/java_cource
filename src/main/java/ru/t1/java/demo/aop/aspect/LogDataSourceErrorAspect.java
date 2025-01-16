package ru.t1.java.demo.aop.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
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

    @Pointcut("@within(ru.t1.java.demo.*)")
    public void logDataSourceErrorPointcut() {

    }

    @AfterThrowing(
            value = "@annotation(ru.t1.java.demo.aop.annotation.LogDataSourceError)",
            throwing = "e"
    )
    public void LogDataSourceErrorAfterThrowing(JoinPoint joinPoint, Exception e) {
        log.info("Сохраняем сообщение об ошибке");
        errorLogRepository.save(
                DataSourceErrorLog.builder()
                        .methodSignature(joinPoint.getSignature().toLongString())
                        .stackTrace(toStackTraceString(e))
                        .errorMessage(e.getMessage())
                        .build()
        );

    }

    private String toStackTraceString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
