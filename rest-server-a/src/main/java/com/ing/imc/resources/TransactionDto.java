package com.ing.imc.resources;

import com.ing.imc.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
class TransactionDto {

    private String id;
    @NotNull
    private String accountId;
    @NotNull
    private String debitAccountNumber;
    @NotNull
    private String creditAccountNumber;
    @NotNull
    private String currency;
    @NotNull
    private BigDecimal amount;
    private String communication;
    private String receivedTime;
    private String transactionType;

    static TransactionDto fromTransaction(Transaction transaction) {
        return new TransactionDto(transaction.getId(),
                transaction.getAccountId(),
                transaction.getDebitAccountNumber(),
                transaction.getCreditAccountNumber(),
                transaction.getCurrency(),
                transaction.getAmount(),
                transaction.getCommunication(),
                transaction.getReceivedTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                transaction.getType().name());
    }
}
