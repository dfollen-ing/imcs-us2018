package com.ing.imc.resources;

import com.ing.imc.domain.Transaction;
import com.ing.imc.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping
public class AccountResource {

    private final TransactionService transactionService;

    @Inject
    public AccountResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/account/{accountId}/transactions", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<TransactionDto>>> getAccounts(@PathVariable String accountId) {
        List<Transaction> accounts = transactionService.getTransactionsFor(accountId);
        List<TransactionDto> transactionDtos = accounts.stream().map(TransactionDto::fromTransaction).collect(Collectors.toList());
        return new ResponseEntity<>(new Response<>(transactionDtos), OK);
    }
}
