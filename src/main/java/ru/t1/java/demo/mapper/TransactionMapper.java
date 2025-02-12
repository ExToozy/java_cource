package ru.t1.java.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.t1.java.demo.dto.transaction.TransactionDto;
import ru.t1.java.demo.model.Transaction;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "account.accountId", target = "accountId")
    List<TransactionDto> toDtoList(List<Transaction> transactions);
}
