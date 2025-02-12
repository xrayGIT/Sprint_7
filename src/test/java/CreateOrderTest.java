import client.ScooterServiceClient;
import helper.Enviroment;
import io.qameta.allure.Allure;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.model.Status;
import io.restassured.response.ValidatableResponse;
import model.Order;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


@RunWith(Parameterized.class)
public class CreateOrderTest {
    ScooterServiceClient scooterServiceClient;
    Order order;
    int currentTrackId;
    private final String[] colors;
    private final String metroStation;

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
        scooterServiceClient = new ScooterServiceClient(Enviroment.BASE_URL);
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
        ValidatableResponse response = scooterServiceClient.cancelOrder(currentTrackId);
        int code = response.extract().statusCode();
        if(code != 200){
            Allure.step("Заказ не отменен!", Status.BROKEN);
        }

    }
}
