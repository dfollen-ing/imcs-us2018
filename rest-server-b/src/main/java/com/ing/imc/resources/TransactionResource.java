package com.ing.imc.resources;

import com.ing.imc.domain.Transaction;
import com.ing.imc.services.ErrorOrResult;
import com.ing.imc.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping
@Slf4j
public class TransactionResource {

    private final TransactionService transactionService;

    @Inject
    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(value = "/transaction", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<TransactionDto>> getAccounts(@RequestBody @Valid NewTransactionDto newTransactionDto) {
        ErrorOrResult<Transaction> errorOrResult = transactionService.validateAndExecuteTx(newTransactionDto);
        if (errorOrResult.isSuccessful()) {
            TransactionDto transactionDto = TransactionDto.fromTransaction(errorOrResult.getResult());
            return new ResponseEntity<>(new Response<>(transactionDto), OK);
        }
        // else
        log.error(errorOrResult.getErrorMessage());
        return new ResponseEntity<>(new Response<>(errorOrResult.getErrorMessage()), HttpStatus.valueOf(errorOrResult.getErrorCode()));
    }
}
