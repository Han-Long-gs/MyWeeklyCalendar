package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CalendarManageTest {

    private CalendarManage calendarManage;

    @BeforeEach
    void runBefore() {
        calendarManage = new CalendarManage();
        CalendarManage calendarHan = new CalendarManage();
        CalendarManage calendarWang = new CalendarManage();
        Event eventAH = new Event("Han", "210", 1, 1, 1300, 1500);
        Event eventBH = new Event("Han", "210", 1, 2, 800, 900);
        Event eventCH = new Event("Han", "210", 2, 3, 800, 900);
        Event eventDH = new Event("Han", "210", 2, 4, 800, 900);
        Event eventAW = new Event("Wang", "210", 1, 1, 1250, 1320);
        Event eventBW = new Event("Wang", "210", 1, 2, 800, 900);
        Event eventCW = new Event("Wang", "210", 2, 3, 1300, 1500);
        Event eventDW = new Event("Wang", "210", 2, 5, 800, 900);
        calendarManage.addEvent(eventAH);
        calendarManage.addEvent(eventBH);
        calendarManage.addEvent(eventCH);
        calendarManage.addEvent(eventDH);
        calendarManage.addEvent(eventAW);
        calendarManage.addEvent(eventBW);
        calendarManage.addEvent(eventCW);
        calendarManage.addEvent(eventDW);
        calendarHan.addEvent(eventAH);
        calendarHan.addEvent(eventBH);
        calendarHan.addEvent(eventCH);
        calendarHan.addEvent(eventDH);
        calendarWang.addEvent(eventAW);
        calendarWang.addEvent(eventBW);
        calendarWang.addEvent(eventCW);
        calendarWang.addEvent(eventDW);
    }

    @Test
    void testConstructor() {
        assertNotNull(calendarManage);
        assertTrue(calendarManage.isEmpty());
        assertEquals(calendarManage.getCalendar().getClass(), ArrayList.class);
    }

    @Test
    void testShowFriendValidTime() {
        ArrayList<TimeInterval> testMerge= new ArrayList<>();
        testMerge.add(new TimeInterval(0, 800));
        testMerge.add(new TimeInterval(900, 1300));
        testMerge.add(new TimeInterval(1500, 2359));

        ArrayList<TimeInterval> actualMerge = calendarManage.showFriendValidTime("Wang", "Han", 2, 3);

        for (int i = 0; i < testMerge.size(); i++) {
            assertEquals(testMerge.get(i).getStartTime(), actualMerge.get(i).getStartTime());
            assertEquals(testMerge.get(i).getEndTime(), actualMerge.get(i).getEndTime());
        }

        ArrayList<TimeInterval> testMergeB = new ArrayList<>();
        testMergeB.add(new TimeInterval(0, 800));
        testMergeB.add(new TimeInterval(900, 2359));

        ArrayList<TimeInterval> actualMergeB = calendarManage.showFriendValidTime("Wang", "Han", 2, 4);

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

        ArrayList<TimeInterval> actualValid = calendarManage.findFriendValidTime(testMerge);


        for (int i = 0; i < testValid.size(); i++) {
            assertEquals(testValid.get(i).getStartTime(), actualValid.get(i).getStartTime());
            assertEquals(testValid.get(i).getEndTime(), actualValid.get(i).getEndTime());
        }

        ArrayList<TimeInterval> testValidB = new ArrayList<>();
        testValidB.add(new TimeInterval(0, 800));
        testValidB.add(new TimeInterval(900, 2359));

        ArrayList<TimeInterval> testMergeB= new ArrayList<>();
        testMergeB.add(new TimeInterval(800, 900));

        ArrayList<TimeInterval> actualValidB = calendarManage.findFriendValidTime(testMergeB);

        for (int i = 0; i < testValidB.size(); i++) {
            assertEquals(testValidB.get(i).getStartTime(), actualValidB.get(i).getStartTime());
            assertEquals(testValidB.get(i).getEndTime(), actualValidB.get(i).getEndTime());
        }
    }

    @Test
    void testFindName() {
        assertTrue(calendarManage.findName("Han"));
        assertTrue(calendarManage.findName("han"));
        assertFalse(calendarManage.findName("H an"));
    }

    @Test
    void testFindEvent() {
        Event eventAH = new Event("Han", "210", 1, 1, 1300, 1500);
        Event test = calendarManage.findEvent("Han", "210", 1, 1, 1300, 1500);
        assertEquals(eventAH.getPersonName(), test.getPersonName());
        assertEquals(eventAH.getEventName(), test.getEventName());
        assertEquals(eventAH.getWeekNum(), test.getWeekNum());
        assertEquals(eventAH.getWeekDay(), test.getWeekDay());
        assertEquals(eventAH.getStartTime(), test.getStartTime());
        assertEquals(eventAH.getEndTime(), test.getEndTime());
        assertNull(calendarManage.findEvent("Mi", "210", 1, 1, 1300, 1500));
    }

}
