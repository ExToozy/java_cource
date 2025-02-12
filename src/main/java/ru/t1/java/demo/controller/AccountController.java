package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.dto.account.AccountDto;
import ru.t1.java.demo.dto.account.CreateAccountDto;
import ru.t1.java.demo.dto.transaction.TransactionDto;
import ru.t1.java.demo.mapper.AccountMapper;
import ru.t1.java.demo.mapper.TransactionMapper;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    private final TransactionService transactionService;

    private final AccountMapper accountMapper;

    private final TransactionMapper transactionMapper;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@RequestBody @Validated CreateAccountDto dto) {
        Account account = accountMapper.toEntity(dto);
        accountService.create(account);
        return accountMapper.toDto(account);
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionDto> getTransactionByAccountId(@PathVariable("id") Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return transactionMapper.toDtoList(transactions);
    }
}
