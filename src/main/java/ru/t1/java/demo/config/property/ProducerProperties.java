package ru.t1.java.demo.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ProducerProperties {
    @Value("${t1.kafka.bootstrap.server}")
    private String servers;

    @Value("${t1.kafka.producer.retries}")
    private String retries;

    @Value("${t1.kafka.producer.retry-backoff-ms}")
    private String retryBackoffMs;

    @Value("${t1.kafka.producer.enable-idempotence}")
    private String enableIdempotence;

    @Value("${t1.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${t1.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value("${t1.kafka.producer.transaction-id-prefix}")
    private String transactionIdPrefix;

    @Value("${t1.kafka.producer.acks}")
    private String acks;
}
