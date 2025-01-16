package ru.t1.java.demo.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferTransactionDto {

    @NotNull(message = "account_id_from must be not null")
    @JsonProperty("account_id_from")
    private Long accountIdFrom;

    @NotNull(message = "account_id_to must be not null")
    @JsonProperty("account_id_to")
    private Long accountIdTo;

    @NotNull(message = "amount must be not null")
    @Positive(message = "amount must be positive")
    @JsonProperty("amount")
    private BigDecimal amount;

}
