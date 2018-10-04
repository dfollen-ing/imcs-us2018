package com.ing.imc.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewTransactionDto {

    @NotNull
    private String ownerId;
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

}
