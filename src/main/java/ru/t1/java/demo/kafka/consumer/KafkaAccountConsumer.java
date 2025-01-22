package ru.t1.java.demo.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.account.CreateAccountDto;
import ru.t1.java.demo.mapper.AccountMapper;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.service.AccountService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaAccountConsumer {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    @KafkaListener(
            topics = {"${t1.kafka.topic.accounts}"}
    )
    public void listener(@Payload List<CreateAccountDto> messageList,
                         Acknowledgment ack) {
        log.debug("Account consumer: Обработка новых сообщений");

        try {
            List<Account> accounts = accountMapper.toEntityList(messageList);
            accountService.createMany(accounts);
        } finally {
            ack.acknowledge();
        }

        log.debug("Account consumer: записи обработаны");
    }


}
