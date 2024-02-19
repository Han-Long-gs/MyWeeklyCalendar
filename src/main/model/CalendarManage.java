package model;

import java.util.*;
import java.util.stream.Collectors;

// represent a list of events
public class CalendarManage {
    ArrayList<Event> calendar;

    public CalendarManage() {
        calendar = new ArrayList<>();

    }

    public void addEvent(Event event) {
        calendar.add(event);
    }

    public void removeEvent(Event event) {
        calendar.remove(event);
    }

    public ArrayList<Event> getCalendar() {
        return calendar;
    }

    public boolean isEmpty() {
        return calendar.isEmpty();
    }

    // EFFECTS: create a combined list of indicated friend and user's calendar; then return friend's valid time base
    //          on the merged calendar.
    public ArrayList<TimeInterval> showFriendValidTime(String friendName, String myName, int weekNum, int weekDay) {
        // create a list of that includes my friend's events
        List<Event> mergedSchedule = calendar.stream()
                .filter(event -> (event.getPersonName().equalsIgnoreCase(friendName))
                        &&
                        event.getWeekNum() == weekNum
                        &&
                        event.getWeekDay() == weekDay)
                .collect(Collectors.toList());

        // create a list of all my events
        List<Event> mySchedule = calendar.stream()
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

    // EFFECTS: return the friend's valid time base on the merged calendar; function as a helper method to
    //          showFriendValidTime
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

    public boolean findName(String name) {
        for (Event event : calendar) {
            if (event.getPersonName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    // REQUIRES: there aren't two events that share the exactly same info for six parameters
    // EFFECTS: find the event that matches user's input
    public Event findEvent(String name, String eventName, int weekNum, int weekDay, int startTime, int endTime) {
        for (Event event : calendar) {
            if (event.getPersonName().equalsIgnoreCase(name)
                    &&
                    event.getEventName().equalsIgnoreCase(eventName)
                    &&
                    weekNum == event.getWeekNum()
                    &&
                    weekDay == event.getWeekDay()
                    &&
                    startTime == event.getStartTime()
                    &&
                    endTime == event.getEndTime()) {
                return event;
            }
        }
        return null;
    }

}
