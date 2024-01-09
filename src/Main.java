package src;

import java.util.*;
import java.lang.*;

// SKUJA ADVICE: CHECK IF NULL IS NULL AND NOT JUST EMPTY OR ANY OTHER VALUE, IF SO, MAKE AN IF TO TELL PROGRAM THAT THAT EQUALS NULL

public class Main {
    //  Constants // 
    static final String DB = "db.csv";  //  Path to database file
    static CSV db = new CSV(DB);  //  Open database
    static int entry;  //  Index of current entry in database

    static final String TEMPLATE = "%s,%s,%s,%s,%f,%f";  //  Used for formatting database entries

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

    /**
     * ╔═════════════════════════════════════════════════╗
     * ║                    SESSIONS                     ║
     * ╠══════════════╦══════════════════════════════════╣
     * ║ 12/14/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Roughly documented main loop     ║
     * ║              ║ logic. Wrote initial loop        ║
     * ║              ║ prototype.                       ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/15/2023   ║ James Tung                       ║
     * ║              ║ Updated loop logic to use two    ║
     * ║              ║ loops.                           ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/17/2023   ║ James Tung                       ║
     * ║              ║ Updated loop logic to rely on    ║
     * ║              ║ return values. Also resets       ║
     * ║              ║ static variables each log in.    ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/18/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Wrapped both loops in a third    ║
     * ║              ║ infinite loop.                   ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/18/2023   ║ James Tung                       ║
     * ║              ║ Loops split into two separate    ║
     * ║              ║ whiles under one infinite loop.  ║
     * ║              ║ Done to allow independent        ║
     * ║              ║ repetition of either loop body.  ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/24/2023   ║ James Tung                       ║
     * ║              ║ Options loop set to loop         ║
     * ║              ║ infinitely for debug purposes.   ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/25/2023   ║ James Tung                       ║
     * ║              ║ Loop variables to control        ║
     * ║              ║ repetition. <code>entry</code>   ║
     * ║              ║ reset to -1 before log in.       ║
     * ║              ║ Options loop condition reset to  ║
     * ║              ║ catch log out events.            ║
     * ╚══════════════╩══════════════════════════════════╝
     * The overarching logic loop.
     *
     * <p>Calls each function representing one step in the control cycle.
     * <p>
     * Allows the user to log in, and proceed to {@link #options() options}. If the user was unsuccessful, prompt
     * user to try again or to {@link #signUp() sign up}. Achieves this through a loop which only breaks when the
     * login/sign up is successful.
     * <p>Calls {@link #options() options()} repeatedly, until the "log out" response is encountered.
     */
    public static void main(String[] args) {
        //  Log in and prompt for acc_num and pin
        //  If valid send to Option Select
        //  If invalid send to Sign Up
        //  If user chooses to log out, send back to Log in screen to loop infinitely

        boolean repeat;
        boolean loop;

        System.out.println(Format.BOLD + "Welcome to Tungular Bank!" + Format.RESET);

        while (true) {
            repeat = true;
            loop = true;

            while (repeat) {  //  Repeat log in if user chooses to try again
                entry = -1;  //  Reset entry index
                repeat = logIn();
            }

            while (loop) {
                loop = options();  //  If user chooses to log out, return false
            }
        }
    }

