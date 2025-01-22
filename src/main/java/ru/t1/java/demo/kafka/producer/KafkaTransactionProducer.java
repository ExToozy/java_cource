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

    public void send(ReplenishTransactionDto replenishTransactionDto) {
        template.send(replenishTransactionTopicName, replenishTransactionDto);
    }

    public void send(TransferTransactionDto replenishTransactionDto) {
        template.send(transferTransactionTopicName, replenishTransactionDto);
    }
}
