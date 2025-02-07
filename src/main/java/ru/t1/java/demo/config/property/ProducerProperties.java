package ru.t1.java.demo.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "t1.kafka.producer")
public class ProducerProperties {
    private String servers;

    private String retries;

    private String retryBackoffMs;

    private String enableIdempotence;

    private String keySerializer;

    private String valueSerializer;

    private String transactionIdPrefix;

    private String acks;
}