    /**
     * ╔═════════════════════════════════════════════════╗
     * ║                    SESSIONS                     ║
     * ╠══════════════╦══════════════════════════════════╣
     * ║ 12/17/2023   ║ James Tung                       ║
     * ║              ║ Entire function written during   ║
     * ║              ║ this period.                     ║
     * ╚══════════════╩══════════════════════════════════╝
     * Locates an account using and account number and pin.
     *
     * <p>Reads through accounts.csv and checks that both account number and PIN match.
     * <p>Ensured to work since each account number can only be issued once.
     *
     * @param acc   The user's account number, used to validate the login.
     * @param pin   The user's PIN, used to validate the login.
     * @return      Returns the line number the account information is found on.
     *              If a matching account-PIN pair is not found, returns -1.
     */
    private static int findEntry(String acc, String pin) {
        db.setLine(0);
        CSV.Items item = db.readLine();  //  Read first line

        while (item != null) {
            if (acc.equals(item.num) && pin.equals(item.pin)) {  //  Check if account number and PIN match
                return db.getLine();
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
        String acc = "";
        String pin = "";
        boolean proceed = false;

        // Prompt for account number and validate
        while (!proceed) {
            System.out.print("Enter your six-digit account number: ");
            input = sc.nextLine();
            if (Validate.accountNumber(input)) {
                acc = input;
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

        proceed = false;  //  Reset for PIN validation

        //  Prompt for PIN and validate
        while (!proceed) {
            System.out.print("Enter your four-digit PIN: ");
            input = sc.nextLine();
            if (Validate.pin(input)) {
                pin = input;
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
            if (db.exists(acc)) {
                System.out.println(Format.RED + "Account number and PIN do not match." + Format.RESET);
            } else {
                System.out.println(Format.RED + "Account not found." + Format.RESET);
            }
            System.out.print("Would you like to:\n1. Try again\n2. Sign up\n> ");
            return switch (sc.next()) {
                case "1" -> true;
                case "2" -> {
                    signUp();
                    yield false;
                }
                default -> {
                    System.out.println(Format.RED + "Invalid input." + Format.RESET + "Returning to login.");
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
        //  Send back to login()

        Scanner sc = new Scanner(System.in);
        String firstName, lastName, input;
        String acc = "";
        String pin = "";
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
                if (db.exists(input)) {
                    System.out.println(Format.YELLOW + "Account number in use." + Format.RESET);
                } else {
                    acc = input;
                    proceed = true;
                }
            } else {
                System.out.println(Format.RED + "Account number must be a positive six-digit number." + Format.RESET);
            }
        }

        proceed = false;  //  Reset for PIN validation

        //  Asking for new pin
        while (!proceed) {
            System.out.print("Create a four-digit pin for your new account: ");
            input = sc.nextLine();
            if (Validate.pin(input)) {
                pin = input;
                proceed = true;
            } else {
                System.out.println(Format.RED + "PIN must be a positive four-digit number." + Format.RESET);
            }
        }

        //  Write new entry to database
        entry = db.writeLine(String.format(TEMPLATE, firstName, lastName, acc, pin, -1.0, -1.0), entry);
    }

    public static boolean options() {
        //  Prompt for what the user wants to do
        //  Withdraw, Deposit, Open or Close Accounts, View Balance, Change Pin, and Log-out

        //  Initialize variables
        Scanner sc = new Scanner(System.in);
        String input;

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

        // Select option
        input = sc.nextLine().toLowerCase();
        switch (input) {
            case "1" -> withdraw();
            case "2" -> deposit();
            case "3" -> openAccount();
            case "4" -> closeAccount();
            case "5" -> viewBalance();
            case "6" -> changePin();
            case "7" -> System.out.println(Format.BOLD + "Logging out..." + Format.RESET);
            default  -> System.out.println(Format.RED + "Invalid input." + Format.RESET);
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
        double amount;

        if (item.chequing == -1 && item.savings == -1) {
            accounts = 0;
        } else if (item.chequing != -1 && item.savings == -1) {
            accounts = 1;
        } else if (item.chequing == -1 && item.savings != -1) {
            accounts = 2;
        } else {
            accounts = 3;
        }

        switch (accounts) {
            case 0 -> {
                System.out.println("There are no accounts to withdraw from. Please open an account first.");
                return;
            }
            case 1 -> {
                System.out.print("How much would you like to withdraw from chequing?\n> $");
                input = sc.nextLine().strip();
                if (input.startsWith("$")) input = input.substring(1);  // Remove $ if provided

                // Parse input
                try {
                    amount = Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    System.out.println(Format.RED + "The provided value must be a number." + Format.RESET + "Returning to options menu.");
                    return;
                }

                // Sanitize
                if (amount <= 0) {
                    System.out.println(Format.RED + "Cannot withdraw a zero or a negative value." + Format.RESET);
                    return;
                } else if (amount > item.chequing)  {
                    System.out.println(Format.RED + "Insufficient funds." + Format.RESET);
                    return;
                }

                item.chequing -= amount;
            }
            case 2 -> {
                System.out.print("How much would you like to withdraw from savings?\n> $");
                input = sc.nextLine().strip();
                if (input.startsWith("$")) input = input.substring(1);  // Remove $ if provided

                // Parse input
                try {
                    amount = Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    System.out.println(Format.RED + "The provided value must be a number." + Format.RESET + "Returning to options menu.");
                    return;
                }

                // Sanitize
                if (amount <= 0) {
                    System.out.println(Format.RED + "Cannot withdraw a zero or a negative value." + Format.RESET);
                    return;
                } else if (amount > item.savings)  {
                    System.out.println(Format.RED + "Insufficient funds." + Format.RESET);
                    return;
                }

                item.savings -= amount;
            }
            case 3 -> {
                System.out.print("""
                    Which account would you like to withdraw the money from?
                     1. Chequing
                     2. Savings
                    >\s"""
                );

                input = sc.nextLine().strip();

                switch (input) {
                    case "1" -> {
                        System.out.print("How much would you like to withdraw from chequing?\n> $");
                        input = sc.nextLine().strip();
                        if (input.startsWith("$")) input = input.substring(1);  // Remove $ if provided

                        // Parse input
                        try {
                            amount = Double.parseDouble(input);
                        } catch (NumberFormatException e) {
                            System.out.println(Format.RED + "The provided value must be a number." + Format.RESET + "Returning to options menu.");
                            return;
                        }

                        // Sanitize
                        if (amount <= 0) {
                            System.out.println(Format.RED + "Cannot withdraw a zero or a negative value." + Format.RESET);
                            return;
                        } else if (amount > item.chequing)  {
                            System.out.println(Format.RED + "Insufficient funds." + Format.RESET);
                            return;
                        }

                        item.chequing -= amount;
                    }
                    case "2" -> {
                        System.out.print("How much would you like to withdraw from savings?\n> $");
                        input = sc.nextLine().strip();
                        if (input.startsWith("$")) input = input.substring(1);  // Remove $ if provided

                        // Parse input
                        try {
                            amount = Double.parseDouble(input);
                        } catch (NumberFormatException e) {
                            System.out.println(Format.RED + "The provided value must be a number." + Format.RESET + "Returning to options menu.");
                            return;
                        }

                        // Sanitize
                        if (amount <= 0) {
                            System.out.println(Format.RED + "Cannot withdraw a zero or a negative value." + Format.RESET);
                            return;
                        } else if (amount > item.savings)  {
                            System.out.println(Format.RED + "Insufficient funds." + Format.RESET);
                            return;
                        }

                        item.savings -= amount;
                    }
                    default -> {
                        System.out.println(Format.RED + "Invalid option." + Format.RESET + "Returning to options menu.");
                        return;
                    }
                }
            }
            default -> {
                System.out.println(Format.RED + "Invalid option." + Format.RESET + "Returning to options menu.");
                return;
            }
        }
        // Update db
        db.writeLine(item, entry);

        System.out.println(Format.GREEN + "Transaction complete." + Format.RESET + "Returning to options menu.");
    }

    public static void deposit() {
        // Prompts the user which account they want to deposit money to
        // If there is one account is automatically chooses
        // If there is no account it sends an error

        Scanner sc = new Scanner(System.in);
        CSV.Items item = db.readLine(entry);
        String input;
        int accounts;
        double amount;

        if (item.chequing == -1 && item.savings == -1) {
            accounts = 0;
        } else if (item.chequing != -1 && item.savings == -1) {
            accounts = 1;
        } else if (item.chequing == -1 && item.savings != -1) {
            accounts = 2;
        } else {
            accounts = 3;
        }

        switch (accounts) {
            case 0 -> {
                System.out.println("No accounts to deposit to, please open an account first.");
                return;
            }
            case 1 -> {
                System.out.print("How much would you like to deposit to chequing?\n> $");
                input = sc.nextLine().strip();
                if (input.startsWith("$")) input = input.substring(1);  // Remove $ if provided

                // Parse input
                try {
                    amount = Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    System.out.println(Format.RED + "The provided value must be a number." + Format.RESET + "Returning to options menu.");
                    return;
                }

                // Sanitize
                if (amount <= 0) {
                    System.out.println(Format.RED + "Cannot deposit a zero or a negative value." + Format.RESET);
                    return;
                }

                item.chequing += amount;
            }
            case 2 -> {
                System.out.print("How much would you like to deposit to savings?\n> $");
                input = sc.nextLine().strip();
                if (input.startsWith("$")) input = input.substring(1);  // Remove $ if provided

                // Parse input
                try {
                    amount = Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    System.out.println(Format.RED + "The provided value must be a number." + Format.RESET + "Returning to options menu.");
                    return;
                }

                // Sanitize
                if (amount <= 0) {
                    System.out.println(Format.RED + "Cannot deposit a zero or a negative value." + Format.RESET);
                    return;
                }

                item.savings += amount;
            }
            case 3 -> {
                System.out.print("""
                    Which account would you like to deposit the money to?
                     1. Chequing
                     2. Savings
                    >\s"""
                );

                input = sc.nextLine().strip();

                switch (input) {
                    case "1" -> {
                        System.out.print("How much would you like to deposit to chequing?\n> $");
                        input = sc.nextLine().strip();
                        if (input.startsWith("$")) input = input.substring(1);  // Remove $ if provided

                        // Parse input
                        try {
                            amount = Double.parseDouble(input);
                        } catch (NumberFormatException e) {
                            System.out.println(Format.RED + "The provided value must be a number." + Format.RESET + "Returning to options menu.");
                            return;
                        }

                        // Sanitize
                        if (amount <= 0) {
                            System.out.println(Format.RED + "Cannot deposit a zero or a negative value." + Format.RESET);
                            return;
                        }

                        item.chequing += amount;
                    }
                    case "2" -> {
                        System.out.print("How much would you like to deposit to savings?\n> $");
                        input = sc.nextLine().strip();
                        if (input.startsWith("$")) input = input.substring(1);  // Remove $ if provided

                        // Parse input
                        try {
                            amount = Double.parseDouble(input);
                        } catch (NumberFormatException e) {
                            System.out.println(Format.RED + "The provided value must be a number." + Format.RESET + "Returning to options menu.");
                            return;
                        }

                        // Sanitize
                        if (amount <= 0) {
                            System.out.println(Format.RED + "Cannot deposit a zero or a negative value." + Format.RESET);
                            return;
                        }

                        item.savings += amount;
                    }
                    default -> {
                        System.out.println(Format.RED + "Invalid option." + Format.RESET + "Returning to options menu.");
                        return;
                    }
                }
            }
            default -> {
                System.out.println(Format.RED + "Invalid option." + Format.RESET + "Returning to options menu.");
                return;
            }
        }

        // Update db
        db.writeLine(item, entry);

        System.out.println(Format.GREEN + "Transaction complete." + Format.RESET + "Returning to options menu.");
    }

    public static void openAccount() {
        // Prompts the user if they want to create an account
        // if one account, it automatically creates the other
        // if two accounts, it outputs error and sends back to options
        // if no accounts, asks first, then creates

        Scanner sc = new Scanner(System.in);
        CSV.Items item = db.readLine(entry);
        String input;
        int accounts;

        // RUN BY MR.SKUJA TO SEE IF I CAN PLACE THIS IN MAIN OR ANOTHER METHOD TO KEEP FROM REPEATING
        if (item.chequing == -1 && item.savings == -1) {  // Both uninitialized
            accounts = 0;
        } else if (item.chequing != -1 && item.savings == -1) {  // Chequing initialized
            accounts = 1;
        } else if (item.chequing == -1 && item.savings != -1) {  // Savings initialized
            accounts = 2;
        } else {  // Both initialized
            accounts = 3;
        }

        switch (accounts) {
            case 0 -> {
                System.out.print("""
                    Which account would you like to open?
                     1. Chequing
                     2. Savings
                    >\s"""
                );

                input = sc.nextLine().strip();

                switch (input) {
                    case "1" -> {
                        item.chequing = 0;

                        System.out.println(Format.GREEN + "Chequing account successfully opened." + Format.RESET + "Returning to options menu.");
                    }
                    case "2" -> {
                        item.savings = 0;

                        System.out.println(Format.GREEN + "Savings account successfully opened." + Format.RESET + "Returning to options menu.");
                    }
                    default -> {
                        System.out.println(Format.RED + "Invalid option." + Format.RESET + "Returning to options menu.");
                        return;
                    }
                }
            }
            case 1 -> {
                item.savings = 0;

                System.out.println(Format.GREEN + "Savings account successfully opened." + Format.RESET + "Returning to options menu.");
            }
            case 2 -> {
                item.chequing = 0;

                System.out.println(Format.GREEN + "Chequing account successfully opened." + Format.RESET + "Returning to options menu.");
            }
            case 3 -> {
                System.out.println(Format.RED + "Both accounts already open." + Format.RESET + "Returning to options menu.");
                return;
            }
            default -> {
                System.out.println(Format.RED + "Invalid option." + Format.RESET + "Returning to options menu.");
                return;
            }
        }

        // Update db
        db.writeLine(item, entry);
    }

    public static void closeAccount() {
        // Prompts the user if they want to close an account
        // if one account, it automatically closes the other
        // if no accounts, it outputs error and sends back to options
        // if two accounts, asks first, then closes

        Scanner sc = new Scanner(System.in);
        CSV.Items item = db.readLine(entry);
        int accounts;
        String input;

        if (item.chequing == -1 && item.savings == -1) {  // Both uninitialized
            accounts = 0;
        } else if (item.chequing != -1 && item.savings == -1) {  // Chequing initialized
            accounts = 1;
        } else if (item.chequing == -1 && item.savings != -1) {  // Savings initialized
            accounts = 2;
        } else {  // Both initialized
            accounts = 3;
        }

        switch (accounts) {
            case 0 -> {
                System.out.println(Format.RED + "No accounts to close." + Format.RESET + "Returning to options menu.");
                return;
            }
            case 1 -> {
                item.chequing = -1;

                System.out.println(Format.GREEN + "Chequing account closed." + Format.RESET + "Returning to options menu.");
            }
            case 2 -> {
                item.savings = -1;

                System.out.println(Format.GREEN + "Savings account closed." + Format.RESET + "Returning to options menu.");
            }
            case 3 -> {
                System.out.print("""
                    Which account would you like to close?
                     1. Chequing
                     2. Savings
                    >\s"""
                );

                input = sc.nextLine().strip();

                switch (input) {
                    case "1" -> {
                        item.chequing = -1;

                        System.out.println(Format.GREEN + "Chequing account successfully closed." + Format.RESET + "Returning to options menu.");
                    }
                    case "2" -> {
                        item.savings = -1;

                        System.out.println(Format.GREEN + "Savings account successfully closed." + Format.RESET + "Returning to options menu.");
                    }
                    default -> {
                        System.out.println(Format.RED + "Invalid option." + Format.RESET + "Returning to options menu.");
                        return;
                    }
                }
            }
            default -> {
                System.out.println(Format.RED + "Invalid option." + Format.RESET + "Returning to options menu.");
                return;
            }
        }

        // Update DB
        db.writeLine(item, entry);
    }

    public static void viewBalance() {
        // This method prints out the balances of your accounts
        // if there are no accounts, it sends an error and redirects back to options

        CSV.Items item = db.readLine(entry);
        boolean display = false;

        // My solution
        if (item.chequing != -1) {  // Display balance for chequing account if exists
            System.out.printf("Chequing Balance: " + Format.GREEN + "$%.2f" + Format.RESET + "%n", item.chequing);
            display = true;
        }
        
        if (item.savings != -1) {  // Display balance for savings account if exists, regardless of other accounts
            System.out.printf("Savings Balance: " + Format.GREEN + "$%.2f" + Format.RESET + "%n", item.savings);
            display = true;
        }
        
        if (!display) {  // If neither displays, output this
            System.out.println(Format.YELLOW + "No accounts to display balance for. Please create an account first." + Format.RESET);
        }
    }

    public static void changePin() {
        // This method asks the user for a new pin
        // if valid it updates the database and returns back to options

        // JUAMES PLEASE CHECK IF THIS CODE WORKSSSSSSS PRETTY PLEASEUH also earlier code can we implement this looping sstem r smth??
        Scanner sc = new Scanner(System.in);
        CSV.Items item = db.readLine(entry);
        String input;
        String pin = "";
        boolean proceed = false;

        while (!proceed) {
            System.out.print("Enter your new PIN: ");
            input = sc.nextLine();
            if (Validate.pin(input)) {
                pin = input;
                proceed = true;
            } else {
                System.out.println(Format.RED + "PIN must be a positive four-digit number." + Format.RESET);
            }
        }

        item.pin = pin;
        db.writeLine(item, entry);

        System.out.println(Format.GREEN + "PIN successfully updated." + Format.RESET + "Returning to options menu.");
    }
}
