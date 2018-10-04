package com.ing.learn.imc.client.util;

import com.ing.imc.domain.Account;
import com.ing.imc.domain.AccountType;

import java.math.BigDecimal;

import static com.ing.imc.domain.AccountType.CURRENT;

public class AccountUtil {

    public static Account generateAccount(String accountId, String customerId) {
        return new Account(accountId, generateAccountNumber(customerId), generateRandomCurrency(), generateRandomAmount(getMaxAmount(CURRENT)), customerId, CURRENT);
    }

    public static Account generateAccount(String accountId, String customerId, AccountType type) {
        return new Account(accountId, generateAccountNumber(customerId), generateRandomCurrency(), generateRandomAmount(getMaxAmount(type)), customerId, type);
    }

    private static int getMaxAmount(AccountType type) {
        switch (type) {
            case CURRENT: return 5_000;
            case SAVINGS: return 100_000;
            case TRADING:
            default:
                    return 20_000;
        }
    }

    private static final String[] CURRENCIES = {"EUR", "USD", "CAD", "SEK", "NOK"};

    private static String generateAccountNumber(String customerId) {
        return String.format("BE99 %s %s %s", to4Chars(CustomerUtil.randTo(9999)), to4Chars(CustomerUtil.randTo(9999)), to4Chars(Integer.parseInt(customerId)));
    }

    public static String generateAccountNumber() {
        return generateAccountNumber(to4Chars(CustomerUtil.randTo(9999)));
    }

    private static String to4Chars(Integer integer) {
        int valueToParse = integer;
        if (valueToParse < 0) {
            valueToParse = -valueToParse;
        }
        valueToParse = valueToParse % 10_000;
        String result = Integer.toString(valueToParse);

        if (valueToParse < 10) {
            result = "000" + result;
        } else if (valueToParse < 100) {
            result = "00" + result;
        } else if (valueToParse < 1000) {
            result = "0" + result;
        }
        return result;
    }

    private static BigDecimal generateRandomAmount(int maxAmount) {
        BigDecimal amount = new BigDecimal(CustomerUtil.randTo(maxAmount) + "." + CustomerUtil.randTo(99));
        return amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    private static String generateRandomCurrency() {
        return "EUR";//return CURRENCIES[CustomerUtil.randTo(CURRENCIES.length)];
    }
}
