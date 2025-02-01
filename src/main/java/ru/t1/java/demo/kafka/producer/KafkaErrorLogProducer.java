package ru.t1.java.demo.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.enums.ErrorType;
import ru.t1.java.demo.model.DataSourceErrorLog;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class KafkaErrorLogProducer {

    private final KafkaTemplate<String, Object> template;

    @Value("${t1.kafka.topic.metrics}")
    private String metricTopicName;

    public void send(DataSourceErrorLog errorLog) {
        try {
            var metricRecord = getErrorLogProducerRecord(errorLog);
            template.send(metricRecord);
        } catch (Exception e) {
            log.error("Error occurred while trying to send message", e);
        } finally {
            template.flush();
        }
    }

    private ProducerRecord<String, Object> getErrorLogProducerRecord(DataSourceErrorLog errorLog) {
        var metricRecord = new ProducerRecord<String, Object>(
                metricTopicName,
                errorLog
        );
        metricRecord.headers().add("ERROR_TYPE", ErrorType.DATA_SOURCE.name().getBytes());
        return metricRecord;
    }
}
