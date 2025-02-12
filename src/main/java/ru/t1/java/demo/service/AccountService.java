package ru.t1.java.demo.service;

import ru.t1.java.demo.exception.ResourceNotFoundException;
import ru.t1.java.demo.model.Account;

import java.util.List;

public interface AccountService {

    Account create(Account account);

    List<Account> createMany(List<Account> accounts);

    void deleteById(Long id) throws ResourceNotFoundException;

    Account getById(Long id) throws ResourceNotFoundException;


}
