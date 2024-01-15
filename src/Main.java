/*
 * TITLE: ATM-Interface
 * AUTHOR: James Tung, Jon Jon Feng
 * COURSE: ICS3
 * DATE CREATED: 12/14/2023
 */

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
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/22/2023   ║ James Tung                       ║
     * ║              ║ Getters replaced with simple     ║
     * ║              ║ attribute reads.                 ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/24/2023   ║ James Tung                       ║
     * ║              ║ Parameter type changed from int  ║
     * ║              ║ to String. Equality checking     ║
     * ║              ║ updated accordingly.             ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/25/2023   ║ James Tung                       ║
     * ║              ║ DB entry point set to always be  ║
     * ║              ║ the beginning of the file.       ║
     * ╚══════════════╩══════════════════════════════════╝
     * Locates an account using an account number and pin.
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

    /**
     * ╔═════════════════════════════════════════════════╗
     * ║                    SESSIONS                     ║
     * ╠══════════════╦══════════════════════════════════╣
     * ║ 12/14/2023   ║ James Tung                       ║
     * ║              ║ Method was created. Created a    ║
     * ║              ║ basic checking system to see if  ║
     * ║              ║ the account number and PIN were  ║
     * ║              ║ numbers.                         ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/15/2023   ║ James Tung                       ║
     * ║              ║ Check if inputs are of the       ║
     * ║              ║ correct length (account number:  ║
     * ║              ║ 6, PIN: 4). Return type changed  ║
     * ║              ║ from void to boolean. Created    ║
     * ║              ║ the proceed variable. Added code ║
     * ║              ║ to allow the user to try again   ║
     * ║              ║ if input was invalid but of the  ║
     * ║              ║ right format.                    ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/17/2023   ║ James Tung                       ║
     * ║              ║ Compare inputs to database.      ║
     * ║              ║ Allow the user to sign up if     ║
     * ║              ║ they enter an account number     ║
     * ║              ║ which is not in use yet.         ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/21/2023   ║ James Tung                       ║
     * ║              ║ Method overhauled to adhere to a ║
     * ║              ║ good style. Use external methods ║
     * ║              ║ to check validity of input.      ║
     * ╚══════════════╩══════════════════════════════════╝
     * Handles the process of logging a user in.
     *
     * <p>Asks for an account number and calls an external verify method to ensure the account number is valid
     *    (6 digits). If invalid, asks the user if they would like to try again, or to sign up instead.
     * <p>Asks for a PIN and calls an external verification method to ensure the PIN is valid (4 digits). If invalid,
     *    asks the user if they would like to try again with a different account number. If not, ask for a PIN again.
     * <p>Search the database file to find a matching account-PIN pair. If a matching pair is not found, check if the
     *    account number exists. Output corresponding messages, then ask if the user would like to try again with a
     *    different account-PIN combination or sign up.
     * <p>If the account-PIN pair is valid, exit method and advance to the next step of the main method.
     *
     * @return A boolean to represent whether the system should attempt to log in again or not.
     */
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

    /**
     * ╔═════════════════════════════════════════════════╗
     * ║                    SESSIONS                     ║
     * ╠══════════════╦══════════════════════════════════╣
     * ║ 12/14/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Method created. Described the    ║
     * ║              ║ overall function of the method   ║
     * ║              ║ using pseudocode comments.       ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/15/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Created variables for scanner    ║
     * ║              ║ (Scanner), first name (String),  ║
     * ║              ║ last name (String), account      ║
     * ║              ║ number (int) and PIN (int).      ║
     * ║              ║ Output an error message if name  ║
     * ║              ║ fields are left blank. Check     ║
     * ║              ║ that account number is valid     ║
     * ║              ║ (6 digits) and PIN is valid      ║
     * ║              ║ (4 digits) using try/catch.      ║
     * ║              ║ Inline comments added.           ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/18/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Created variable input (String). ║
     * ║              ║ Use variable input to capture    ║
     * ║              ║ and validate user input before   ║
     * ║              ║ assigning to variables.          ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/18/2023   ║ James Tung                       ║
     * ║              ║ Created variable proceed         ║
     * ║              ║ (boolean) to loop on invalid     ║
     * ║              ║ data. Write new user information ║
     * ║              ║ to database.                     ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/21/2023   ║ James Tung                       ║
     * ║              ║ Reformatted the method using     ║
     * ║              ║ switch-cases for strict if       ║
     * ║              ║ statements, and external methods ║
     * ║              ║ for validation.                  ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/24/2023   ║ James Tung                       ║
     * ║              ║ Variables <code>acc</code> and   ║
     * ║              ║ <code>pin</pin> now Strings;     ║
     * ║              ║ variable assignments changed     ║
     * ║              ║ accordingly.                     ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/25/2023   ║ James Tung                       ║
     * ║              ║ Check if account number is in    ║
     * ║              ║ use already, and error if true.  ║
     * ║              ║ Edited user prompts to be more   ║
     * ║              ║ specific. Variable               ║
     * ║              ║ <code>entry</code> set to the    ║
     * ║              ║ return value of the database     ║
     * ║              ║ write.                           ║
     * ╚══════════════╩══════════════════════════════════╝
     * Handles the process of signing a new user up.
     *
     * <p>Reads in the first and last name of the user, an account number (6 digits) and a PIN (4 digits). First and
     *    last name are checked to make sure they are not empty. Account number and PIN are validated (correct length
     *    and is a number). Account number is checked to make sure it is not in use. Initializes the new user in the
     *    database with uninitialized bank accounts.
     */
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

    /**
     * ╔═════════════════════════════════════════════════╗
     * ║                    SESSIONS                     ║
     * ╠══════════════╦══════════════════════════════════╣
     * ║ 12/15/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Method created and logic         ║
     * ║              ║ outlined in pseudocode comments. ║
     * ║              ║ Options menu output works.       ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/19/2023   ║ James Tung                       ║
     * ║              ║ Used a switch case to add        ║
     * ║              ║ function to the menu.            ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/21/2023   ║ James Tung                       ║
     * ║              ║ Reformatted method.              ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/24/2023   ║ James Tung                       ║
     * ║              ║ Assignment to variable           ║
     * ║              ║ <code>input</code> extracted     ║
     * ║              ║ from switch condition.           ║
     * ╚══════════════╩══════════════════════════════════╝
     * Main menu for selecting next action.
     *
     * <p>Use a switch-case to jump to different methods based on user input. If input is "7" (the logout option),
     *    return false and break the loop in {@link #main(String[] args) main()}.
     *
     * @return The return value determines if options should be called again. On a logout event, <code>false</code> is
     *         returned, breaking out of the loop in main().
     */
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

    /**
     * ╔═════════════════════════════════════════════════╗
     * ║                    SESSIONS                     ║
     * ╠══════════════╦══════════════════════════════════╣
     * ║ 12/18/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Method created and described in  ║
     * ║              ║ pseudocode.                      ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/19/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Created variables scanner        ║
     * ║              ║ (Scanner), accounts (int),       ║
     * ║              ║ choice (int), finish (int),      ║
     * ║              ║ amount (double). Checks which    ║
     * ║              ║ accounts the user has opened,    ║
     * ║              ║ and changes response             ║
     * ║              ║ accordingly.                     ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/19/2023   ║ James TUng                       ║
     * ║              ║ Added try/catch to validate the  ║
     * ║              ║ withdraw amount (0 < amount <=   ║
     * ║              ║ balance). Implemented logic to   ║
     * ║              ║ send user to {@link #options()   ║
     * ║              ║ options()} upon transaction      ║
     * ║              ║ completion.                      ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/21/2023   ║ James Tung                       ║
     * ║              ║ Method reformatted.              ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/22/2023   ║ James Tung                       ║
     * ║              ║ Inlined some statements/blocks.  ║
     * ║              ║ Update with new method names     ║
     * ║              ║ (withdrawChequing ->             ║
     * ║              ║ updateChequing). Success         ║
     * ║              ║ messages implemented, and method ║
     * ║              ║ exits early if invalid data is   ║
     * ║              ║ encountered.                     ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/23/2023   ║ James Tung                       ║
     * ║              ║ Reverted the inlining of opened  ║
     * ║              ║ account checking code. Updates   ║
     * ║              ║ written to database.             ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/24/2023   ║ James Tung                       ║
     * ║              ║ All dead end cases return early. ║
     * ║              ║ Database write and success       ║
     * ║              ║ message are at the end of        ║
     * ║              ║ method.                          ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/25/2023   ║ James Tung                       ║
     * ║              ║ Account fields accessed directly ║
     * ║              ║ instead of through a method      ║
     * ║              ║ (updateChequing(-amount) ->      ║
     * ║              ║ .chequing -= amount).            ║
     * ╚══════════════╩══════════════════════════════════╝
     * Withdraw money from a user's accounts.
     *
     * <p>Depending on the accounts a user has open, decides (or asks if both are open) which account to withdraw from.
     *    If neither is open, error and exit.
     * <p>If the amount entered is zero or negative, or more than is in the account, error and exit. Otherwise, withdraw
     *    that amount and update the database.
     */
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

    /**
     * ╔═════════════════════════════════════════════════╗
     * ║                    SESSIONS                     ║
     * ╠══════════════╦══════════════════════════════════╣
     * ║ 12/19/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Method created and described     ║
     * ║              ║ using pseudocode. Variables      ║
     * ║              ║ created.                         ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/19/2023   ║ James Tung                       ║
     * ║              ║ Added try/catch to validate the  ║
     * ║              ║ withdraw amount (0 < amount <=   ║
     * ║              ║ balance). Implemented logic to   ║
     * ║              ║ send user to {@link #options()   ║
     * ║              ║ options()} upon transaction      ║
     * ║              ║ completion.                      ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/21/2023   ║ James Tung                       ║
     * ║              ║ Method reformatted.              ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/22/2023   ║ James Tung                       ║
     * ║              ║ Replace getter methods with      ║
     * ║              ║ direct access. Main if statement ║
     * ║              ║ converted into switch case.      ║
     * ║              ║ Account selection conditions     ║
     * ║              ║ simplified. Read input as a      ║
     * ║              ║ String and cast instead of       ║
     * ║              ║ reading double.                  ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/23/2023   ║ James Tung                       ║
     * ║              ║ Write updates to database.       ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/24/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Dead end cases exit early;       ║
     * ║              ║ database write and success       ║
     * ║              ║ message are at the end of        ║
     * ║              ║ method.                          ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/25/2023   ║ James Tung                       ║
     * ║              ║ Access account fields directly   ║
     * ║              ║ instead of through a method      ║
     * ║              ║ (updateChequing(amount) ->       ║
     * ║              ║ .chequing += amount).            ║
     * ╚══════════════╩══════════════════════════════════╝
     * Deposit money into a user's accounts.
     *
     * <p>Depending on the accounts a user has open, decides (or asks if both are open) which account to deposit to.
     *    If neither are open, error and exit.
     * <p>If the amount entered is zero or negative, error and exit. Otherwise, deposit to that account and update
     *    database.
     */
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

    /**
     * ╔═════════════════════════════════════════════════╗
     * ║                    SESSIONS                     ║
     * ╠══════════════╦══════════════════════════════════╣
     * ║ 12/20/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Method created and outlined in   ║
     * ║              ║ pseudocode. Variables created.   ║
     * ║              ║ Accounts check written.          ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/20/2023   ║ James Tung                       ║
     * ║              ║ Validated user choice when asked ║
     * ║              ║ to choose an account to open.    ║
     * ║              ║ Method will go back to {@link #options() options()} ║
     * ║              ║ upon completion.                 ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/21/2023   ║ James Tung                       ║
     * ║              ║ Reformatted method.              ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/22/2023   ║ James Tung                       ║
     * ║              ║ Access account fields directly   ║
     * ║              ║ instead of through a method      ║
     * ║              ║ (updateChequing(0) -> .chequing  ║
     * ║              ║ = 0). If statements replaced     ║
     * ║              ║ with switch-case.                ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/23/2023   ║ James Tung                       ║
     * ║              ║ Database write moved to end of   ║
     * ║              ║ method. Error and success        ║
     * ║              ║ messages implemented.            ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/24/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Account select menu uses print   ║
     * ║              ║ instead of println.              ║
     * ╚══════════════╩══════════════════════════════════╝
     * Opens an account for the user.
     *
     * <p>If both accounts are uninitialized, prompt the user to choose one. If one is already opened, open the other.
     *    If both are open, error and exit.
     * <p>To open an account, set the balance to 0. Database write is at the end of the method.
     */
    public static void openAccount() {
        // Prompts the user if they want to create an account
        // if one account, it automatically creates the other
        // if two accounts, it outputs error and sends back to options
        // if no accounts, asks first, then creates

        Scanner sc = new Scanner(System.in);
        CSV.Items item = db.readLine(entry);
        String input;
        int accounts;

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

    /**
     * ╔═════════════════════════════════════════════════╗
     * ║                    SESSIONS                     ║
     * ╠══════════════╦══════════════════════════════════╣
     * ║ 12/20/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Method created and documented in ║
     * ║              ║ pseudocode. Variables made.      ║
     * ║              ║ Added account check code.        ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/20/2023   ║ James Tung                       ║
     * ║              ║ Validated user choice when asked ║
     * ║              ║ to choose an account to close.   ║
     * ║              ║ Method will go back to {@link #options() options()} ║
     * ║              ║ upon completion.                 ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/21/2023   ║ James Tung                       ║
     * ║              ║ Method reformatted.              ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/22/2023   ║ James Tung                       ║
     * ║              ║ Access account fields directly   ║
     * ║              ║ instead of through a method      ║
     * ║              ║ (updateChequing(-1) -> .chequing ║
     * ║              ║ = -1).                           ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/23/2023   ║ James Tung                       ║
     * ║              ║ Account check reformatted. Begin ║
     * ║              ║ writing switch-case to replace   ║
     * ║              ║ if statement.                    ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/25/2023   ║ James Tung                       ║
     * ║              ║ Finish switch-case.              ║
     * ╚══════════════╩══════════════════════════════════╝
     * Closes an account for the user.
     *
     * <p>If both accounts are uninitialized error and exit. If one is open, close it. If both are open, prompt the
     *    user to choose one.
     * <p>To close an account, set the balance to -1. Method writes to database at the end.
     */
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

    /**
     * ╔═════════════════════════════════════════════════╗
     * ║                    SESSIONS                     ║
     * ╠══════════════╦══════════════════════════════════╣
     * ║ 12/20/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Method created and documented in ║
     * ║              ║ pseudocode. Made variables.      ║
     * ║              ║ Account checking code written.   ║
     * ║              ║ Check if input from prompt is    ║
     * ║              ║ valid, and if valid, print       ║
     * ║              ║ balances of accounts depending   ║
     * ║              ║ on which are open.               ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/21/2023   ║ James Tung                       ║
     * ║              ║ Overhauled method and            ║
     * ║              ║ drastically reduce complexity.   ║
     * ║              ║ Just print account if it is      ║
     * ║              ║ open.                            ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/22/2023   ║ James Tung                       ║
     * ║              ║ Access account fields directly   ║
     * ║              ║ instead of through a method      ║
     * ║              ║ (getChequing() -> .chequing).    ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/23/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Remove commented code            ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/25/2023   ║ James Tung                       ║
     * ║              ║ Conditions for printing          ║
     * ║              ║ corrected (needed negation).     ║
     * ║              ║ Print statements aesthetically   ║
     * ║              ║ improved.                        ║
     * ╚══════════════╩══════════════════════════════════╝
     * Display how much money the user has in their accounts.
     *
     * <p>Displays the balance for each account as long as it is open. If neither account is open, and error message
     *    displays.
     */
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

    /**
     * ╔═════════════════════════════════════════════════╗
     * ║                    SESSIONS                     ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/21/2023   ║ Jon Jon Feng                     ║
     * ║              ║ Created method and described     ║
     * ║              ║ function. Variables created and  ║
     * ║              ║ PIN validity is checked. Added   ║
     * ║              ║ code to write new PIN to         ║
     * ║              ║ database.                        ║
     * ╠══════════════╬══════════════════════════════════╣
     * ║ 12/25/2023   ║ James Tung                       ║
     * ║              ║ Variable <code>pin</code> is now ║
     * ║              ║ String; assignments to           ║
     * ║              ║ <code>pin</code> updated         ║
     * ║              ║ accordingly. Database update     ║
     * ║              ║ working and success message      ║
     * ║              ║ printing.                        ║
     * ╚══════════════╩══════════════════════════════════╝
     * Change a user's pin.
     *
     * <p>Ask for a new PIN and validate it (4 digits). If valid, update database and output success message.
     */
    public static void changePin() {
        // This method asks the user for a new pin
        // if valid it updates the database and returns back to options

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
