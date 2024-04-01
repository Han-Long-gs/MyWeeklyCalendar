package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

// REFERENCE: Alarm System
public class EventTest {
    private Event e;
    private Date d;

    @BeforeEach
    public void runBefore() {
        e = new Event("Calendar Added: han, study, 5, 2, 1300, 1500");   // (1)
        d = Calendar.getInstance().getTime();   // (2)
    }

    @Test
    public void testEvent() {
        assertEquals("Calendar Added: han, study, 5, 2, 1300, 1500", e.getDescription());
        assertEquals(d.getYear(), e.getDate().getYear());
        assertEquals(d.getMonth(), e.getDate().getMonth());
        assertEquals(d.getDay(), e.getDate().getDay());
        assertEquals(d.getHours(), e.getDate().getHours());
        assertEquals(d.getMinutes(), e.getDate().getMinutes());
        assertEquals(d.getSeconds(), e.getDate().getSeconds());
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "Calendar Added: han, study, 5, 2, 1300, 1500", e.toString());
    }
}
