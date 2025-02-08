package client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import model.Courier;
import model.Credentials;
import model.Order;

import static io.restassured.RestAssured.given;

@AllArgsConstructor
public class ScooterServiceClient {
    private String BASE_URI;



    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then();
    }

    @Step("Логин курьера")
    public ValidatableResponse loginBy(Credentials credentials) {
        return given().filter(new AllureRestAssured()).baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .body(credentials)
                .post("/api/v1/courier/login")
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(int courierId) {
        String endPoint = "/api/v1/courier/" + courierId;
        return given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .delete(endPoint)
                .then();
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .body(order)
                .post("/api/v1/orders")
                .then();
    }

    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int trackId) {
        return given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .body("{\"track\":" + trackId + "}")
                .post("/api/v1/orders/cancel/")
                .then();

    }
}
