package com.ing.imc.resources;

import com.ing.imc.domain.Account;
import com.ing.imc.services.AccountService;
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
public class CustomerResource {

    private final AccountService accountService;

    @Inject
    public CustomerResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/customer/{custId}/accounts", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<AccountDto>>> getAccounts(@PathVariable String custId) {
        List<Account> accounts = accountService.getAccountsOf(custId);
        List<AccountDto> accountDtos = accounts.stream().map(AccountDto::fromAccount).collect(Collectors.toList());
        return new ResponseEntity<>(new Response<>(accountDtos), OK);
    }
}
