package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.transaction.TransactionStatusDto;
import ru.t1.java.demo.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    void replenishBalance(Long accountId, BigDecimal amount);

    void transferMoney(Long accountIdFrom, Long accountIdTo, BigDecimal amount);

    List<Transaction> getTransactionsByAccountId(Long accountId);

    void handleTransactionStatusRecord(TransactionStatusDto transactionStatus);
}
