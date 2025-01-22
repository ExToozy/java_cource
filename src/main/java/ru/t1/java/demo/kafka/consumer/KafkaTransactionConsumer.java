package ru.t1.java.demo.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.transaction.ReplenishTransactionDto;
import ru.t1.java.demo.dto.transaction.TransferTransactionDto;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaTransactionConsumer {

    private final TransactionService transactionService;

    @KafkaListener(
            topics = {"${t1.kafka.topic.replenish_transactions}"}
    )
    public void listenReplenishTransactionsTopic(@Payload List<ReplenishTransactionDto> messageList,
                                                 Acknowledgment ack) {
        log.debug("Replenish transaction consumer: Обработка новых сообщений");

        try {
            messageList.forEach(dto -> {
                transactionService.replenishBalance(
                        dto.getAccountId(),
                        dto.getAmount()
                );
                log.info("Replenish transaction consumer: transaction was handled %s".formatted(dto));
            });
        } finally {
            ack.acknowledge();
        }

        log.debug("Replenish transaction consumer: записи обработаны");
    }

    @KafkaListener(
            topics = {"${t1.kafka.topic.transfer_transactions}"}
    )
    public void listenTransferTransactionsTopic(@Payload List<TransferTransactionDto> messageList,
                                                Acknowledgment ack) {
        log.debug("Transfer transaction consumer: Обработка новых сообщений");

        try {
            messageList.forEach(dto ->
                    transactionService.transferMoney(
                            dto.getAccountIdFrom(),
                            dto.getAccountIdTo(),
                            dto.getAmount()
                    )
            );
        } finally {
            ack.acknowledge();
        }

        log.debug("Transfer transaction consumer: записи обработаны");
    }


}
