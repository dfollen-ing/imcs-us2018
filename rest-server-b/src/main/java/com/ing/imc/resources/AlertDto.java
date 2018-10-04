package com.ing.imc.resources;

import com.ing.imc.domain.Alert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
class AlertDto {
    private String destination;
    private String message;
    private String creationTime;

    static AlertDto fromAlert(Alert alert) {
        return new AlertDto(alert.getDestination(), alert.getMessage(), alert.getCreationTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
