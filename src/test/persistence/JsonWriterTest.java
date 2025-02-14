package persistence;

import model.MyEvent;
import model.Calendar;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// a test class for JsonWriter class
public class JsonWriterTest extends JsonTest {
    @Test
    void testWriterInvalidFile() {
        try {
            Calendar calendar = new Calendar("han");
            JsonWriter writer = new JsonWriter("./data/no\0SuchFile.json");
            writer.open();
            fail("IOException excepted");
        } catch (IOException e) {

        }
    }

    @Test
    void testWriterEmptyCalendar() {
        try {
            Calendar calendar = new Calendar("han");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyCalendar.json");
            writer.open();
            writer.write(calendar);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyCalendar.json");
            calendar = reader.read();
            assertEquals("han", calendar.getCalendarName());
            assertEquals(0, calendar.getEvents().size());
        } catch (IOException e) {
            fail("catched IOException when shouldn't");
        }
    }

    @Test
    void testWriterGeneralCalendar() {
        try {
            Calendar calendar = new Calendar("han");
            MyEvent myEventA = new MyEvent("han", "study",
                    5, 3, 300, 600);
            MyEvent myEventB = new MyEvent("han", "daydreaming",
                    3, 5, 1200, 1300);
            calendar.addEvent(myEventA);
            calendar.addEvent(myEventB);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralCalendar.json");
            writer.open();
            writer.write(calendar);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralCalendar.json");
            assertEquals("han", calendar.getCalendarName());
            checkEvent("han", "study",
                    5, 3, 300, 600, calendar.getEvents().get(0));
            checkEvent("han", "daydreaming",
                    3, 5, 1200, 1300, calendar.getEvents().get(1));
        } catch (IOException e) {
            fail("catched IOException when shouldn't");
        }
    }
}
