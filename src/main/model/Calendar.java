package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// represents a calendar with user's name and a list of events
public class Calendar implements Writable {
    private String calendarName;
    private List<MyEvent> myEvents;

    public Calendar(String name) {
        this.calendarName = name;
        this.myEvents = new ArrayList<>();
    }

    // MODIFIES: this.events
    // EFFECTS: add the given event to the calendar
    public void addEvent(MyEvent myEvent) {
        myEvents.add(myEvent);
        EventLog.getInstance().logEvent(new Event("Added to Calendar: "
                + myEvent.getPersonName() + ", "
                + myEvent.getEventName() + ", "
                + myEvent.getWeekNum() + ", "
                + myEvent.getWeekDay() + ", "
                + myEvent.getStartTime() + ", "
                + myEvent.getEndTime()));
    }

    // MODIFIES: this.events
    // EFFECTS: remove the given event from the calendar
    public void removeEvent(MyEvent myEvent) {
        myEvents.remove(myEvent);
        EventLog.getInstance().logEvent(new Event("Removed from Calendar: "
                + myEvent.getPersonName() + ", "
                + myEvent.getEventName() + ", "
                + myEvent.getWeekNum() + ", "
                + myEvent.getWeekDay() + ", "
                + myEvent.getStartTime() + ", "
                + myEvent.getEndTime()));
    }

    // EFFECTS: find friend's valid time intervals base on my schedule (i.e. shared free time intervals) then return it
    public ArrayList<TimeInterval> showFriendValidTime(String friendName, String myName, int weekNum, int weekDay) {
        // create a list of that includes my friend's events
        List<MyEvent> mergedSchedule = myEvents.stream()
                .filter(event -> (event.getPersonName().equalsIgnoreCase(friendName))
                        &&
                        event.getWeekNum() == weekNum
                        &&
                        event.getWeekDay() == weekDay)
                .collect(Collectors.toList());

        // create a list of all my events
        List<MyEvent> mySchedule = myEvents.stream()
                .filter(event -> (event.getPersonName().equalsIgnoreCase(myName))
                        &&
                        event.getWeekNum() == weekNum
                        &&
                        event.getWeekDay() == weekDay)
                .collect(Collectors.toList());

        // merge above two lists together
        mergedSchedule.addAll(mySchedule);

        ArrayList<TimeInterval> mergedTime = new ArrayList<>();

        // extract the time interval information from every event
        for (MyEvent myEvent : mergedSchedule) {
            TimeInterval timeInterval;
            timeInterval = new TimeInterval(myEvent.getStartTime(), myEvent.getEndTime());
            mergedTime.add(timeInterval);
        }

        // call the helper method to return the friend's valid time
        return findFriendValidTime(mergedTime);
    }

    // EFFECTS: return the friend's valid time interval base on the merged calendar; function as a helper method to
    //          showFriendValidTime()
    public ArrayList<TimeInterval> findFriendValidTime(ArrayList<TimeInterval> mergedTime) {
        mergedTime.sort(Comparator.comparing(TimeInterval::getStartTime));
        int previousEnd = 0;
        ArrayList<TimeInterval> validTimeIntervals = new ArrayList<>();

        for (TimeInterval timeInterval : mergedTime) {
            if (timeInterval.getStartTime() > previousEnd) {
                validTimeIntervals.add(new TimeInterval(previousEnd, timeInterval.getStartTime()));
            }
            previousEnd = Math.max(previousEnd, timeInterval.getEndTime());
        }

        if (previousEnd < 2359) {
            validTimeIntervals.add(new TimeInterval(previousEnd, 2359));
        }
        return validTimeIntervals;
    }

    // EFFECTS: return true if there are events under the given name; else false
    public boolean findName(String name) {
        for (MyEvent myEvent : myEvents) {
            if (myEvent.getPersonName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: return all the events under the given name
    public List<MyEvent> getMyEvents(String name) {
        List<MyEvent> myMyEvents = new ArrayList<>();
        for (MyEvent e : this.myEvents) {
            if (e.getPersonName().equalsIgnoreCase(name)) {
                myMyEvents.add(e);
            }
        }
        return myMyEvents;
    }

    // CREDIT: JsonSerializationDemo
    // EFFECTS: save calendar as json Object and return it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("calendarName", calendarName);
        json.put("eventList", eventsToJson());
        return json;
    }

    // CREDIT: JsonSerializationDemo
    // EFFECTS: save events to json Array
    public JSONArray eventsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (MyEvent e : myEvents) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }

    // SIMPLE GETTERS AND SETTERS
    public List<MyEvent> getEvents() {
        return this.myEvents;
    }

    public String getCalendarName() {
        return this.calendarName;
    }

    public void setEvents(List<MyEvent> myEvents) {
        this.myEvents = myEvents;
    }
}
