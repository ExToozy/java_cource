package ru.t1.java.demo.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.t1.java.demo.dto.transaction.TransactionToResolveDto;
import ru.t1.java.demo.kafka.producer.KafkaTransactionProducer;

/**
 * Этот класс, отправляет сохраненные в БД транзакции в кафку.
 * Это необходимо для того, чтобы статус по транзакциям приходил,
 * только после сохранения транзакции в БД.
 * P.S. Егор, есть ли способ лучше? В голову приходит только это и Debezium.
 * Была вот такая проблема:
 * 1. Транзакция сохранялась под аннотацией Transactional и отправлялась в кафку
 * 2. Приходил ответ в топике t1_demo_transaction_result
 * 3. От туда брал transaction_id и вот тут два случая
 * 4. 1. Он есть в базе(Transactional в п.1 успевал закрыться)
 * 4. 2. Его в базе нет(Transactional в п.1 не успевал закрыться)
 * И вот с помощью такой прослойки всё хорошо
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
