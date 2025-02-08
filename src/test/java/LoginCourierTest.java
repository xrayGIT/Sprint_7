import client.ScooterServiceClient;
import helper.Helper;
import io.qameta.allure.junit4.DisplayName;
import model.Courier;
import model.Credentials;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginCourierTest {
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
    @DisplayName("Логин курьера (базовый кейс)")
    public void loginCourier_basicSuccessCase() {
        scooterServiceClient.loginBy(Credentials.getFrom(courier))
                .assertThat()
                .statusCode(200)
                .body("id", CoreMatchers.any(Integer.class));
    }
    @Test
    @DisplayName("Логин с неверным паролем не возможен")
    public void loginCourier_wrongPass() {
        Courier courierWithWrongPassword = new Courier(courier.getLogin(), "11", courier.getFirstName());
        scooterServiceClient.loginBy(Credentials.getFrom(courierWithWrongPassword))
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин с неверным логином не возможен")
    public void loginCourier_wrongLogin() {
        Courier notExistingCourier = new Courier("notExistingLogin", "11", "name");
        scooterServiceClient.loginBy(Credentials.getFrom(notExistingCourier))
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин без пароля невозможен")
    public void loginCourier_missedPasswordField() {
        scooterServiceClient.loginBy(new Credentials("login1", null))
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин без 'логина' невозможен")
    public void loginCourier_missedLoginField() {
        scooterServiceClient.loginBy(new Credentials(null, "pass1"))
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @After
    public void deleteCourier() {
        Credentials credentials = Credentials.getFrom(courier);
        int courierId = scooterServiceClient.loginBy(credentials)
                .extract()
                .jsonPath()
                .getInt("id");
        scooterServiceClient.deleteCourier(courierId)
                .assertThat()
                .body("ok", equalTo(true));
    }
}
