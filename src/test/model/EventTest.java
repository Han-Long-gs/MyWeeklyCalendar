package model;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class EventTest {

    private Event han;

    @BeforeEach
    void runBefore() {
        han = new Event("Han", "Study", 2, 1, 1300, 1400);
    }

    @Test
    void testConstructor() {
        assertEquals(han.getPersonName(), "Han");
        assertEquals(han.getEventName(), "Study");
        assertEquals(han.getWeekNum(), 2);
        assertEquals(han.getWeekDay(), 1);
        assertEquals(han.getStartTime(),1300);
        assertEquals(han.getEndTime(), 1400);
    }

    @Test
    void testCheckPersonName() {
        assertTrue(han.checkPersonName("Han"));
        assertTrue(han.checkPersonName("Han Long"));
        assertFalse(han.checkPersonName("han l*n"));
    }

    @Test
    void testCheckWeekNum() {
        assertTrue(han.checkWeekNum(han.getWeekNum()));
        assertFalse(han.checkWeekNum(0));
        assertFalse(han.checkWeekNum(-2));
    }

    @Test
    void testCheckWeekDay() {
        assertTrue(han.checkWeekDay(han.getWeekDay()));
        assertFalse(han.checkWeekDay(0));
        assertFalse(han.checkWeekDay(8));
        assertFalse(han.checkWeekDay(-1));
    }

    @Test
    void testCheckTime() {
        assertTrue(han.checkTime(han.getStartTime(), han.getEndTime()));
        assertFalse(han.checkTime(1180, 1250));
        assertFalse(han.checkTime(1259, 1861));
        assertFalse(han.checkTime(-1100, 1230));
        assertFalse(han.checkTime(1100, -1200));
        assertFalse(han.checkTime(1259, 1101));
        assertFalse(han.checkTime(1230, 1230));
    }

    @Test
    void testToJson() {
        JSONObject actual = han.toJson();
        JSONObject expected = new JSONObject();
        expected.put("personName", "Han");
        expected.put("eventName", "Study");
        expected.put("weekNum", 2);
        expected.put("weekDay", 1);
        expected.put("startTime", 1300);
        expected.put("endTime", 1400);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void testEquals() {
        Event eventTest1 = null;
        Event eventTest = new Event("Han", "Study", 2, 1, 1300, 1400);
        Event eventTest2 = new Event("wang", "work", 3, 7, 1500, 1600);
        assertFalse(han.equals(eventTest1));
        assertTrue(eventTest.equals(han));
        assertFalse(eventTest2.equals(han));
    }

    @Test
    void testIsNumeric() {
        String test = null;
        assertTrue(Event.isNumeric("20"));
        assertFalse(Event.isNumeric("2T"));
        assertFalse(Event.isNumeric(test));
    }

}