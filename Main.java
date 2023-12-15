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

        // Ask for acc
        System.out.print("Enter your account number (XXXXXX): ");

        // Validate format of acc_num
        try {
            acc = sc.nextInt();
            if (acc != 6) {
                throw new InputMismatchException();
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid account number.");
        }

        // Ask for pin
        System.out.print("Enter your PIN (XXXX): ");

        // Validate format of pin
        try {
            pin = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid PIN.");
        }
    }

    public static void signUp() {
        // Prompt for first and last name
        // Prompt for a new acc_num
        // Prompt for a pin
        // Send back to logIn

        Scanner sc = new Scanner(System.in);
        String firstName;
        String lastName;
        int acc;
        int pin;

        System.out.print("Enter your first name:");
        firstName = sc.nextLine();
        System.out.print("Enter your last name:");
        lastName = sc.nextLine();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            throw new InputMismatchException("Name cannot be left blank.");
        }

        System.out.print("Enter your new six-digit account number:");
        try {
            acc = sc.nextInt();
        }
        catch (InputMismatchException e) {
            //Wait for Comrade James to find out how to loop asking
            System.out.println("Account number must contain only integers.");
        }
        //Wait for Comrade James to Complete Code
        if () {
            throw new InputMismatchException("The account number must be six-digits.");
        }

        System.out.print("Enter a pin for your new account:");
        try {
            pin = sc.nextInt();
        }
        catch (InputMismatchException e) {
            //Wait for Comrade James to find out how to loop asking
            System.out.println("Pin must contain only integers.");
        }
        //Wait for Comrade James to Complete Code
        if () {
            throw new InputMismatchException("The Pin number must be four-digits.");
        }
    }

}
