package ru.netology.patterns;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    private static void sendRequest(UserForRegistration user) {
        Gson gson = new Gson();
        given()
                .spec(requestSpec)
                .body(gson.toJson(user)) // передаём в теле объект, который будет преобразован в JSON
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);
    }

    private static String sendRequest2(UserWithCode user) {
        Gson gson = new Gson();
        return given()
                .spec(requestSpec)
                .body(gson.toJson(user)) // передаём в теле объект, который будет преобразован в JSON
                .when()
                .post("/api/auth/verification")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    private static void perevod(Cards user, String token) {
        Gson gson = new Gson();
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(gson.toJson(user)) // передаём в теле объект, который будет преобразован в JSON
                .when()
                .post("/api/transfer")
                .then()
                .statusCode(200);
    }

    public static class Registration {
        private Registration() {
        }

        public static void testLogin() {
            sendRequest(new UserForRegistration());
        }

        public static String testVerific(String code) {
            UserWithCode user = new UserWithCode(code);
            return sendRequest2(user);
        }

        public static void testTransfer(String token, int amount) {
            perevod(new Cards(amount), token);
        }

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
