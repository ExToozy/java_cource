package ru.t1.java.demo.service;

import ru.t1.java.demo.exception.ResourceNotFoundException;
import ru.t1.java.demo.model.Account;

public interface AccountService {

    Account create(Account dto);

    void deleteById(Long id) throws ResourceNotFoundException;

    Account getById(Long id) throws ResourceNotFoundException;


}
