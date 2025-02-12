package ru.t1.java.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.t1.java.demo.dto.account.AccountDto;
import ru.t1.java.demo.dto.account.CreateAccountDto;
import ru.t1.java.demo.model.Account;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "client.id", target = "clientId")
    AccountDto toDto(Account account);

    Account toEntity(CreateAccountDto dto);

    List<Account> toEntityList(List<CreateAccountDto> dtos);

}
