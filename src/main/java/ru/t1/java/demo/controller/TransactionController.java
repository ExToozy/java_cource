package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.aop.annotation.LogDataSourceError;
import ru.t1.java.demo.dto.transaction.ReplenishTransactionDto;
import ru.t1.java.demo.dto.transaction.TransferTransactionDto;
import ru.t1.java.demo.service.TransactionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    @LogDataSourceError
    public void transferMoney(@RequestBody @Validated TransferTransactionDto dto) {
        transactionService.transferMoney(
                dto.getAccountIdFrom(),
                dto.getAccountIdTo(),
                dto.getAmount()
        );
    }

    @PostMapping("/replenish")
    @LogDataSourceError
    public void replenishBalance(@RequestBody @Validated ReplenishTransactionDto dto) {
        transactionService.replenishBalance(
                dto.getAccountId(),
                dto.getAmount()
        );
    }

}
