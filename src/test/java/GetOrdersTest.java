import client.ScooterServiceClient;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import model.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;


public class GetOrdersTest {
    public static final String BASE_URI = "http://qa-scooter.praktikum-services.ru/";
    ScooterServiceClient scooterServiceClient;

    @Before
    public void prereq() {
        scooterServiceClient = new ScooterServiceClient(BASE_URI);
    }

    @Test
    @DisplayName("Получить список заказов (базовый кейс)")
    public void createOrder_basicSuccessCase() {
        List<Order> orders = scooterServiceClient.getOrdersList(5)
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("orders", Order.class);
        Assert.assertEquals("Количество сообщений в ответе 5", 5, orders.size());
        assertNotNullWithStep("Имя не Null", orders.get(0).getFirstName());
        assertNotNullWithStep("Телефон не Null", orders.get(0).getPhone());
        assertNotNullWithStep("Станция метро не Null", orders.get(0).getMetroStation());
        // правильно проверять это по JSON схеме, но цели такой нет в проекте.
    }

    @Step("{0}")
    private void assertNotNullWithStep(String stepName, Object object) {
        assertNotNull(object);
    }
}
