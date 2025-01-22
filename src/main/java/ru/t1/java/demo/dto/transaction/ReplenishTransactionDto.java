package ru.t1.java.demo.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReplenishTransactionDto {

    @NotNull(message = "account_id must be not null")
    @JsonProperty("account_id")
    private Long accountId;

    @NotNull(message = "amount must be not null")
    @Positive(message = "amount must be positive")
    @JsonProperty("amount")
    private BigDecimal amount;
}
