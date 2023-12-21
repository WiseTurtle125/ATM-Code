package src;

import java.text.Normalizer;
import java.util.*;
import java.lang.*;

// SKUJA ADVICE: CHECK IF NULL IS NULL AND NOT JUST EMPTY OR ANY OTHER VALUE, IF SO, MAKE AN IF TO TELL PROGRAM THAT THAT EQUALS NULL

public class Main {
    //  Constants // 
    static final String DB = "db.csv";  //  Path to database file
    static CSV db = new CSV(DB);  //  Open database
    static int entry;  //  Index of current entry in database

    // Database indices
    private static class Data {
        static final int FIRST = 0;  //  Index of first name in database
        static final int LAST = 1;  //  Index of last name in database
        static final int NUM = 2;  //  Index of customer number in database
        static final int PIN = 3;  //  Index of PIN in database
        static final int SAVINGS = 4;  //  Index of savings balance in database
        static final int CHEQUING = 5;  //  Index of chequing balance in database
    }

    static final String TEMPLATE = "%s,%s,%d,%d,%f,%f";  //  Used for formatting database entries

    //  Print formatting
    private static class Format {
        static final String RESET = "\033[0m ";     //  Reset text formatting
        static final String BOLD = "\033[1m";       //  Bold text
        static final String ITALIC = "\033[3m";     //  Italic text
        static final String UNDERLINE = "\033[4m";  //  Underline text
        static final String BLACK = "\033[30m";     //  Black text
        static final String RED = "\033[31m";       //  Red text
        static final String GREEN = "\033[32m";     //  Green text
        static final String YELLOW = "\033[33m";    //  Yellow text
        static final String CYAN = "\033[36m";      //  Cyan text
        static final String WHITE = "\033[37m";     //  White text
    }

    public static void main(String[] args) {
        //  Log in and prompt for acc_num and pin
        //  If valid send to Option Select
        //  If invalid send to Sign Up
        //  If user chooses to log out, send back to Log in screen to loop infinitely

        Scanner sc = new Scanner(System.in);
        boolean loop = true;
        boolean repeat = true;

        System.out.println(Format.BOLD + Format.WHITE + "Welcome to Tungular Bank!" + Format.RESET);

        while (true) {
            while (repeat) {  //  Repeat log in if user chooses to try again
                entry = -1;  //  Reset entry index
                repeat = logIn();
            }

            while (loop) {
                loop = options();  //  If user chooses to log out, return false
            }
        }
    }

    private static int findEntry(int acc, int pin) {
        CSV.Items item = db.readLine();  //  Read first line

        while (item != null) {
            if (item.getNum() == acc && item.getPin() == pin) {  //  Check if account number and PIN match
                return db.getLine() - 1;
            }
            item = db.readLine();
        }
        return -1;
    }

