import java.util.*;
import java.lang.*;
import java.io.*;

public class Main {
    // Constants //
    final String DB = "db.csv";  // Path to database file

    // Database indices
    private static class Data {
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
        boolean repeat = true;

        while (loop) {
            while (repeat) {
                repeat = logIn();
            }
            signUp();
            loop = options();  // If user chooses to log out, return false
        }
    }

    public static boolean logIn() {
        // Prompt for acc and pin
        // Check if valid
        // If valid and matching send to Option Select
        // If invalid ask to try again or sign up

        Scanner sc = new Scanner(System.in);
        int acc, pin;
        boolean proceed = false;

        // Prompt for account number and validate
        while (!proceed) {
            System.out.print("Enter your six-digit account number:");
            try {
                acc = sc.nextInt();
                if (acc < 100000 || acc > 999999) {
                    System.out.println("Account number must a positive six-digit number.");
                } else {
                    proceed = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Account number must contain only integers.");
            }

            if (!proceed) {
                System.out.println("Would you like to try again? (y/n)");
                if (sc.nextLine().equals("y")) {
                    return false;
                }
            }
        }

        proceed = false;  // Reset for PIN validation

        // Prompt for PIN and validate
        while (!proceed) {
            System.out.print("Enter your four-digit PIN:");
            try {
                pin = sc.nextInt();
                if (pin < 1000 || pin > 9999) {
                    System.out.println("Account number must be a positive four-digit number.");
                } else {
                    proceed = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("PIN must contain only integers.");
            }

            if (!proceed) {
                System.out.println("Would you like to try again? (Y/N)");
                if (sc.nextLine().equals("y")) {
                    return false;
                }
            }
        }



        return true;
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

        //Ask for first and last name
        System.out.print("Enter your first name:");
        firstName = sc.nextLine();
        System.out.print("Enter your last name:");
        lastName = sc.nextLine();

        //Check if names are valid (is not empty)
        if (firstName.isEmpty() || lastName.isEmpty()) {
            throw new InputMismatchException("Name cannot be left blank.");
        }

        //Asking for new account number
        System.out.print("Enter your new six-digit account number:");
        //Validating account number format
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

        //Asking for new pin
        System.out.print("Enter a pin for your new account:");
        //Validating pin format
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

    public static void options() {
        // Prompt for what the user wants to do
        // Withdraw, Deposit, Open or Close Accounts, View Balance, Change Pin, and Log-out

        System.out.println("Type 1 to withdraw:");
        System.out.println("Type 2 to deposit:");
        System.out.println("Type 3 to open an account:");
        System.out.println("Type 4 to close an account:");
        System.out.println("Type 5 to change your pin:");
        System.out.println("Type 6 to logout:");
    }

    public static void read() {
        // Read from database
    }

    public static void write() {
        // Write to database
    }
}
