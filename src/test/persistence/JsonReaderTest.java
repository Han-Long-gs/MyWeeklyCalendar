package persistence;

import model.MyEvent;
import model.Calendar;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// a test class for JsonReader class
// CREDIT: JsonSerializationDemo
public class JsonReaderTest extends JsonTest {
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Calendar calendar = reader.read();
            fail("IOException expected");
        } catch (IOException e) {

        }
    }

    @Test
    void testReaderEmptyCalendar() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyCalendar.json");
        try {
            Calendar calendar = reader.read();
            assertEquals("han", calendar.getCalendarName());
            assertEquals(0, calendar.getEvents().size());
        } catch (IOException e) {
            fail("catched IOException when shouldn't");
        }
    }

    @Test
    void testReaderGeneralCalendar() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralCalendar.json");
        try {
            Calendar calendar = reader.read();
            List<MyEvent> myEvents = calendar.getEvents();
            assertEquals("han", calendar.getCalendarName());
            assertEquals(4, myEvents.size());
            checkEvent("han", "study",
                    5, 2, 1200, 1400, myEvents.get(0));
            checkEvent("wang", "woofwoof",
                    5, 2, 1230, 1300, myEvents.get(1));
            checkEvent("mi", "meow",
                    3, 1, 1200, 1201, myEvents.get(2));
            checkEvent("lele", "meowmeow",
                    5, 2, 1300, 1500, myEvents.get(3));

        } catch (IOException e) {
            fail("catched IOException when shouldn't");
        }
    }
}