    public static boolean logIn() {
        //  Prompt for acc and pin
        //  Check if valid
        //  If valid and matching send to Option Select
        //  If invalid ask to try again or sign up

        //  Initialize variables
        Scanner sc = new Scanner(System.in);
        String input;
        int acc = -1;
        int pin = -1;
        boolean proceed = false;

        //  Prompt for account number and validate
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
                    return false;  //  Exit log in loop
                }
            }
        }

        proceed = false;  //  Reset for PIN validation

        //  Prompt for PIN and validate
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

        //  Check if account number and PIN match
        //  If not, ask to try again or sign up
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
        //  Prompt for first and last name
        //  Prompt for a new acc_num
        //  Prompt for a pin
        //  Send back to login

        Scanner sc = new Scanner(System.in);
        String firstName, lastName;
        String input;
        int acc = -1;
        int pin = -1;
        boolean proceed = false;

        //  Ask for first and last name
        //  Force non-empty input
        do {
            System.out.print("Enter your first name: ");
            firstName = sc.nextLine();
        } while (firstName.isEmpty());

        System.out.print("Enter your last name: ");
        do {
            lastName = sc.nextLine();
        } while (lastName.isEmpty());

        //  Asking for new account number
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

        proceed = false;  //  Reset for PIN validation

        //  Asking for new pin
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

        //  Write new entry to database
        // JAMES THEY CANNNOT HAVE BOTH ACCOUNTS FROM ThE START THEY HAVE TO OPEM THEMMMMMasd
        db.writeLine(String.format(TEMPLATE, firstName, lastName, acc, pin, 0.0, 0.0));
    }

    public static boolean options() {
        //  Prompt for what the user wants to do
        //  Withdraw, Deposit, Open or Close Accounts, View Balance, Change Pin, and Log-out

        //  Initialize variables
        Scanner sc = new Scanner(System.in);
        String input = "";

        //  Prompt for action
        System.out.print("""
            What would you like to do?
             1. Withdraw
             2. Deposit
             3. Open an Account
             4. Close Accounts
             5. View Balance
             6. Change Pin
             7. Log out
            >\s"""
        );

        switch (input = sc.nextLine().toLowerCase()) {
            case "1", "withdraw"                ->  withdraw();
            case "2", "deposit"                 ->  deposit();
            case "3", "open", "open account"    ->  openAccount();
            case "4", "close", "close account"  ->  closeAccount();
            case "5", "balance", "view balance" ->  viewBalance();
            case "6", "change pin"              ->  changePin();
            case "7", "log out"                 ->  System.out.println(Format.BOLD + "Logging out..." + Format.RESET);
            default                             ->  System.out.println(Format.RED + "Invalid input." + Format.RESET);
        }

        return !input.equals("7");  //  Return false if user chooses to log out
    }

    public static void withdraw() {
        // Prompts the user which account they want to withdraw money from
        // If there is one account is automatically chooses
        // If there is no account it sends an error

        Scanner sc = new Scanner(System.in);
        CSV.Items item = db.readLine(entry);
        String input;
        int accounts;
        double amount = 0;

        if (item.getChequing() == -1 && item.getSavings() == -1) {
            accounts = 0;
        } else if (item.getChequing() != -1 && item.getSavings() == -1) {
            accounts = 1;
        } else if (item.getChequing() == -1 && item.getSavings() != -1) {
            accounts = 2;
        } else {
            accounts = 3;
        }

        switch (accounts) {
            case 0 -> {
                System.out.println("There are no accounts to withdraw from. Please open an account first.");
            }
            case 1 -> {
                System.out.print("How much would you like to withdraw from chequing?\n> ");
                input = sc.nextLine().strip();
                if (input.startsWith("$")) input = input.substring(1);  // Remove $ if provided

                // Parse input
                try {
                    amount = Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    System.out.println(Format.RED + "The provided value must be a number." + Format.RESET + "Returning to options menu.");
                }

                // Sanitize
                if (amount <= 0) {
                    System.out.println(Format.RED + "Cannot withdraw a zero or a negative value." + Format.RESET);
                } else if (amount > item.getChequing())  {
                    System.out.println(Format.RED + "Insufficient funds." + Format.RESET);
                }

                item.withdrawChequing(amount);

                // Update db
            }
            case 2 -> {
                System.out.print("How much would you like to withdraw from savings?\n> ");
                input = sc.nextLine().strip();
                if (input.startsWith("$")) input = input.substring(1);  // Remove $ if provided

                // Parse input
                try {
                    amount = Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    System.out.println(Format.RED + "The provided value must be a number." + Format.RESET + "Returning to options menu.");
                }

                // Sanitize
                if (amount <= 0) {
                    System.out.println(Format.RED + "Cannot withdraw a zero or a negative value." + Format.RESET);
                } else if (amount > item.getSavings())  {
                    System.out.println(Format.RED + "Insufficient funds." + Format.RESET);
                }

                item.withdrawSavings(amount);

                // Update db
            }
            case 3 -> {
                System.out.println("""
                    Which account would you like to withdraw the money from?
                     1. Chequing
                     2. Savings
                    >\s"""
                );

                input = sc.nextLine().strip();

                switch (input) {
                    case "1" -> {
                        System.out.print("How much would you like to withdraw from chequing?\n> ");
                        input = sc.nextLine().strip();
                        if (input.startsWith("$")) input = input.substring(1);  // Remove $ if provided

                        // Parse input
                        try {
                            amount = Double.parseDouble(input);
                        } catch (NumberFormatException e) {
                            System.out.println(Format.RED + "The provided value must be a number." + Format.RESET + "Returning to options menu.");
                        }

                        // Sanitize
                        if (amount <= 0) {
                            System.out.println(Format.RED + "Cannot withdraw a zero or a negative value." + Format.RESET);
                        } else if (amount > item.getChequing())  {
                            System.out.println(Format.RED + "Insufficient funds." + Format.RESET);
                        }

                        item.withdrawChequing(amount);

                        // Update db
                    }
                    case "2" -> {
                        System.out.print("How much would you like to withdraw from savings?\n> ");
                        input = sc.nextLine().strip();
                        if (input.startsWith("$")) input = input.substring(1);  // Remove $ if provided

                        // Parse input
                        try {
                            amount = Double.parseDouble(input);
                        } catch (NumberFormatException e) {
                            System.out.println(Format.RED + "The provided value must be a number." + Format.RESET + "Returning to options menu.");
                        }

                        // Sanitize
                        if (amount <= 0) {
                            System.out.println(Format.RED + "Cannot withdraw a zero or a negative value." + Format.RESET);
                        } else if (amount > item.getSavings())  {
                            System.out.println(Format.RED + "Insufficient funds." + Format.RESET);
                        }

                        item.withdrawSavings(amount);

                        // Update db
                    }
                    default -> System.out.println(Format.RED + "Invalid option." + Format.RESET + "Returning to options menu.");
                }
            }
            default -> System.out.println(Format.RED + "Invalid option." + Format.RESET + "Returning to options menu.");
        }
    }
    
    public static void deposit() {
        // Prompts the user which account they want to deposit money to
        // If there is one account is automatically chooses
        // If there is no account it sends an error

        Scanner sc = new Scanner(System.in);
        int accounts;
        int choice = 0;
        double amount = 0;
        CSV.Items item = db.readLine(entry);

        if ((item.getChequing() == -1) && (item.getSavings() == -1)) {
            accounts = 0;
        } else if (!(item.getChequing() == -1)) {
            accounts = 1;
        } else if (!(item.getSavings() == -1)) {
            accounts = 2;
        } else {
            accounts = 3;
        }

        if (accounts == 0) {
            System.out.println("No accounts to deposit to, please open an account first.");
            return;
        } else if (accounts == 1) {
            System.out.println("How much would you like to deposit to your chequing account?");
            try {
                amount = sc.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("You can only deposit a positive numerical amount.");
                deposit();
            }

            if (amount < 0) {
                System.out.println("You can only deposit a positive amount.");
                deposit();
            } else {
                System.out.println("Transaction complete, returning to options.");
                return;
            }
        } else if (accounts == 2) {
            System.out.println("How much would you like to deposit to your savings account?");
            try {
                amount = sc.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("You can only deposit a positive numerical amount.");
                deposit();
            }

            if (amount < 0) {
                System.out.println("You can only deposit a positive amount.");
                deposit();
            } else {
                System.out.println("Transaction complete, returning to options.");
                return;
            }
        } else if (accounts == 3) {
            System.out.println("Which account would you like to deposit to? 1 for chequing, and 2 for savings.");
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid account.");
                deposit();
            }

            if (choice == 1){
                System.out.println("How much would you like to deposit to your chequing account?");
                try {
                    amount = sc.nextDouble();
                } catch (InputMismatchException e) {
                    System.out.println("You can only deposit a numerical amount.");
                    deposit();
                }

                if (amount < 0) {
                    System.out.println("You can only deposit a positive amount.");
                    deposit();
                } else {
                    // send back or something
                    System.out.println("Transaction complete, returning to options.");
                }
            } else if (choice == 2){
                System.out.println("How much would you like to withdraw from your savings account?");
                try {
                    amount = sc.nextDouble();
                } catch (InputMismatchException e) {
                    System.out.println("You can only withdraw a numerical amount.");
                    deposit();
                }

                if (amount < 0) {
                    System.out.println("You can only deposit a positive amount.");
                    deposit();
                } else {
                    // send back or something
                    System.out.println("Transaction complete, returning to options.");
                }
            }
        }
    }

    public static void openAccount() {
        // Prompts the user if they want to create an account
        // if one account, it automatically creates the other
        // if two accounts, it outputs error and sends back to options
        // if no accounts, asks first, then creates

        Scanner sc = new Scanner(System.in);
        int accounts = 0;
        int choice = 0;
        CSV.Items item = db.readLine(entry);

        // RUN BY MR.SKUJA TO SEE IF I CAN PLACE THIS IN MAIN OR ANOTHER METHOD TO KEEP FROM REPEATING
        if ((item.getChequing() == -1) && (item.getSavings() == -1)) {
            accounts = 0;
        } else if (!(item.getChequing() == -1)) {
            accounts = 1;
        } else if (!(item.getSavings() == -1)) {
            accounts = 2;
        } else {
            accounts = 3;
        }

        if (accounts == 0) {
            System.out.println("What account would you like to open? 1 for chequing and 2 for savings.");
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid account.");
                openAccount();
            }
            
            if (choice == 1) {
                System.out.println("Account created, sending back to options.");
                // insert code to make chequing = $0.00 and not null
            } else if (choice == 2) {
                System.out.println("Account created, sending back to options.");
                // insert code to make savings = $0.00 and not null
            }
        } else if (accounts == 1) {
            System.out.println("Savings account created, sending back to options.");
            // insert code to make savings account = $0.00 and not null
        } else if (accounts == 2) {
            System.out.println("Chequing account created, sending back to options.");
            // insert code to make chequing account = $0.00 and not null
        } else if (accounts == 3) {
            System.out.println("Error, both accounts already open, sending back to options.");
        }
    }
    public static void closeAccount() {
        // Prompts the user if they want to close an account
        // if one account, it automatically closes the other
        // if no accounts, it outputs error and sends back to options
        // if two accounts, asks first, then closes

        Scanner sc = new Scanner(System.in);
        int accounts =0;
        int choice =0;
        CSV.Items item = db.readLine(entry);

        if ((item.getChequing() == -1) && (item.getSavings() == -1)) {
            accounts = 0;
        } else if (!(item.getChequing() == -1)) {
            accounts = 1;
        } else if (!(item.getSavings() == -1)) {
            accounts = 2;
        } else {
            accounts = 3;
        }

        if (accounts == 0) {
            System.out.println("Error, no accounts to close, sending back to options.");
        } else if (accounts == 1) {
            System.out.println("Chequing account closed, sending back to options.");
            // insert code to make chequing account = null
        } else if (accounts == 2) {
            System.out.println("Savings account closed, sending back to options.");
            // insert code to make savings account = null
        } else if (accounts == 3) {
            System.out.println("What account would you like to close? 1 for chequing and 2 for savings.");
            try {
                choice = sc.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid account.");
                openAccount();
            }
            if (choice == 1) {
                System.out.println("Account closed, sending back to options.");
                // insert code to make chequing = null
            } else if (choice == 2) {
                System.out.println("Account closed, sending back to options.");
                // insert code to make savings = null
            }
        }
    }

    public static void viewBalance() {
        // This method prints out the balances of your accounts
        // if there are no accounts, it sends an error and redirects back to options

        // FOR THE REDIRECT OPTIONS YOU DON'T NEED 2 IF IT AUTOMATICALLY CHOOSES WHICH TO DISPLAY

        Scanner sc = new Scanner(System.in);
        CSV.Items item = db.readLine(entry);
        boolean display = false;

        // My solution
        if (item.getChequing() == -1) {  // Display balance for chequing account if exists
            System.out.println("Balance: " + Format.GREEN + "$" + item.getChequing() + Format.RESET);
            display = true;
        }
        
        if (item.getSavings() == -1) {  // Display balance for savings account if exists, regardless of other accounts
            System.out.println("Balance: " + Format.GREEN + "$" + item.getSavings() + Format.RESET);
            display = true;
        }
        
        if (!display) {  // If neither displays, output this
            System.out.println("No accounts to display balance for. Please create an account first.");
        }
        
        
//        // Good but inefficient; See above for my solution
//        if ((Objects.isNull(item.getChequing()) && (Objects.isNull(item.getSavings())))) {
//            accounts = 0;
//        } else if (!(Objects.isNull(item.getChequing()))) {
//            accounts = 1;
//        } else if (!(Objects.isNull(item.getSavings()))) {
//            accounts = 2;
//        } else {
//            accounts = 3;
//        }
//
//        if (accounts == 0) {
//            System.out.println("No accounts to check, returning to options.");
//        } else if (accounts == 1) {
//            System.out.println("Your chequing account has $" + item.getChequing());
//            System.out.println("Press 1 to view other accounts balance and 2 to return to options.");
//            try {
//                choice = sc.nextInt();
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid option.");
//            }
//
//            if (choice == 1) {
//                System.out.println("Returning to viewing accounts balance.");
//                viewBalance();
//            } else if (choice == 2) {
//                System.out.println("Returning to options.");
//            }
//        } else if (accounts == 2) {
//            System.out.println("Your savings account has $" + item.getSavings());
//            System.out.println("Press 1 to view other accounts balance and 2 to return to options.");
//            try {
//                choice = sc.nextInt();
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid option.");
//            }
//
//            if (choice == 1) {
//                System.out.println("Returning to viewing accounts balance.");
//                viewBalance();
//            } else if (choice == 2) {
//                System.out.println("Returning to options.");
//            }
//        } else if (accounts == 3) {
//            System.out.println("Your chequing account has $" + item.getChequing());
//            System.out.println("Your savings account has $" + item.getSavings());
//            System.out.println("Press 1 to view other accounts balance and 2 to return to options.");
//            try {
//                choice = sc.nextInt();
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid option.");
//            }
//
//            if (choice == 1) {
//                System.out.println("Returning to viewing accounts balance.");
//                viewBalance();
//            } else if (choice == 2) {
//                System.out.println("Returning to options.");
//            }
//        }
    }

    public static void changePin() {
        // This method asks the user for a new pin
        // if valid it updates the database and returns back to options

        // JUAMES PLEASE CHECK IF THIS CODE WORKSSSSSSS PRETTY PLEASEUH also earlier code can we implement this looping sstem r smth??
        Scanner sc = new Scanner(System.in);
        String input;
        int pin = -1;
        boolean proceed = false;

        while (!proceed) {
            System.out.print("Enter a pin:");
            input = sc.nextLine();
            if (Validate.pin(input)) {
                pin = Integer.parseInt(input);
                proceed = true;
            } else {
                System.out.println(Format.RED + "PIN must be a positive four-digit number." + Format.RESET);
            }
        }
    }
}
