package model;
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

}