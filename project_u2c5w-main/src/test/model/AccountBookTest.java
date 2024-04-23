package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountBookTest {

    private Account testAccount;

    @BeforeEach
    void runBefore() {
        testAccount = new Account();
    }

    @Test
    void testConstructor() {
        assertEquals(0, testAccount.getAmountSaved());
        assertEquals(0, testAccount.totalAmountNeedToSave());
    }

    @Test
    void testSetGoal() {
        testAccount.setGoal("Buy shoes", 100);
        assertEquals(100, testAccount.totalAmountNeedToSave());
        assertEquals(1, testAccount.numOfGoalsNotFinished());
        List<Goal> notFinishedList = testAccount.goalNotFinished();
        assertEquals("Buy shoes", notFinishedList.get(0).getGoalName());
    }

    @Test
    void testSetMultipleGoals() {
        testAccount.setGoal("Buy shoes", 100);
        testAccount.setGoal("Buy Concert Ticket", 300);
        testAccount.setGoal("Buy Airplane Ticket", 1000);
        assertEquals(1400, testAccount.totalAmountNeedToSave());
        assertEquals(3, testAccount.numOfGoalsNotFinished());
        List<Goal> notFinishedList = testAccount.goalNotFinished();
        assertEquals("Buy shoes", notFinishedList.get(0).getGoalName());
        assertEquals("Buy Concert Ticket", notFinishedList.get(1).getGoalName());
        assertEquals("Buy Airplane Ticket", notFinishedList.get(2).getGoalName());
    }

    @Test
    void testAddAmount() {
        testAccount.setGoal("Buy shoes", 100);
        testAccount.setGoal("Buy Concert Ticket", 300);
        testAccount.setGoal("Buy Airplane Ticket", 1000);
        testAccount.addMoney(50);
        assertEquals(1350, testAccount.totalAmountNeedToSave());
        assertEquals(3, testAccount.numOfGoalsNotFinished());
        List<Goal> notFinishedList = testAccount.goalNotFinished();
        assertEquals("Buy shoes", notFinishedList.get(0).getGoalName());
        assertEquals("Buy Concert Ticket", notFinishedList.get(1).getGoalName());
        assertEquals("Buy Airplane Ticket", notFinishedList.get(2).getGoalName());
        assertEquals(50, testAccount.getAmountSaved());
    }

    @Test
    void testAddJustAmount() {
        testAccount.setGoal("Buy shoes", 100);
        testAccount.setGoal("Buy Concert Ticket", 300);
        testAccount.setGoal("Buy Airplane Ticket", 1000);
        testAccount.addMoney(100);
        assertEquals(1300, testAccount.totalAmountNeedToSave());
        assertEquals(2, testAccount.numOfGoalsNotFinished());
        List<Goal> notFinishedList = testAccount.goalNotFinished();
        assertEquals("Buy Concert Ticket", notFinishedList.get(0).getGoalName());
        assertEquals("Buy Airplane Ticket", notFinishedList.get(1).getGoalName());
        assertEquals(100, testAccount.getAmountSaved());
    }

    @Test
    void testAddMoreAmount() {
        testAccount.setGoal("Buy shoes", 100);
        testAccount.setGoal("Buy Concert Ticket", 300);
        testAccount.setGoal("Buy Airplane Ticket", 1000);
        testAccount.addMoney(200);
        assertEquals(1200, testAccount.totalAmountNeedToSave());
        assertEquals(2, testAccount.numOfGoalsNotFinished());
        List<Goal> notFinishedList = testAccount.goalNotFinished();
        assertEquals("Buy Concert Ticket", notFinishedList.get(0).getGoalName());
        assertEquals("Buy Airplane Ticket", notFinishedList.get(1).getGoalName());
        assertEquals(200, testAccount.getAmountSaved());
    }

    @Test
    void testAddMultipleAmount() {
        testAccount.setGoal("Buy shoes", 100);
        testAccount.setGoal("Buy Concert Ticket", 300);
        testAccount.setGoal("Buy Airplane Ticket", 1000);
        testAccount.addMoney(200);
        testAccount.addMoney(500);
        assertEquals(700, testAccount.totalAmountNeedToSave());
        assertEquals(1, testAccount.numOfGoalsNotFinished());
        List<Goal> notFinishedList = testAccount.goalNotFinished();
        assertEquals("Buy Airplane Ticket", notFinishedList.get(0).getGoalName());
        assertEquals(700, testAccount.totalAmountNeedToSave());
    }
}
