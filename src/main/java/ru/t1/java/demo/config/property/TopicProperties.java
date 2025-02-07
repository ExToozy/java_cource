package ru.t1.java.demo.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "t1.kafka.topic")
public class TopicProperties {

    private String accounts;

    private String replenishTransactions;

    private String transferTransactions;

    private String transactionAccept;

    private String transactionResult;

    private String metrics;
}
