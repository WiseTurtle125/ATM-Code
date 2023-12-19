package src;

public class Validate {
    public static boolean accountNumber(String accountNumber) {
        return accountNumber.matches("[0-9]{6}");
    }

    public static boolean pin(String pin) {
        return pin.matches("[0-9]{4}");
    }
}
