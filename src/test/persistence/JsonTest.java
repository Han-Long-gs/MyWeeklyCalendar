package persistence;

import model.Event;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkEvent(String personName, String eventName, int weekNum,
                              int weekDay, int startTime, int endTime, Event event) {
        assertEquals(personName, event.getPersonName());
        assertEquals(eventName, event.getEventName());
        assertEquals(weekNum, event.getWeekNum());
        assertEquals(weekDay, event.getWeekDay());
        assertEquals(startTime, event.getStartTime());
        assertEquals(endTime, event.getEndTime());
    }
}
