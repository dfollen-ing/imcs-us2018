package com.ing.imc.resources;

import com.ing.imc.domain.Transaction;
import com.ing.imc.services.AccountService;
import com.ing.imc.services.ErrorOrResult;
import com.ing.imc.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping
@Slf4j
public class AccountResource {

    private final TransactionService transactionService;

    private final AccountService accountService;

    @Inject
    public AccountResource(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @GetMapping(value = "/account/{accountId}/transactions", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<TransactionDto>>> getAccounts(@PathVariable String accountId) {
        List<Transaction> accounts = transactionService.getTransactionsFor(accountId);
        List<TransactionDto> transactionDtos = accounts.stream().map(TransactionDto::fromTransaction).collect(Collectors.toList());
        return new ResponseEntity<>(new Response<>(transactionDtos), OK);
    }

    @PostMapping(value = "/account/{accountId}/limit", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<BigDecimal>> addLimitDetails(@RequestBody @Valid LimitDetailsDto limitDetailsDto, @PathVariable String accountId) {
        ErrorOrResult<BigDecimal> errorOrResult = accountService.updateAmountLimit(limitDetailsDto, accountId);
        if (errorOrResult.isSuccessful()) {
            return new ResponseEntity<>(new Response<>(errorOrResult.getResult()), OK);
        }
        log.error(errorOrResult.getErrorMessage());
        return new ResponseEntity<>(new Response<>(errorOrResult.getErrorMessage()), HttpStatus.valueOf(errorOrResult.getErrorCode()));
    }
}
