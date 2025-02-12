package model;

import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
@Getter
@Setter
public class Order {
    public Order(String metroStation, String[] color) {
        Faker faker = new Faker();
        this.firstName = faker.name().firstName();
        this.lastName = faker.name().lastName();
        this.address = faker.address().fullAddress();
        this.metroStation = metroStation;
        this.phone = faker.phoneNumber().phoneNumber();
        this.rentTime = (int) (Math.random() * 10 + 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.deliveryDate = simpleDateFormat.format(faker.date().future(10, TimeUnit.DAYS));
        this.comment = "some comment for " + firstName;
        this.color = color;
    }

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

}
