package ru.t1.java.demo.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "t1.kafka.consumer")
public class ConsumerProperties {
    private String groupId;

    private String servers;

    private String sessionTimeout;

    private String maxPartitionFetchBytes;

    private String maxPollRecords;

    private String maxPollIntervalMs;

    private String heartbeatIntervalMs;

    private String keyDeserializer;

    private String valueDeserializer;

    private String trustedPackages;

    private String enableAutoCommit;

    private String autoOffsetReset;

    private String isolationLevel;
}
