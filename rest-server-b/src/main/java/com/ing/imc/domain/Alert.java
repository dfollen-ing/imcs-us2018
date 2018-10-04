package com.ing.imc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ignite.binary.BinaryObject;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alert {

    public static final Alert EMPTY_ALERT = new Alert("-1", "-1", "-1", LocalDateTime.MIN);

    private String id;
    private String destination;
    private String message;
    private LocalDateTime creationTime;

    public static Alert fromBinary(BinaryObject binaryAccount) {
        String id = binaryAccount.field("id");
        String destination = binaryAccount.field("destination");
        String message = binaryAccount.field("message");
        LocalDateTime creationTime = binaryAccount.field("creationTime");
        return new Alert(id, destination, message, creationTime);
    }

}
