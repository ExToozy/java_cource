package ru.t1.java.demo.dto.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TransactionToResolveDto {

    @JsonProperty("transaction_id")
    UUID transactionId;

    @JsonProperty("client_id")
    Long clientId;

    @JsonProperty("account_id")
    UUID accountId;

    @JsonProperty("transaction_amount")
    BigDecimal transactionAmount;

    @JsonProperty("account_balance")
    BigDecimal accountBalance;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    LocalDateTime requestedAt;
    
}
