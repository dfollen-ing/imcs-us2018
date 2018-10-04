package com.ing.imc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.internal.binary.BinaryEnumObjectImpl;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private String id;
    private String accountNumber;
    private String currency;
    private BigDecimal balance;
    private String ownerId;
    private AccountType type;

    public static Account fromBinary(BinaryObject binaryAccount) {
        String id = binaryAccount.field("id");
        String accountNumber = binaryAccount.field("accountNumber");
        String currency = binaryAccount.field("currency");
        BigDecimal balance = binaryAccount.field("balance");
        String ownerId = binaryAccount.field("ownerId");
        BinaryEnumObjectImpl  type = binaryAccount.field("type");
        AccountType accountType = AccountType.values()[type.enumOrdinal()];
        return new Account(id, accountNumber, currency, balance, ownerId, accountType);
    }

    public AffinityKey<String> getAffinityKey() {
        return new AffinityKey<>(id, ownerId);
    }
}
