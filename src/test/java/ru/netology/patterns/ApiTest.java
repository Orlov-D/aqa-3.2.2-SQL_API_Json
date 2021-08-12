package ru.netology.patterns;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.patterns.DataGenerator.Registration.*;

public class ApiTest {

    @Test
    @SneakyThrows
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        testLogin();
        SqlGetters sqlGetters = new SqlGetters();
        String tok = testVerific(sqlGetters.getCode());
        int difference = 300;
        int[] balanceBefore = sqlGetters.getBalance();
        int firstBalanceBefore = balanceBefore[0];
        int secondBalanceBefore = balanceBefore[1];
        testTransfer(tok, difference);
        int[] balanceAfter = sqlGetters.getBalance();
        assertEquals(firstBalanceBefore + difference * 100, balanceAfter[0]);
        assertEquals(secondBalanceBefore - difference * 100, balanceAfter[1]);
    }
}
