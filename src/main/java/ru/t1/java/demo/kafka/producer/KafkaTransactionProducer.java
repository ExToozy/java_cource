package ru.t1.java.demo.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.transaction.ReplenishTransactionDto;
import ru.t1.java.demo.dto.transaction.TransactionToResolveDto;
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

    @Value("${t1.kafka.topic.transaction_accept}")
    private String transactionResolveTopic;

    public void send(ReplenishTransactionDto replenishTransactionDto) {
        sendMessage(replenishTransactionTopicName, replenishTransactionDto);
    }

    public void send(TransferTransactionDto replenishTransactionDto) {
        sendMessage(transferTransactionTopicName, replenishTransactionDto);
    }

    public void send(TransactionToResolveDto transactionToResolveDto) {
        sendMessage(transactionResolveTopic, transactionToResolveDto);
    }

    private void sendMessage(String topic, Object object) {
        try {
            template.send(topic, object).get();
        } catch (Exception e) {
            log.error("Error occurred while trying to send message", e);
        } finally {
            template.flush();
        }
    }
}
