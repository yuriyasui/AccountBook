package ui;

import model.Account;
import model.Event;
import model.EventLog;
import model.Goal;
import ui.AccountBook;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class AccountBookGUI extends JFrame implements LogPrinter {

    private static final String JSON_STORE = "./data/accountBookGUI.json";
    private static final int WIDTH = 2000;
    private static final int HEIGHT = 700;
    private Account myAccount;
    private JDesktopPane desktop;
    private JInternalFrame controlPanel;
    private JInternalFrame dataPanel;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JTextArea previewTextArea;
    private JPanel graphicPanel;

    //
    public AccountBookGUI() {
        super("Account Book");
        myAccount = new Account();

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        graphicPanel = new JPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        initializeGraphics();
        initializeDataGraphics();
        addButtonPanel();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }


    // MODIFIES: this
    // EFFECTS:  draws the JFrame window where this AccountBook will operate, and populates the tools to be used
    //           to manipulate this drawing
    private void initializeGraphics() {
        desktop = new JDesktopPane();
        setContentPane(desktop);
        setTitle("Account Book");
        setSize(WIDTH, HEIGHT);

        controlPanel = new JInternalFrame("Control Panel", false, false, false, false);
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setSize(new Dimension(1000, 300));
        desktop.add(controlPanel);
        controlPanel.setLocation(0, 0);
        controlPanel.setVisible(true);
        previewTextArea = new JTextArea();
        previewTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(previewTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        controlPanel.add(scrollPane, BorderLayout.CENTER);
    }


    private void initializeDataGraphics() {
        dataPanel = new JInternalFrame("Data Panel", false, false, false, false);
        dataPanel.setLayout(new BorderLayout());
        dataPanel.setSize(new Dimension(1000, 300));
        desktop.add(dataPanel);
        dataPanel.setVisible(true);
        dataPanel.setLocation(0, 300);
        graphicPanel = new JPanel();
        dataPanel.add(graphicPanel, BorderLayout.CENTER);
    }

    /**
     * Adds preview screen
     */
    private void displayPreview() {
        Account account = myAccount;
        double remainingAmountToSave = account.totalAmountNeedToSave();
        StringBuilder previewText = new StringBuilder(String.format("Total amount need to save: $%.2f\n"
                        + "Goals need to achieve: %d\n",
                remainingAmountToSave, account.goalNotFinished().size()));


        List<Goal> goals = account.goalNotFinished();
        for (Goal goal : goals) {
            previewText.append(String.format("Goal: %s, Amount: $%.2f\n", goal.getGoalName(), goal.getGoalAmount()));
        }

        previewTextArea.setText(previewText.toString());
        Graphics g = graphicPanel.getGraphics();
        drawGraphic(g);
    }

    /**
     * Add buttons
     */
    public void addButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4,1));
        buttonPanel.add(new JButton(new AddMoney()));
        buttonPanel.add(new JButton(new SetGoalAction()));
        buttonPanel.add(new JButton(new SaveToFile()));
        buttonPanel.add(new JButton(new LoadFromFile()));

        controlPanel.add(buttonPanel, BorderLayout.WEST);
    }

    /**
     * Represents action to be taken when user wants to add money
     * to the accountBook.
     */
    private class AddMoney extends AbstractAction {
        AddMoney() {
            super("Add Money");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Account account = myAccount;
            String amountStr = JOptionPane.showInputDialog(null, "How much money do you want to add?",
                    "Enter amount to add: $", JOptionPane.QUESTION_MESSAGE);
            try {
                int amount = Integer.parseInt(amountStr);
                double target = account.totalAmountNeedToSave();
                if (target < amount) {
                    account.addMoney(amount);
                    JOptionPane.showMessageDialog(null, "Goal is achieved!!",
                            "Note", JOptionPane.ERROR_MESSAGE);
                } else if (amount >= 0.0) {
                    account.addMoney(amount);
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot add negative amount...",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                displayPreview();
                Graphics g = graphicPanel.getGraphics();
                drawGraphic(g);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Represents action to be taken when user wants to set a new goal
     * to the account book.
     */
    private class SetGoalAction extends AbstractAction {
        SetGoalAction() {
            super("Set A Goal");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Account account = myAccount;
            String goalName = JOptionPane.showInputDialog(null,
                    "What is the goal Name?",
                    "Goal Name",
                    JOptionPane.QUESTION_MESSAGE);

            String goalAmountStr = JOptionPane.showInputDialog(null,
                    "The goal amount: $",
                    "Goal Amount",
                    JOptionPane.QUESTION_MESSAGE);

            try {
                int goalAmount = Integer.parseInt(goalAmountStr);
                account.setGoal(goalName, goalAmount);
                JOptionPane.showMessageDialog(null, "Goal is set!!", "Note",
                        JOptionPane.ERROR_MESSAGE);
                displayPreview();
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Represents action to be taken when user wants to save the account book
     * data to the system.
     */
    private class SaveToFile extends AbstractAction {
        SaveToFile() {
            super("Save to File");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(myAccount);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null, "Saved account book to " + JSON_STORE, "Note",
                        JOptionPane.ERROR_MESSAGE);
                displayPreview();
            } catch (FileNotFoundException err) {
                JOptionPane.showMessageDialog(null, "Unable to write to file: " + JSON_STORE, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Represents action to be taken when user wants to load data saved
     * to the system.
     */
    private class LoadFromFile extends AbstractAction {
        LoadFromFile() {
            super("Load from file");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                myAccount = jsonReader.read();
                JOptionPane.showMessageDialog(null, "Loaded account from " + JSON_STORE, "Note",
                        JOptionPane.ERROR_MESSAGE);
                displayPreview();
            } catch (IOException err) {
                JOptionPane.showMessageDialog(null, "Unable to read from file: " + JSON_STORE, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    // Draw a rectangle representing the total amount needed to save
    private void drawGraphic(Graphics g) {
        int totalAmount = (int) myAccount.totalAmountNeedToSave();

        g.setColor(Color.WHITE); // Use the background color
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLUE);
        g.fillRect(10, 10, totalAmount, 20);

        g.setColor(Color.BLACK);
        for (int i = 0; i <= totalAmount; i += 50) {
            g.drawLine(10 + i, 30, 10 + i, 40);
        }
    }


    public void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(next);
        }
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            handleWindowClosing();
        }
        super.processWindowEvent(e);
    }

    private void handleWindowClosing() {
        printLog(EventLog.getInstance());
        System.exit(0);
    }

    // starts the application
    public static void main(String[] args) {
        new AccountBookGUI();
    }
}
