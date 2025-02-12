package ru.t1.java.demo.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class KafkaClientProducer {

    private final KafkaTemplate<String, Object> template;

    public void send(Long clientId) {
        try {
            template.sendDefault(UUID.randomUUID().toString(), clientId).get();

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }

    public void sendTo(String topic, String o) {
        try {
            template.send(topic, o).get();
            template.send(topic,
                            1,
                            LocalDateTime.now().toEpochSecond(ZoneOffset.of("+03:00")),
                            UUID.randomUUID().toString(),
                            o)
                    .get();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }

}