package src;

import java.util.*;
import java.lang.*;

public class Main {
    // Constants //
    static final String DB = "db.csv";  // Path to database file
    static CSV db = new CSV(DB);  // Open database
    static int entry;  // Index of current entry in database

    // Database indices
    private static class Data {
        static final int FIRST = 0;  // Index of first name in database
        static final int LAST = 1;  // Index of last name in database
        static final int NUM = 2;  // Index of customer number in database
        static final int PIN = 3;  // Index of PIN in database
        static final int SAVINGS = 4;  // Index of savings balance in database
        static final int CHEQUING = 5;  // Index of chequing balance in database
    }

    static final String TEMPLATE = "%s,%s,%d,%d,%f,%f";  // Used for formatting database entries

    // Print formatting
    private static class Format {
        static final String RESET = "\033[0m";  // Reset text formatting
        static final String BOLD = "\033[1m";  // Bold text
        static final String ITALIC = "\033[3m";  // Italic text
        static final String UNDERLINE = "\033[4m";  // Underline text\
        static final String BLACK = "\033[30m";  // Black text
        static final String RED = "\033[31m";  // Red text
        static final String GREEN = "\033[32m";  // Green text
        static final String YELLOW = "\033[33m";  // Yellow text
        static final String CYAN = "\033[36m";  // Cyan text
        static final String WHITE = "\033[37m";  // White text
    }

    public static void main(String[] args) {
        // Log in and prompt for acc_num and pin
        // If valid send to Option Select
        // If invalid send to Sign Up
        // If user chooses to log out, send back to Log in screen to loop infinitely

        Scanner sc = new Scanner(System.in);
        boolean loop = true;
        boolean repeat = true;

        System.out.println(Format.BOLD + Format.WHITE + "Welcome to Tungular Bank!" + Format.RESET);

        while (true) {
            while (repeat) {  // Repeat log in if user chooses to try again
                entry = -1;  // Reset entry index
                repeat = logIn();
            }

            while (loop) {
                loop = options();  // If user chooses to log out, return false
            }
        }
    }

    private static int findEntry(int acc, int pin) {
        CSV.Items item = db.readLine();  // Read first line

        while (item != null) {
            if (item.getNum() == acc && item.getPin() == pin) {  // Check if account number and PIN match
                return db.getLine() - 1;
            }
            item = db.readLine();
        }
        return -1;
    }

    public static boolean logIn() {
        // Prompt for acc and pin
        // Check if valid
        // If valid and matching send to Option Select
        // If invalid ask to try again or sign up

        // Initialize variables
        Scanner sc = new Scanner(System.in);
        String input;
        int acc = -1;
        int pin = -1;
        boolean proceed = false;

        // Prompt for account number and validate
        while (!proceed) {
            System.out.print("Enter your six-digit account number: ");
            input = sc.nextLine();
            if (Validate.accountNumber(input)) {
                acc = Integer.parseInt(input);
                proceed = true;
            } else {
                System.out.println(Format.RED + "Account number must be a positive six-digit number." + Format.RESET);
            }

            if (!proceed) {
                System.out.print("Would you like to sign up instead? (Y/N)\n> ");
                if (sc.nextLine().equals("y")) {
                    signUp();
                    return false;  // Exit log in loop
                }
            }
        }

        proceed = false;  // Reset for PIN validation

        // Prompt for PIN and validate
        while (!proceed) {
            System.out.print("Enter your four-digit PIN: ");
            input = sc.nextLine();
            if (Validate.pin(input)) {
                pin = Integer.parseInt(input);
                proceed = true;
            } else {
                System.out.println(Format.RED + "PIN must be a positive four-digit number." + Format.RESET);
            }

            if (!proceed) {
                System.out.print("Would you like to try again with a different account number? (Y/N)\n> ");
                if (sc.nextLine().equals("y")) {
                    return true;
                }
            }
        }

        // Check if account number and PIN match
        // If not, ask to try again or sign up
        if ((entry = findEntry(acc, pin)) == -1) {
            System.out.println(Format.RED + "Account number and PIN do not match." + Format.RESET);
            System.out.print("Would you like to:\n1. Try again\n2. Sign up\n> ");
            return switch (sc.next()) {
                case "1" -> true;
                case "2" -> {
                    signUp();
                    yield false;
                }
                default -> {
                    System.out.println(Format.RED + "Invalid input." + Format.RESET);
                    yield true;
                }
            };
        }

        return false;
    }

