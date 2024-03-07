package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CalendarTest {
    private Calendar calendar;
    private Event eventAH;
    private Event eventBH;
    private Event eventCH;
    private Event eventDH;
    private Event eventAW;
    private Event eventBW;
    private Event eventCW;
    private Event eventDW;

    @BeforeEach
    void runBefore() {
        calendar = new Calendar("Han");
        eventAH = new Event("Han", "210", 1, 1, 1300, 1500);
        eventBH = new Event("Han", "210", 1, 2, 800, 900);
        eventCH = new Event("Han", "210", 2, 3, 800, 900);
        eventDH = new Event("Han", "210", 2, 4, 800, 900);
        eventAW = new Event("Wang", "210", 1, 1, 1250, 1320);
        eventBW = new Event("Wang", "210", 1, 2, 800, 900);
        eventCW = new Event("Wang", "210", 2, 3, 1300, 1500);
        eventDW = new Event("Wang", "210", 2, 5, 800, 900);
        calendar.addEvent(eventAH);
        calendar.addEvent(eventBH);
        calendar.addEvent(eventCH);
        calendar.addEvent(eventDH);
        calendar.addEvent(eventAW);
        calendar.addEvent(eventBW);
        calendar.addEvent(eventCW);
        calendar.addEvent(eventDW);
    }

    @Test
    void testConstructor() {
        Calendar calendarTest = new Calendar("test");
        assertEquals("test", calendarTest.getCalendarName());
        assertTrue(calendarTest.getEvents().isEmpty());
    }

    @Test
    void testAddEvent() {
        Event test = new Event("mi", "210", 2, 5, 800, 900);
        calendar.addEvent(test);
        assertTrue(calendar.getEvents().contains(test));
    }

    @Test
    void testShowFriendValidTime() {
        ArrayList<TimeInterval> testMerge= new ArrayList<>();
        testMerge.add(new TimeInterval(0, 800));
        testMerge.add(new TimeInterval(900, 1300));
        testMerge.add(new TimeInterval(1500, 2359));

        ArrayList<TimeInterval> actualMerge = calendar.showFriendValidTime("Wang", "Han", 2, 3);

        for (int i = 0; i < testMerge.size(); i++) {
            assertEquals(testMerge.get(i).getStartTime(), actualMerge.get(i).getStartTime());
            assertEquals(testMerge.get(i).getEndTime(), actualMerge.get(i).getEndTime());
        }

        ArrayList<TimeInterval> testMergeB = new ArrayList<>();
        testMergeB.add(new TimeInterval(0, 800));
        testMergeB.add(new TimeInterval(900, 2359));

        ArrayList<TimeInterval> actualMergeB = calendar.showFriendValidTime("Wang", "Han", 2, 4);

        for (int i = 0; i < testMergeB.size(); i++) {
            assertEquals(testMergeB.get(i).getStartTime(), actualMergeB.get(i).getStartTime());
            assertEquals(testMergeB.get(i).getEndTime(), actualMergeB.get(i).getEndTime());
        }
    }

    // NOTE: assertEquals can not directly compare two lists or two objects

    @Test
    void testFindFriendValidTime() {
        ArrayList<TimeInterval> testValid= new ArrayList<>();
        testValid.add(new TimeInterval(0, 800));
        testValid.add(new TimeInterval(900, 1300));
        testValid.add(new TimeInterval(1500, 2359));

        ArrayList<TimeInterval> testMerge= new ArrayList<>();
        testMerge.add(new TimeInterval(800, 900));
        testMerge.add(new TimeInterval(1300, 1500));

        ArrayList<TimeInterval> actualValid = calendar.findFriendValidTime(testMerge);


        for (int i = 0; i < testValid.size(); i++) {
            assertEquals(testValid.get(i).getStartTime(), actualValid.get(i).getStartTime());
            assertEquals(testValid.get(i).getEndTime(), actualValid.get(i).getEndTime());
        }

        ArrayList<TimeInterval> testValidB = new ArrayList<>();
        testValidB.add(new TimeInterval(0, 800));
        testValidB.add(new TimeInterval(900, 2359));

        ArrayList<TimeInterval> testMergeB= new ArrayList<>();
        testMergeB.add(new TimeInterval(800, 900));

        ArrayList<TimeInterval> actualValidB = calendar.findFriendValidTime(testMergeB);

        for (int i = 0; i < testValidB.size(); i++) {
            assertEquals(testValidB.get(i).getStartTime(), actualValidB.get(i).getStartTime());
            assertEquals(testValidB.get(i).getEndTime(), actualValidB.get(i).getEndTime());
        }
    }

    @Test
    void testFindName() {
        assertTrue(calendar.findName("Han"));
        assertTrue(calendar.findName("han"));
        assertFalse(calendar.findName("mi"));
        assertFalse(calendar.findName("H an"));
    }

    @Test
    void testGetMyEvents() {
        List<Event> testEvents = calendar.getMyEvents("Han");
        assertTrue(testEvents.contains(eventAH));
        assertTrue(testEvents.contains(eventBH));
        assertTrue(testEvents.contains(eventCH));
        assertTrue(testEvents.contains(eventDH));
        assertEquals(4, testEvents.size());

        List<Event> testEventsB = calendar.getMyEvents("wang");
        assertTrue(testEventsB.contains(eventAW));
        assertTrue(testEventsB.contains(eventBW));
        assertTrue(testEventsB.contains(eventCW));
        assertTrue(testEventsB.contains(eventDW));
        assertEquals(4, testEvents.size());
    }

    @Test
    void testToJson() {
        JSONObject actual = calendar.toJson();
        JSONObject expected = new JSONObject();
        expected.put("calendarName", "Han");
        expected.put("eventList", calendar.eventsToJson());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void testEventsToJson() {
        Calendar test = new Calendar("test");
        List<Event> testEvents = new ArrayList<>();
        testEvents.add(eventAH);
        testEvents.add(eventCH);
        test.setEvents(testEvents);
        JSONArray actualArray = test.eventsToJson();
        JSONObject expected = new JSONObject("{\"calendarName\":\"Han\","
                + "\"eventList\":[{\"personName\":\"Han\","
                + "\"weekNum\":1,\"weekDay\":1,\"eventName\":\"210\",\"startTime\":1300,\"endTime\":1500},"
                + "{\"personName\":\"Han\","
                + "\"weekNum\":2,\"weekDay\":3,\"eventName\":\"210\",\"startTime\":800,\"endTime\":900}]}");
        JSONArray expectedArray = expected.getJSONArray("eventList");
        assertEquals(expectedArray.length(), actualArray.length());
        for (int i = 0; i < expectedArray.length(); i++) {
            assertEquals(expectedArray.getJSONObject(i).toString(), actualArray.getJSONObject(i).toString());
        }
    }
    
}
