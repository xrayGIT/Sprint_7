import client.ScooterServiceClient;
import io.qameta.allure.junit4.DisplayName;
import model.Order;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    public static final String BASE_URI = "http://qa-scooter.praktikum-services.ru/";
    ScooterServiceClient scooterServiceClient;
    Order order;
    int currentTrackId;
    private String[] colors;
    private String metroStation;

    public CreateOrderTest(String metroStation, String[] colors){
        this.colors = colors;
        this.metroStation = metroStation;
    }

    @Parameterized.Parameters
    public static Object[][] testData(){
        return new Object[][]{
                {"4", new String[]{"BLACK"}},
                {"3", new String[]{}},
                {"2", new String[]{"BLACK", "GREY"}},
                {"1", new String[]{"GREY"}}
        };
    }

    @Before
    public void prereq() {
        scooterServiceClient = new ScooterServiceClient(BASE_URI);
        order = new Order(metroStation, colors);
        currentTrackId = scooterServiceClient.createOrder(order)
                .assertThat()
                .statusCode(201)
                .body("track", CoreMatchers.any(Integer.class))
                .extract().jsonPath().getInt("track");
    }

    @Test
    @DisplayName("Создание заказа (базовый сценарий)")
    public void createOrder_basicSuccessCase() {
        //
    }

    @After
    public void cancelOrder(){
        scooterServiceClient.cancelOrder(currentTrackId)
                .assertThat()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}
