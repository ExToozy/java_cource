package ru.t1.java.demo.kafka.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.t1.java.demo.dto.transaction.TransactionToResolveDto;
import ru.t1.java.demo.kafka.producer.KafkaTransactionProducer;

/**
 * Этот класс, отправляет сохраненные в БД транзакции в кафку.
 * Это необходимо для того, чтобы статус по транзакциям приходил,
 * только после сохранения транзакции в БД.
 * P.S. Егор, есть ли способ лучше? В голову приходит только это и Debezium
 */
@Component
@RequiredArgsConstructor
public class SavedToDbTransactionEventListener {

    private final KafkaTransactionProducer transactionProducer;

    @TransactionalEventListener
    public void sendSavedTransactionsToKafka(TransactionToResolveDto transaction) {
        transactionProducer.send(transaction);
    }
}
