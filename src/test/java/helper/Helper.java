package helper;

public class Helper {
    public static String generateRandomLogin(){
        int randPostfix = (int) (Math.random() * 10000000);
        return "pavel" + randPostfix;
    }
}
