package model;

import org.json.JSONObject;
import persistence.Writable;


public class Goal implements Writable {
    private String goalName;
    private double goalAmount;
    private boolean status;

    public Goal(String name, double amount) {
        goalName = name;
        goalAmount = amount;
        status = false;
    }

    public String getGoalName() {
        return goalName;
    }

    public double getGoalAmount() {
        return goalAmount;
    }

    public boolean getStatus() {
        return status;
    }

    // REQUIRES: amount >= goalAmount
    // MODIFIES: this
    // EFFECTS: subtract amount from goalAmount
    public void removeMoney(double amount) {
        goalAmount = goalAmount - amount;
        if (goalAmount == 0) {
            status = true;
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", goalName);
        json.put("amount", goalAmount);
        return json;
    }
}
