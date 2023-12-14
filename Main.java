import java.util.*;
import java.lang.*;
import java.io.*;

public class Main {
    // Constants //
    final String DB = "db.csv";  // Path to database file

    // Database indices
    private class Data {
        final int FIRST = 0;  // Index of first name in database
        final int LAST = 1;  // Index of last name in database
        final int NUM = 2;  // Index of customer number in database
        final int PIN = 3;  // Index of PIN in database
        final int SAVINGS = 4;  // Index of savings balance in database
        final int CHEQUING = 5;  // Index of chequing balance in database
    }

    final String TEMPLATE = "%s,%s,%d,%d,%f,%f";  // Used for formatting database entries

    public static void main(String[] args) {
        // Log in and prompt for acc_num and pin
        // If valid send to Option Select
        // If invalid send to Sign Up
        // If user chooses to log out, send back to Log in screen to loop infinitely

        boolean loop = true;

        while (loop) {
            logIn();
            signUp();
            options();

        }
    }

    public static void logIn() {
        // Prompt for acc and pin
        // Check if valid
        // If valid and matching send to Option Select
        // If invalid ask to try again or sign up

        Scanner sc = new Scanner(System.in);
        int acc, pin;

        System.out.print("Enter your account number (XXXXXX): ");

        // Validate format of acc_num
        try {
            acc = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid account number.");
        }
    }

    public static void singUp() {
        // Prompt for first and last name
        // Prompt for a new acc_num
        // Prompt for a pin
        // Send back to logIn
    }

}
