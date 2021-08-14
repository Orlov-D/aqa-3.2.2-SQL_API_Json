package ru.netology.data;

import lombok.Value;

public class DataGenerator {

    private DataGenerator() {
    }

    @Value
    public static class UserForRegistration {
        String login = "vasya";
        String password = "qwerty123";
    }

    @Value
    public static class UserWithCode {
        String login = "vasya";
        String code;
    }

    @Value
    public static class Cards {
        String from = "5559 0000 0000 0002";
        String to = "5559 0000 0000 0001";
        int amount;
    }
}
