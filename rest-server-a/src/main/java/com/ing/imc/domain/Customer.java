package com.ing.imc.domain;

import lombok.*;
import org.apache.ignite.binary.BinaryObject;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {

    private String id;
    private String firstName;
    private String lastName;

    public static Customer fromBinary(BinaryObject binaryCustomer) {
        return new Customer(binaryCustomer.field("id"), binaryCustomer.field("firstName"), binaryCustomer.field("lastName"));
    }

}
