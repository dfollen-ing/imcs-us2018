package com.ing.imc.resources;

import com.ing.imc.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class AccountDto {

    private String id;
    private String accountNumber;
    private String currency;
    private BigDecimal balance;
    private String type;
    private BigDecimal limit;

    static AccountDto fromAccount(Account account) {
        return new AccountDto(account.getId(), account.getAccountNumber(), account.getCurrency(), account.getBalance(), account.getType().name(), account.getLimit());
    }
}
