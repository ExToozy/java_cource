package ru.t1.java.demo.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.config.property.TopicProperties;
import ru.t1.java.demo.dto.metric.MetricDto;
import ru.t1.java.demo.enums.ErrorType;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class KafkaMetricProducer {

    private final KafkaTemplate<String, Object> template;

    private final TopicProperties topics;

    public void send(MetricDto metric) {
        try {
            var metricRecord = getMetricProducerRecord(metric);
            template.send(metricRecord);
        } catch (Exception e) {
            log.error("Error occurred while trying to send message", e);
        } finally {
            template.flush();
        }
    }

    private ProducerRecord<String, Object> getMetricProducerRecord(MetricDto metric) throws JsonProcessingException {
        var metricRecord = new ProducerRecord<String, Object>(
                topics.getMetrics(),
                metric
        );
        metricRecord.headers().add("ERROR_TYPE", ErrorType.METRICS.name().getBytes());
        return metricRecord;
    }

}
