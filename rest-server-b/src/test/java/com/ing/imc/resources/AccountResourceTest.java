package com.ing.imc.resources;

import com.ing.imc.domain.Transaction;
import com.ing.imc.domain.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static com.ing.imc.resources.TestingUtil.TRANSACTION_SERVICE;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AccountResource.class)
@ActiveProfiles("unit")
@Import(TestingUtil.class)
@Slf4j
public class AccountResourceTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testSimpleAccount() throws Exception {
        final String accountId = "id1";
        when(TRANSACTION_SERVICE.getTransactionsFor(eq(accountId)))
                .thenReturn(Collections.singletonList(
                    new Transaction(accountId, "accountId", "from", "to", "EUR", BigDecimal.TEN, "communication", LocalDateTime.now(), TransactionType.DEBIT)));

        MvcResult mvcResult = mvc.perform(get("/account/id1/transactions")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("payload[0].id").value(accountId))
                .andReturn();
        log.info("Account 1 has the following transactions: {}", mvcResult.getResponse().getContentAsString());
    }


}
