public class Validate {
    public static boolean validateAccountNumber(String accountNumber) {
        return accountNumber.matches("[0-9]{6}");
    }

    public static boolean validatePin(String pin) {
        return pin.matches("[0-9]{4}");
    }
}
