package ru.netology.ru.netology.api;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.netology.data.DataGenerator;

import static io.restassured.RestAssured.given;

public final class Api {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static void sendRequest(DataGenerator.UserForRegistration user) {
        Gson gson = new Gson();
        given()
                .spec(requestSpec)
                .body(gson.toJson(user)) // передаём в теле объект, который будет преобразован в JSON
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);
    }

    private static String sendRequest2(DataGenerator.UserWithCode user) {
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

    private static void perevod(DataGenerator.Cards user, String token) {
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
            sendRequest(new DataGenerator.UserForRegistration());
        }

        public static String testVerific(String code) {
            DataGenerator.UserWithCode user = new DataGenerator.UserWithCode(code);
            return sendRequest2(user);
        }

        public static void testTransfer(String token, int amount) {
            perevod(new DataGenerator.Cards(amount), token);
        }

    }

}
