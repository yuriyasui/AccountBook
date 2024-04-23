package persistence;

import model.Account;
import model.Goal;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Account account = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testWriterEmptyAccount.json");
        try {
            Account account = reader.read();
            assertEquals(0, account.numOfGoalsNotFinished());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralAccountBook() {
        JsonReader reader = new JsonReader("./data/testWriterGeneralAccount.json");
        try {
            Account account = reader.read();
            List<Goal> goals = account.goalNotFinished();
            assertEquals(2, goals.size());
            checkAccount("shoes", 100.0, goals.get(0));
            checkAccount("concert ticket", 300.0, goals.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
