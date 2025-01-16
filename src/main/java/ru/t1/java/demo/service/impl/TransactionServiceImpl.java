package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final AccountService accountService;

    @Override
    @Transactional
    public void replenishBalance(Long accountId, BigDecimal amount) {
        Account account = accountService.getById(accountId);

        transactionRepository.save(
                Transaction.builder()
                        .account(account)
                        .completedAt(LocalDateTime.now())
                        .amount(amount)
                        .build()
        );
    }

    @Override
    @Transactional
    public void transferMoney(Long accountIdFrom, Long accountIdTo, BigDecimal amount) {
        Account accountFrom = accountService.getById(accountIdFrom);
        Account accountTo = accountService.getById(accountIdTo);

        if (isAmountAvailable(accountFrom, amount)) {
            throw new TransactionException("Amount must be equal to or greater than balance");
        }

        transactionRepository.saveAll(
                List.of(
                        Transaction.builder()
                                .account(accountFrom)
                                .completedAt(LocalDateTime.now())
                                .amount(amount.negate())
                                .build(),
                        Transaction.builder()
                                .account(accountTo)
                                .completedAt(LocalDateTime.now())
                                .amount(amount)
                                .build()
                )
        );
    }

    private boolean isAmountAvailable(Account accountFrom, BigDecimal amount) {
        return accountFrom.getBalance().compareTo(amount) >= 0;
    }

}
