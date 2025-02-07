package ru.t1.java.demo.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.config.property.TopicProperties;
import ru.t1.java.demo.dto.transaction.ReplenishTransactionDto;
import ru.t1.java.demo.dto.transaction.TransactionToResolveDto;
import ru.t1.java.demo.dto.transaction.TransferTransactionDto;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class KafkaTransactionProducer {
    
    private final KafkaTemplate<String, Object> template;

    private final TopicProperties topics;

    public void send(ReplenishTransactionDto replenishTransactionDto) {
        sendMessage(
                topics.getReplenishTransactions(),
                UUID.randomUUID().toString(),
                replenishTransactionDto
        );
    }

    public void send(TransferTransactionDto replenishTransactionDto) {
        sendMessage(
                topics.getTransferTransactions(),
                UUID.randomUUID().toString(),
                replenishTransactionDto
        );
    }

    public void send(TransactionToResolveDto transactionToResolveDto) {
        sendMessage(
                topics.getTransactionAccept(),
                transactionToResolveDto.getAccountId().toString(),
                transactionToResolveDto
        );
    }

    private void sendMessage(String topic, String key, Object object) {
        try {
            template.send(topic, key, object);

        } catch (Exception e) {
            log.error("Error occurred while trying to send message", e);
        } finally {
            template.flush();
        }
    }
}
