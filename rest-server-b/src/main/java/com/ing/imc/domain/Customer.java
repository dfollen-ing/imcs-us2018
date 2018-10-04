package com.ing.imc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ignite.binary.BinaryObject;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {

    private String id;
    private String firstName;
    private String lastName;
    private String contactDetails;

    public static Customer fromBinary(BinaryObject binaryCustomer) {

        String id = binaryCustomer.field("id");
        String firstName = binaryCustomer.field("firstName");
        String lastName = binaryCustomer.field("lastName");
        String contactDetails = null;
        if (binaryCustomer.hasField("contactDetails")) {
            contactDetails = binaryCustomer.field("contactDetails");
        }
        return new Customer(id, firstName, lastName, contactDetails);
    }

}
