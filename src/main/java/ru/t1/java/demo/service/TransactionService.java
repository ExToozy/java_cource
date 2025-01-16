package ru.t1.java.demo.service;

import java.math.BigDecimal;

public interface TransactionService {

    void replenishBalance(Long accountId, BigDecimal amount);

    void transferMoney(Long accountIdFrom, Long accountIdTo, BigDecimal amount);
}
