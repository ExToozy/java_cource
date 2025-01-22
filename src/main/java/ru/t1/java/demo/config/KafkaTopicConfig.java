package ru.t1.java.demo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {


    @Value("${t1.kafka.topic.metrics}")
    private String metricTopicName;

    @Value("${t1.kafka.topic.accounts}")
    private String accountTopicName;

    @Value("${t1.kafka.topic.replenish_transactions}")
    private String replenishTransactionTopicName;

    @Value("${t1.kafka.topic.transfer_transactions}")
    private String transferTransactionTopicName;

    @Bean
    public NewTopic metricTopic() {
        return TopicBuilder
                .name(metricTopicName)
                .replicas(1)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder
                .name(accountTopicName)
                .replicas(1)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic replenishTransactionTopic() {
        return TopicBuilder
                .name(replenishTransactionTopicName)
                .replicas(1)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic transferTransactionTopic() {
        return TopicBuilder
                .name(transferTransactionTopicName)
                .replicas(2)
                .partitions(3)
                .build();
    }
}
