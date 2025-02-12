package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.exception.ResourceNotFoundException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.service.AccountService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account create(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public List<Account> createMany(List<Account> accounts) {
        return accountRepository.saveAll(accounts);
    }

    @Override
    public void deleteById(Long id) throws ResourceNotFoundException {
        Account account = getById(id);
        accountRepository.delete(account);
    }

    @Override
    public Account getById(Long id) throws ResourceNotFoundException {
        return accountRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id %s not found".formatted(id)));
    }
}
