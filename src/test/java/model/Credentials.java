package model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Credentials {
    private String login;
    private String password;

    static public Credentials getFrom(Courier courier){
        return new Credentials(courier.getLogin(), courier.getPassword());
    }
}
