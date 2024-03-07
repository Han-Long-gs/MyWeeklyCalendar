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
    private List<Event> events;

    public Calendar(String name) {
        this.calendarName = name;
        this.events = new ArrayList<>();
    }

    // MODIFIES: this.events
    // EFFECTS: add the given event to the calendar
    public void addEvent(Event event) {
        events.add(event);
    }

    // EFFECTS: find friend's valid time intervals base on my schedule (i.e. shared free time intervals) then return it
    public ArrayList<TimeInterval> showFriendValidTime(String friendName, String myName, int weekNum, int weekDay) {
        // create a list of that includes my friend's events
        List<Event> mergedSchedule = events.stream()
                .filter(event -> (event.getPersonName().equalsIgnoreCase(friendName))
                        &&
                        event.getWeekNum() == weekNum
                        &&
                        event.getWeekDay() == weekDay)
                .collect(Collectors.toList());

        // create a list of all my events
        List<Event> mySchedule = events.stream()
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
        for (Event event : mergedSchedule) {
            TimeInterval timeInterval;
            timeInterval = new TimeInterval(event.getStartTime(), event.getEndTime());
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
        for (Event event : events) {
            if (event.getPersonName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: return all the events under the given name
    public List<Event> getMyEvents(String name) {
        List<Event> myEvents = new ArrayList<>();
        for (Event e : events) {
            if (e.getPersonName().equalsIgnoreCase(name)) {
                myEvents.add(e);
            }
        }
        return myEvents;
    }

    // EFFECTS: save calendar as json Object and return it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("calendarName", calendarName);
        json.put("eventList", eventsToJson());
        return json;
    }

    // EFFECTS: save events to json Array
    public JSONArray eventsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Event e : events) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }

    // SIMPLE GETTERS AND SETTERS
    public List<Event> getEvents() {
        return this.events;
    }

    public String getCalendarName() {
        return this.calendarName;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
