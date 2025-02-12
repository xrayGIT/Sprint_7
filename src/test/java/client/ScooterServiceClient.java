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
    private static final String COURIER_API = "/api/v1/courier";
    private static final String LOGIN_COURIER_API = "/api/v1/courier/login";
    private static final String ORDERS_API = "/api/v1/orders";
    private static final String CANCEL_ORDER_API = "/api/v1/orders/cancel";


    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .body(courier)
                .post(COURIER_API)
                .then();
    }

    @Step("Логин курьера")
    public ValidatableResponse loginBy(Credentials credentials) {
        return given().filter(new AllureRestAssured()).baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .body(credentials)
                .post(LOGIN_COURIER_API)
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(int courierId) {
        String endPoint = COURIER_API + "/" + courierId;
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
                .post(ORDERS_API)
                .then();
    }

    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int trackId) {
        return given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .body("{\"track\":" + trackId + "}")
                .put(CANCEL_ORDER_API)
                .then();

    }

    @Step("Получить список заказов")
    public ValidatableResponse getOrdersList(int rowsLimit) {
        return given().filter(new AllureRestAssured())
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .queryParam("limit", rowsLimit)
                .get(ORDERS_API)
                .then();

    }
}
