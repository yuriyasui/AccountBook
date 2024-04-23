package persistence;

import model.Goal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkAccount(String name, double amount, Goal goal) {
        assertEquals(name, goal.getGoalName());
        assertEquals(amount, goal.getGoalAmount());
    }
}
