package ru.t1.java.demo.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.enums.AccountType;
import ru.t1.java.demo.validation.annotation.ValueOfEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountDto {

    @NotNull(message = "client_id must be not null")
    private Long clientId;

    @NotNull(message = "account_type must be not null")
    @ValueOfEnum(enumClass = AccountType.class)
    @JsonProperty("account_type")
    private AccountType accountType;
}
