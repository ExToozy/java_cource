package ru.t1.java.demo.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import ru.t1.java.demo.config.property.TopicProperties;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final TopicProperties topics;

    @Bean
    public NewTopic metricTopic() {
        return TopicBuilder
                .name(topics.getMetrics())
                .replicas(1)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder
                .name(topics.getAccounts())
                .replicas(1)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic replenishTransactionTopic() {
        return TopicBuilder
                .name(topics.getReplenishTransactions())
                .replicas(1)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic transferTransactionTopic() {
        return TopicBuilder
                .name(topics.getTransferTransactions())
                .replicas(2)
                .partitions(3)
                .build();
    }
}
