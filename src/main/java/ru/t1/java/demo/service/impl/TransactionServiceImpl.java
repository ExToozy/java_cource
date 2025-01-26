package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.dto.transaction.TransactionStatusDto;
import ru.t1.java.demo.dto.transaction.TransactionToResolveDto;
import ru.t1.java.demo.enums.AccountStatus;
import ru.t1.java.demo.enums.TransactionStatus;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void replenishBalance(Long accountId, BigDecimal amount) {
        Account account = accountService.getById(accountId);

        if (isAccountNotOpened(account)) {
            throw new TransactionException("Account must have the status OPEN");
        }

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        UUID transactionId = UUID.randomUUID();
        System.out.printf("generated id=%s%n", transactionId);
        Transaction transaction = Transaction.builder()
                .account(account)
                .requestedAt(LocalDateTime.now())
                .amount(amount)
                .status(TransactionStatus.REQUESTED)
                .build();

        Transaction savedTransaction = transactionRepository.saveAndFlush(transaction);

        TransactionToResolveDto transactionToResolveDto = TransactionToResolveDto.builder()
                .clientId(account.getClient().getId())
                .accountId(account.getAccountId())
                .accountBalance(account.getBalance())
                .transactionId(savedTransaction.getTransactionId())
                .transactionAmount(amount)
                .requestedAt(transaction.getRequestedAt())
                .build();

        eventPublisher.publishEvent(transactionToResolveDto);
    }

    @Override
    @Transactional
    public void transferMoney(Long accountIdFrom, Long accountIdTo, BigDecimal amount) {
        Account accountFrom = accountService.getById(accountIdFrom);
        Account accountTo = accountService.getById(accountIdTo);

        validateTransfer(amount, accountFrom, accountTo);

        accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
        accountTo.setBalance(accountTo.getBalance().add(amount));
        accountRepository.saveAll(List.of(accountFrom, accountTo));

        List<Transaction> transactions = List.of(
                Transaction.builder()
                        .account(accountFrom)
                        .completedAt(LocalDateTime.now())
                        .transactionId(UUID.randomUUID())
                        .amount(amount.negate())
                        .status(TransactionStatus.REQUESTED)
                        .build(),
                Transaction.builder()
                        .account(accountTo)
                        .completedAt(LocalDateTime.now())
                        .transactionId(UUID.randomUUID())
                        .amount(amount)
                        .status(TransactionStatus.REQUESTED)
                        .build()
        );
        transactionRepository.saveAllAndFlush(transactions);

        List<TransactionToResolveDto> transactionToResolveDtos = transactions.stream().map(transaction ->
                {
                    TransactionToResolveDto transactionToResolveDto = TransactionToResolveDto.builder()
                            .transactionId(transaction.getTransactionId())
                            .clientId(transaction.getAccount().getClient().getId())
                            .accountId(transaction.getAccount().getAccountId())
                            .accountBalance(transaction.getAccount().getBalance())
                            .transactionAmount(transaction.getAmount())
                            .requestedAt(transaction.getRequestedAt())
                            .build();
                    System.out.println(transactionToResolveDto);
                    return transactionToResolveDto;
                }
        ).toList();
        
        transactionToResolveDtos.forEach(eventPublisher::publishEvent);
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.getTransactionsByAccount_Id(accountId);
    }

    @Override
    @Transactional
    public void handleTransactionStatusRecord(TransactionStatusDto transactionStatusDto) {
        if (transactionStatusDto.getStatus() == TransactionStatus.ACCEPTED) {
            handleAcceptedTransaction(transactionStatusDto);
        } else if (transactionStatusDto.getStatus() == TransactionStatus.BLOCKED) {
            handleBlockedTransaction(transactionStatusDto);
        } else if (transactionStatusDto.getStatus() == TransactionStatus.REJECTED) {
            handleRejectedTransaction(transactionStatusDto);
        }
    }

    private void handleAcceptedTransaction(TransactionStatusDto transactionStatus) {
        System.out.println(transactionStatus);
        Optional<Transaction> optionalTransaction = transactionRepository
                .findByTransactionId(transactionStatus.getTransactionId());

        System.out.println(optionalTransaction);

        optionalTransaction.ifPresent(transaction -> {
            transaction.setStatus(TransactionStatus.ACCEPTED);
            transactionRepository.save(transaction);
        });
    }

    private void handleBlockedTransaction(TransactionStatusDto transactionStatus) {
        Optional<Transaction> optionalTransaction = transactionRepository
                .findByTransactionId(transactionStatus.getTransactionId());
        System.out.println(optionalTransaction);

        optionalTransaction.ifPresent(transaction -> {
            Account account = transaction.getAccount();
            account.setAccountStatus(AccountStatus.BLOCKED);
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
            account.setFrozenAmount(account.getFrozenAmount().add(transaction.getAmount()));

            transaction.setStatus(TransactionStatus.BLOCKED);

            accountRepository.save(account);
            transactionRepository.save(transaction);
        });
    }

    private void handleRejectedTransaction(TransactionStatusDto transactionStatus) {
        Optional<Transaction> optionalTransaction = transactionRepository
                .findByTransactionId(transactionStatus.getTransactionId());

        System.out.println(optionalTransaction);

        optionalTransaction.ifPresent(transaction -> {
            Account account = transaction.getAccount();
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));

            transaction.setStatus(TransactionStatus.REJECTED);

            accountRepository.save(account);
            transactionRepository.save(transaction);
        });
    }

    private void validateTransfer(BigDecimal amount, Account accountFrom, Account accountTo) {
        if (isAccountNotOpened(accountFrom)) {
            throw new TransactionException("Account from must have the status OPEN");
        }

        if (isAccountNotOpened(accountTo)) {
            throw new TransactionException("Account to must have the status OPEN");
        }

        if (!isAmountAvailable(accountFrom, amount)) {
            throw new TransactionException("Amount must be equal to or greater than balance");
        }
    }

    private boolean isAccountNotOpened(Account account) {
        return account.getAccountStatus() != AccountStatus.OPEN;
    }

    private boolean isAmountAvailable(Account accountFrom, BigDecimal amount) {
        return accountFrom.getBalance().compareTo(amount) >= 0;
    }

}
