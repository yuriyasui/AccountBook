package ui;

import model.Account;
import model.Goal;
import persistence.JsonReader;
import persistence.JsonWriter;
import model.EventLog;
import model.Event;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class AccountBook implements LogPrinter {

    private static final String JSON_STORE = "./data/accountBook.json";
    private Account myAccount;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the account book application
    public AccountBook() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runAccountBook();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runAccountBook() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
                printLog(EventLog.getInstance());
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");


    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tp -> preview");
        System.out.println("\ta -> add money");
        System.out.println("\tg -> set a goal");
        System.out.println("\ts -> save account book to file");
        System.out.println("\tl -> load account book from file");
        System.out.println("\tq -> quit");
    }

    private void processCommand(String command) {
        if (command.equals("p")) {
            seePreview();
        } else if (command.equals("a")) {
            addMoney();
        } else if (command.equals("g")) {
            setGoal();
        } else if (command.equals("s")) {
            saveFile();
        } else if (command.equals("l")) {
            loadFile();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // EFFECTS: display total amount need to save and number of goals need to achieve at console
    private void seePreview() {
        Account account = myAccount;
        System.out.printf("Total amount need to save: $%.2f\n", account.totalAmountNeedToSave());
        System.out.printf("Number of goals need to achieve: %d\n", account.goalNotFinished().size());
        printGoals(account);
    }

    // MODIFIES: this
    // EFFECTS: add money to saved
    private void addMoney() {
        Account account = myAccount;
        System.out.print("Enter amount to add: $");
        double amount = input.nextDouble();
        double target = account.totalAmountNeedToSave();

        if (target < amount) {
            account.addMoney(amount);
            System.out.println("Every goals are achieved!!\n");
        } else if (amount >= 0.0) {
            account.addMoney(amount);
        } else {
            System.out.println("Cannot add negative amount...\n");
        }
        printSaved(account);
        printGoals(account);
    }

    // MODIFIES: this
    // EFFECTS: set the goal
    private void setGoal() {
        Account account = myAccount;
        System.out.print("Enter the goal name:");
        String name = input.next();
        System.out.print("Enter the goal amount: $");
        double amount = input.nextDouble();

        account.setGoal(name, amount);

    }

    // EFFECTS: display the amount saved at console
    private void printSaved(Account account) {
        System.out.printf("Saved: $%.2f\n", account.getAmountSaved());
    }

    // EFFECTS: print goals which is not achieved at console
    private void printGoals(Account account) {
        System.out.println("Goals:");
        List<Goal> notFinishedGoals = account.goalNotFinished();
        for (Goal goal: notFinishedGoals) {
            System.out.printf(goal.getGoalName() + " amount " + goal.getGoalAmount() + "\n");
        }
    }

    // EFFECTS: saves the accountBook to file
    private void saveFile() {
        try {
            jsonWriter.open();
            jsonWriter.write(myAccount);
            jsonWriter.close();
            System.out.println("Saved account book to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: load the accountBook saved before from the file
    private void loadFile() {
        try {
            myAccount = jsonReader.read();
            System.out.println("Loaded account from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    private void init() {
        myAccount = new Account();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    public void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(next);
        }
    }
}
