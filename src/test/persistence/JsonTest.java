package persistence;

import model.MyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;

// super class for JsonReaderTest and JsonWriterTest to simplify the process of checking the events
// CREDIT: JsonSerializationDemo
public class JsonTest {
    protected void checkEvent(String personName, String eventName, int weekNum,
                              int weekDay, int startTime, int endTime, MyEvent myEvent) {
        assertEquals(personName, myEvent.getPersonName());
        assertEquals(eventName, myEvent.getEventName());
        assertEquals(weekNum, myEvent.getWeekNum());
        assertEquals(weekDay, myEvent.getWeekDay());
        assertEquals(startTime, myEvent.getStartTime());
        assertEquals(endTime, myEvent.getEndTime());
    }
}
