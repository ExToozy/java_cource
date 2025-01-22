package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.aop.annotation.LogDataSourceError;
import ru.t1.java.demo.aop.annotation.Metric;
import ru.t1.java.demo.dto.transaction.ReplenishTransactionDto;
import ru.t1.java.demo.exception.ClientException;
import ru.t1.java.demo.kafka.producer.KafkaTransactionProducer;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final KafkaTransactionProducer transactionProducer;

    @PostMapping("/test")
    @Metric(executionTimeMillisLimit = 2000)
    public void doSomethingWithArgs(
            @RequestBody String json,
            @RequestParam("id") Long id
    ) throws InterruptedException {

        Thread.sleep(2001);

    }

    @GetMapping(value = "/test")
    @Metric(executionTimeMillisLimit = 2000)
    @LogDataSourceError
    public void doSomething() throws InterruptedException {
        Thread.sleep(3000L);
        throw new ClientException();
    }

    @GetMapping("/test-send-transaction")
    public void sendTestMessageToKafka() {
        ReplenishTransactionDto transaction = ReplenishTransactionDto.builder()
                .accountId(1L)
                .amount(BigDecimal.valueOf(1000))
                .build();

        transactionProducer.send(transaction);
    }


}
