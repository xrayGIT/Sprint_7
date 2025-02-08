import client.ScooterServiceClient;
import helper.Helper;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ExtractableResponse;
import model.Courier;
import model.Credentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {
    Courier courier;
    public static final String BASE_URI = "http://qa-scooter.praktikum-services.ru/";
    ScooterServiceClient scooterServiceClient;

    @Before
    public void prereq() {
        scooterServiceClient = new ScooterServiceClient(BASE_URI);
        courier = new Courier(Helper.generateRandomLogin(), "passtest1", "Some_courier_name");
        scooterServiceClient.createCourier(courier)
                .assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера (базовый кейс)")
    public void createCourier_basicSuccessCase() {
        // проверки включены в prereq
    }

    @Test
    @DisplayName("Дубликат курьера не может быть создан")
    public void createCourier_theSameCourierCantBeCreated() {
//        scooterServiceClient.createCourier(courier)
//                .assertThat()
//                .statusCode(201)
//                .body("ok", equalTo(true));
        scooterServiceClient.createCourier(courier)
                .assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера невозможно без указания логина")
    public void createCourier_negativeMissedLogin() {
        Courier courierWithNoLogin = new Courier(null, "pass", "name");
        scooterServiceClient.createCourier(courierWithNoLogin)
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера невозможно без указания пароля")
    public void createCourier_negativeMissedPass() {
        Courier courierWithNoPass = new Courier(Helper.generateRandomLogin(), null, "name");
        scooterServiceClient.createCourier(courierWithNoPass)
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера невозможно без указания имени невозможно")
    public void createCourier_negativeMissedName() {
        Courier courierWithNoPass = new Courier(Helper.generateRandomLogin(), "pass", null);
        scooterServiceClient.createCourier(courierWithNoPass)
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void deleteCourier() {
        Credentials credentials = Credentials.getFrom(courier);
        ExtractableResponse response = scooterServiceClient.loginBy(credentials)
                .extract();
        if(response != null){
            int courierId = response.jsonPath()
                    .getInt("id");
            scooterServiceClient.deleteCourier(courierId)
                    .assertThat()
                    .body("ok", equalTo(true));
        } else {

        }
    }
}
