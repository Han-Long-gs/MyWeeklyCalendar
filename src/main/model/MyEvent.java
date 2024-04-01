package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

// represents an event
public class MyEvent implements Writable {
    private final String personName;
    private final String eventName;
    private final int weekNum;
    private final int weekDay;
    private final int startTime;
    private final int endTime;

    public MyEvent(String personName, String eventName, int weekNum, int weekDay, int startTime, int endTime) {
        this.personName = personName;
        this.eventName = eventName;
        this.weekNum = weekNum;
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // EFFECTS: return true if the person's name only contains letters and space; else false
    public static boolean checkPersonName(String name) {
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    // EFFECTS: return true if the week number is greater than 0; else false
    public static boolean checkWeekNum(int weekNum) {
        return weekNum > 0;
    }

    // EFFECTS: return true if the week day is between 1 and 7; else false
    public static boolean checkWeekDay(int weekDay) {
        return 0 < weekDay && weekDay < 8;
    }

    // EFFECTS: return true if the time is legal (for example: 16:30 should be written as 1630, therefore, the first two
    //          digits should always be >= 0 && < 24, the last two digits should always be >= 0 && < 60; the start time
    //          should be less than the end time)
    public static boolean checkTime(int startTime, int endTime) {
        return (startTime / 100 >= 0 && startTime / 100 < 24 && startTime % 100 >= 0 && startTime % 100 < 60)
                &&
                (endTime / 100 >= 0 && endTime / 100 < 24 && endTime % 100 >= 0 && endTime % 100 < 60)
                &&
                (startTime < endTime);
    }

    // CREDIT: JsonSerializationDemo
    // EFFECTS: return a JSON object that contains the information of the event
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("personName", personName);
        json.put("eventName", eventName);
        json.put("weekNum", weekNum);
        json.put("weekDay", weekDay);
        json.put("startTime", startTime);
        json.put("endTime", endTime);
        return json;
    }

    // SIMPLE GETTERS
    public String getPersonName() {
        return personName;
    }

    public String getEventName() {
        return eventName;
    }

    public int getWeekNum() {
        return weekNum;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyEvent myEvent = (MyEvent) o;
        return weekNum == myEvent.weekNum && weekDay == myEvent.weekDay
                && startTime == myEvent.startTime
                && endTime == myEvent.endTime
                && Objects.equals(personName, myEvent.personName) && Objects.equals(eventName, myEvent.eventName);
    }

    // EFFECTS: check if the input string is a number
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return strNum.matches("\\d+");
    }
}