    public static void signUp() {
        // Prompt for first and last name
        // Prompt for a new acc_num
        // Prompt for a pin
        // Send back to login

        Scanner sc = new Scanner(System.in);
        String firstName, lastName;
        String input;
        int acc = -1;
        int pin = -1;
        boolean proceed = false;

        // Ask for first and last name
        // Force non-empty input
        do {
            System.out.print("Enter your first name: ");
            firstName = sc.nextLine();
        } while (firstName.isEmpty());

        System.out.print("Enter your last name: ");
        do {
            lastName = sc.nextLine();
        } while (lastName.isEmpty());

        // Asking for new account number
        while (!proceed) {
            System.out.print("Enter your new six-digit account number: ");
            input = sc.nextLine();
            if (Validate.accountNumber(input)) {
                acc = Integer.parseInt(input);
                proceed = true;
            } else {
                System.out.println(Format.RED + "Account number must be a positive six-digit number." + Format.RESET);
            }
        }

        proceed = false;  // Reset for PIN validation

        // Asking for new pin
        while (!proceed) {
            System.out.print("Enter a pin for your new account:");
            input = sc.nextLine();
            if (Validate.pin(input)) {
                pin = Integer.parseInt(input);
                proceed = true;
            } else {
                System.out.println(Format.RED + "PIN must be a positive four-digit number." + Format.RESET);
            }
        }

        // Write new entry to database
        db.writeLine(String.format(TEMPLATE, firstName, lastName, acc, pin, 0.0, 0.0));
    }

    public static boolean options() {
        // Prompt for what the user wants to do
        // Withdraw, Deposit, Open or Close Accounts, View Balance, Change Pin, and Log-out

        // Initialize variables
        Scanner sc = new Scanner(System.in);
        String input = "";

        // Prompt for action
        System.out.print("What would you like to do?\n1. Withdraw\n2. Deposit\n3. Open an Account\n4. Close Accounts\n5. View Balance\n6. Change Pin\n7. Log out\n> ");
        switch (input = sc.nextLine().toLowerCase()) {
            case "1", "withdraw" -> withdraw();
            case "2", "deposit" -> deposit();
            case "3", "open", "open account" -> openAccount();
            case "4", "close", "close account" -> closeAccount();
            case "5", "balance", "view balance" -> viewBalance();
            case "6", "change pin" -> changePin();
            case "7", "log out" -> System.out.println(Format.BOLD + "Logging out..." + Format.RESET);
            default -> System.out.println(Format.RED + "Invalid input." + Format.RESET);
        }

        return !input.equals("7");  // Return false if user chooses to log out
    }

    public static void withdraw() {
        //Prompts the user which account they want to withdraw money from
        //If there is one account is automatically chooses
        //If there is no account is sends an error

        Scanner sc = new Scanner(System.in);
        int accounts = 0;
        int choice;
        double amount;
        int finish;
        CSV.Items item = db.readLine(entry);


        if ((Objects.isNull(item.getChequing()) && (Objects.isNull(item.getSavings())))) {
            accounts = 0;
        }

        if (!(Objects.isNull(item.getChequing()))) {
            accounts = 1;
        }

        if (!(Objects.isNull(item.getChequing()) && !(Objects.isNull(item.getSavings())))) {
            accounts = 3;
        }
        else if (!(Objects.isNull(item.getSavings()))) {
            accounts = 2;
        }

        if (accounts == 0) {
            System.out.println("No accounts to withdraw from, please open an account first.");
            options();
        }

        if (accounts == 1) {
            System.out.println("How much would you like to withdraw from your chequing account?");
            try {
                amount = sc.nextDouble();
            }
            catch (InputMismatchException e) {
                System.out.println("You can only withdraw a numerical amount.");
                withdraw();
            }
            if (amount<item.getChequing()){
                //send back or something
            }
            else {
                System.out.println("You cannot withdraw that much, insufficient funds.");
                withdraw();
            }
        }

        if (accounts == 2) {
            System.out.println("How much would you like to withdraw from your savings account?");
            try {
                amount = sc.nextDouble();
            }
            catch (InputMismatchException e) {
                System.out.println("You can only withdraw a numerical amount.");
                withdraw();
            }
            if (amount<item.getSavings()){
                //send back or something
            }
            else {
                System.out.println("You cannot withdraw that much, insufficient funds.");
                withdraw();
            }
        }

        if (accounts == 3) {
            System.out.println("Which account would you like to withdraw from? 1 for chequing, and 2 for savings.");
            try {
                choice = sc.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid account.");
            }
            if (choice == 1){
                System.out.println("How much would you like to withdraw from your chequing account?");
                try {
                    amount = sc.nextDouble();
                }
                catch (InputMismatchException e) {
                    System.out.println("You can only withdraw a numerical amount.");
                    withdraw();
                }
                if (amount<item.getSavings()){
                    //send back or something
                }
                else {
                    System.out.println("You cannot withdraw that much, insufficient funds.");
                    withdraw();
                }
            }
            if (choice == 2){
                System.out.println("How much would you like to withdraw from your savings account?");
                try {
                    amount = sc.nextDouble();
                }
                catch (InputMismatchException e) {
                    System.out.println("You can only withdraw a numerical amount.");
                    withdraw();
                }
                if (amount<item.getSavings()){
                    //send back or something
                }
                else {
                    System.out.println("You cannot withdraw that much, insufficient funds.");
                    withdraw();
                }
            }
        }
    }
}
