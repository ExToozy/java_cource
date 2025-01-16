package ru.t1.java.demo.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.enums.AccountType;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private Long id;

    private Long clientId;

    @JsonProperty("account_type")
    private AccountType accountType;

    private BigDecimal balance;

}
