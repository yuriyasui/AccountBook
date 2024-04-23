package persistence;

import model.Account;
import model.Goal;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JasonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Account account = new Account();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            Account account = new Account();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyAccount.json");
            writer.open();
            writer.write(account);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyAccount.json");
            account = reader.read();
            assertEquals(0, account.numOfGoalsNotFinished());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralAccount() {
        try {
            Account account = new Account();
            account.setGoal("shoes", 100);
            account.setGoal("concert ticket", 300);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralAccount.json");
            writer.open();
            writer.write(account);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralAccount.json");
            account = reader.read();
            List<Goal> goals = account.goalNotFinished();
            assertEquals(2, goals.size());
            checkAccount("shoes", 100, goals.get(0));
            checkAccount("concert ticket", 300, goals.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
