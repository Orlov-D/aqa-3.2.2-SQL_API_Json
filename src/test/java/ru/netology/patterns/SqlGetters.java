package ru.netology.patterns;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

import static java.sql.Types.NULL;

public class SqlGetters {
    @SneakyThrows
    public Connection getConnection() {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass"
        );
    }

    @SneakyThrows
    public void cleanDatabase() {
        var conn = getConnection();
        conn.prepareStatement("DROP TABLE IF EXISTS cards;");
        conn.prepareStatement("DROP TABLE IF EXISTS users;");
        conn.prepareStatement("DROP TABLE IF EXISTS auth_codes");
        conn.prepareStatement("DROP TABLE IF EXISTS card_transactions");
    }

    @SneakyThrows
    public String getUserIdVasya() {
        var conn = getConnection();
        var dataStmt = conn.createStatement().executeQuery("SELECT * FROM users");
        String id = "";
        try (var rs = dataStmt) {
            while (rs.next()) {
                id = rs.getString("id");
                var login = rs.getString("login");
                if (login.equals("vasya")) {
                    break;
                }
            }
        }
        return id;
    }

    @SneakyThrows
    public String getCode() {
        var conn = getConnection();
        var dataStmt = conn.createStatement().executeQuery("SELECT * FROM auth_codes ORDER BY created DESC");
        String code = "";
        try (var rs = dataStmt) {
            while (rs.next()) {
                code = rs.getString("code");
                var user_id = rs.getString("user_id");
                if (user_id.equalsIgnoreCase(getUserIdVasya())) {
                    break;
                }
            }
        }
        return code;
    }

    @SneakyThrows
    public int[] getBalance() {
        var conn = getConnection();
        var dataStmt = conn.createStatement().executeQuery("SELECT * FROM cards");
        int balanceFirst = NULL;
        int balanceSecond = NULL;
        int[] cards = new int[2];
        try (var rs = dataStmt) {
            while (rs.next()) {
                int balance_in_kopecks = rs.getInt("balance_in_kopecks");
                var cardNumber = rs.getString("number");
                if (cardNumber.equals("5559 0000 0000 0002")) {
                    cards[1] = balance_in_kopecks;
                } else {
                    cards[0] = balance_in_kopecks;
                }
            }
        }
        return cards;
    }
}
