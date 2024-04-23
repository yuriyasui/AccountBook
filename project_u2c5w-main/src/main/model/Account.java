package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

public class Account implements Writable {
    private double saved; // saved
    private List<Goal> goals;   // target saved

    public Account() {
        saved = 0;
        goals = new ArrayList<>();
    }

    public double getAmountSaved() {
        return saved;
    }

    // EFFECTS: return total amount till all goals to be achieved
    public double totalAmountNeedToSave() {
        double total = 0;
        for (Goal goal: goals) {
            total = total + goal.getGoalAmount();
        }
        return total;//stub
    }

    // REQUIRES: amount > 0
    // MODIFIES: this, goals
    // EFFECTS: amount is added to the amount saved and first goal
    // on the list's amount is reduced by amount
    public void addMoney(double amount) { // stub
        EventLog.getInstance().logEvent(
                new Event("Added money $: " + Double.toString(amount)));
        saved = saved + amount;
        for (Goal goal: goals) {
            if (amount <= 0) {
                break;
            } else {
                double targetAmount = goal.getGoalAmount();
                if (targetAmount > amount) {
                    break;
                } else {
                    goal.removeMoney(targetAmount);
                    amount = amount - targetAmount;
                    removeGoal(goal);
                }
            }
        }
    }

    // EFFECTS: returns the list of goals not achieved
    public List<Goal> goalNotFinished() {
        List<Goal> notFinished = new ArrayList<>();
        for (Goal goal: goals) {
            if (goal.getStatus() == false) {
                notFinished.add(goal);
            }
        }
        return notFinished; //stub
    }

    // EFFECTS: returns the number of goals not finished
    public int numOfGoalsNotFinished() {
        int total = 0;
        for (Goal goal: goals) {
            if (goal.getStatus() == false) {
                total++;
            }
        }
        return total; //stub
    }

    // REQUIRES: amount > 0
    // MODIFIES: goals
    // EFFECTS: set the goal and add it to the ArrayList
    public void setGoal(String name, double amount) {
        Goal goal = new Goal(name, amount);
        goals.add(goal);
        EventLog.getInstance().logEvent(
                new Event("Set a goal: " + name + " amount: " + Double.toString(amount)));
    }

    // MODIFIES: this
    // EFFECTS: remove a goal which is achieved
    public void removeGoal(Goal goal) {
        goals.remove(goal);
        EventLog.getInstance().logEvent(new Event("Achieved a goal: " + goal.getGoalName()));
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("goals", goalsToJson());
        return json;
    }

    // EFFECTS: returns goals in this account as a JSON array
    private JSONArray goalsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Goal goal : goals) {
            jsonArray.put(goal.toJson());
        }

        return jsonArray;
    }


}
