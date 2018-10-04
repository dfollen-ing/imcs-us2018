package com.ing.imc.resources;

import com.ing.imc.Config;
import com.ing.imc.domain.AccountType;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Import(Config.class)
@Slf4j
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static class AccountListResponse extends Response<List<AccountDto>> {
        public AccountListResponse() {
        }

        public AccountListResponse(String errorMessage) {
            super(errorMessage);
        }

        public AccountListResponse(List<AccountDto> payload) {
            super(payload);
        }
    }

    @Test
    public void testWithdrawalAndDeposit() {

        // let's get the current account of 333
        String custId = "333";
        AccountDto originalCurrentAccount = getCurrentAccountOf(custId);
        assertTrue(originalCurrentAccount.getBalance().compareTo(BigDecimal.ZERO) > 0);

        String currentAccountId = originalCurrentAccount.getId();

        ResponseEntity<Response> txResponseEntity = restTemplate.getForEntity("/account/"+currentAccountId+"/transactions", Response.class);
        assertEquals(HttpStatus.OK, txResponseEntity.getStatusCode());

        // let's deposit money
        NewTransactionDto newTransactionDto = new NewTransactionDto("333", currentAccountId, "SOME ACCO UNTN UMBER", originalCurrentAccount.getAccountNumber(),  "EUR", new BigDecimal(500), "Hi there");
        ResponseEntity<Response> creditResponseEntity = restTemplate.postForEntity("/transaction", newTransactionDto, Response.class);
        assertEquals(HttpStatus.OK, creditResponseEntity.getStatusCode());

        // let's check that it has been taken into account
        AccountDto afterDepositAccount = getCurrentAccountOf(custId);
        assertEquals(originalCurrentAccount.getBalance().add(new BigDecimal(500)), afterDepositAccount.getBalance());

        // let's now try to withdraw too much money
        newTransactionDto = new NewTransactionDto("333", currentAccountId, originalCurrentAccount.getAccountNumber(), "SOME ACCO UNTN UMBER","EUR", afterDepositAccount.getBalance().add(BigDecimal.ONE), "too much?");
        ResponseEntity<Response> debitResponseEntity = restTemplate.postForEntity("/transaction", newTransactionDto, Response.class);
        assertEquals(HttpStatus.valueOf(403), debitResponseEntity.getStatusCode());
        assertNotNull(debitResponseEntity.getBody());
        assertNotNull(debitResponseEntity.getBody().getErrorMessage());

        // let's now try to withdraw what we had deposited
        newTransactionDto = new NewTransactionDto("333", currentAccountId, originalCurrentAccount.getAccountNumber(), "SOME ACCO UNTN UMBER","EUR", new BigDecimal(500), "too much?");
        debitResponseEntity = restTemplate.postForEntity("/transaction", newTransactionDto, Response.class);
        assertEquals(HttpStatus.OK, debitResponseEntity.getStatusCode());

        // let's check that it has been taken into account
        AccountDto afterCreditAccount = getCurrentAccountOf(custId);
        assertEquals(originalCurrentAccount.getBalance(), afterCreditAccount.getBalance());

    }

    @NotNull
    private AccountDto getCurrentAccountOf(String custId) {
        ResponseEntity<AccountListResponse> accountsResponseEntity = restTemplate.getForEntity("/customer/"+custId+"/accounts", AccountListResponse.class);
        assertEquals(HttpStatus.OK, accountsResponseEntity.getStatusCode());
        AccountListResponse response = accountsResponseEntity.getBody();

        List<AccountDto> payload = response.getPayload();
        AccountDto currentAccount = null;
        for (AccountDto accountDto : payload) {
            if (accountDto.getType().equals(AccountType.CURRENT.name())) {
                currentAccount = accountDto;
            }
        }
        assertNotNull(currentAccount);
        return currentAccount;
    }
}
