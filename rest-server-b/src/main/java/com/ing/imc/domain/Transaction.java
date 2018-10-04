package com.ing.imc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.internal.binary.BinaryEnumObjectImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private String id;
    private String accountId;
    private String debitAccountNumber;
    private String creditAccountNumber;
    private String currency;
    private BigDecimal amount;
    private String communication;
    private LocalDateTime receivedTime;
    private TransactionType type;

    public static Transaction fromBinary(BinaryObject binaryAccount) {
        String id = binaryAccount.field("id");
        String accountId = binaryAccount.field("accountId");
        String debitAccountNumber = binaryAccount.field("debitAccountNumber");
        String creditAccountNumber = binaryAccount.field("creditAccountNumber");
        String currency = binaryAccount.field("currency");
        BigDecimal amount = binaryAccount.field("amount");
        String communication = binaryAccount.field("communication");
        LocalDateTime receivedTime = binaryAccount.field("receivedTime");
        BinaryEnumObjectImpl type = binaryAccount.field("type");
        TransactionType accountType = TransactionType.values()[type.enumOrdinal()];
        return new Transaction(id, accountId, debitAccountNumber, creditAccountNumber, currency, amount, communication, receivedTime, accountType);
    }

    public AffinityKey<String> getAffinityKey() {
        return new AffinityKey<>(id, accountId);
    }
}
