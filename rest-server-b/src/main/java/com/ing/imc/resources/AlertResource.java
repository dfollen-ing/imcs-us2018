package com.ing.imc.resources;

import com.ing.imc.domain.Alert;
import com.ing.imc.services.AlertService;
import com.ing.imc.services.ErrorOrResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import static com.ing.imc.domain.Alert.EMPTY_ALERT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping
public class AlertResource {

    private final AlertService alertService;

    @Inject
    public AlertResource(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping(value = "/alert", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<AlertDto>> getLastAlert() {
        ErrorOrResult<Alert> result = alertService.getLastAlert();
        if (result.isSuccessful()) {
            if (result.getResult().getId().equals(EMPTY_ALERT.getId())) {
                return new ResponseEntity<>(new Response<>(), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(new Response<>(AlertDto.fromAlert(result.getResult())), OK);
        }
        // else
        return new ResponseEntity<>(new Response<>(result.getErrorMessage()), HttpStatus.valueOf(result.getErrorCode()));
    }
}
