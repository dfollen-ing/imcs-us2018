package com.ing.imc.resources;

import com.ing.imc.domain.Account;
import com.ing.imc.domain.AccountType;
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
import java.util.Arrays;

import static com.ing.imc.resources.TestingUtil.ACCOUNT_SERVICE;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CustomerResource.class)
@ActiveProfiles("unit")
@Import(TestingUtil.class)
@Slf4j
public class CustomerResourceTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testSimpleAccount() throws Exception {
        when(ACCOUNT_SERVICE.getAccountsOf(eq("1"))).thenReturn(Arrays.asList(
                new Account("id1", "accountNumber1", "EUR", BigDecimal.TEN, "ownerId", AccountType.CURRENT),
                new Account("id2", "accountNumber2", "EUR", BigDecimal.TEN, "ownerId", AccountType.SAVINGS),
                new Account("id3", "accountNumber3", "EUR", BigDecimal.TEN, "ownerId", AccountType.TRADING)
                ));
        MvcResult mvcResult = mvc.perform(get("/customer/1/accounts")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("payload[0].id").value("id1"))
                .andReturn();
        log.info("Customer 1 has the following accounts: {}", mvcResult.getResponse().getContentAsString());
    }


}
