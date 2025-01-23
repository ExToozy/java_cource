package ru.t1.java.demo.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.transaction.ReplenishTransactionDto;
import ru.t1.java.demo.dto.transaction.TransferTransactionDto;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionProducer {
    private final KafkaTemplate<String, Object> template;

    @Value("${t1.kafka.topic.replenish_transactions}")
    private String replenishTransactionTopicName;

    @Value("${t1.kafka.topic.transfer_transactions}")
    private String transferTransactionTopicName;

    public void sendTransaction(ReplenishTransactionDto replenishTransactionDto) {
        send(replenishTransactionTopicName, replenishTransactionDto);
    }

    public void sendTransaction(TransferTransactionDto replenishTransactionDto) {
        send(transferTransactionTopicName, replenishTransactionDto);
    }

    private void send(String topic, Object object) {
        try {
            template.send(topic, object);
        } catch (Exception e) {
            log.error("Error occurred while trying to send message", e);
        } finally {
            template.flush();
        }
    }
}
