package ui;

import exceptions.IllegalInputException;
import exceptions.NoEventsException;
import model.Calendar;
import model.MyEvent;
import persistence.JsonReader;
import persistence.JsonWriter;
import model.TimeInterval;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// represents the interactive behaviors with users of the calendar app
public class CalendarApp {
    private Scanner input;
    private Calendar calendar;
    private List<MyEvent> myEvents;
    private static final String JSON_STORE = "./data/calendar.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    // start the app, initialize the calendar and the reader and the writer, throws exception if file not found
    public CalendarApp() throws FileNotFoundException {
        calendar = new Calendar("han");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);

        runCalendar();
    }

    // EFFECTS: run the App
    public void runCalendar() {
        boolean keepGoing = true;
        String cmd;
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        while (keepGoing) {
            displayMenu();
            cmd = stringInput();
            cmd = cmd.toLowerCase();

            if (cmd.equals("q")) {
                keepGoing = false;
            } else {
                proceedCmd(cmd);
            }
        }
        System.out.println("See you next time!");
    }

    // EFFECTS: display main menu
    public void displayMenu() {
        System.out.println("select from: ");
        System.out.println("l -> load calendar from file");
        System.out.println("a -> add new event to calendar");
        System.out.println("d -> delete an existing event");
        System.out.println("p -> print all my events");
        System.out.println("c -> check my friend's valid time");
        System.out.println("s -> save calendar to file");
        System.out.println("q -> quit");
    }

    // EFFECTS: proceed the command for main menu
    public void proceedCmd(String cmd) {
        switch (cmd) {
            case "l": loadCalendar();
                break;
            case "a": helperForAddEvent();
                break;
            case "d": helperForDeleteEvent();
                break;
            case "p": helperForPrintEvents();
                break;
            case "c": helperForCheckFriendTime();
                break;
            case "s": saveCalendar();
                break;
            default: System.out.println("Selection not valid");
        }
    }

    // EFFECTS: helper method for case "a"; throws IllegalInputException if the user's input does not follow the
    //          required format
    public void helperForAddEvent() {
        try {
            createEvent();
            System.out.println("Event successfully added!");
        } catch (IllegalInputException e) {
            System.out.println(e.getMessage());
        }
    }

    // EFFECTS: helper method for case "d"; throws IllegalInputException if the user's input does not follow the
    //          required format
    public void helperForDeleteEvent() {
        try {
            deleteEvent();
        } catch (IllegalInputException e) {
            System.out.println(e.getMessage());
        }
    }

    // EFFECTS: helper method for case "p"; throws IllegalInputException if the user's input does not follow the
    //          required format; throws NoEventsException if there are no events in the list
    public void helperForPrintEvents() {
        try {
            printEvents();
        } catch (NoEventsException | IllegalInputException e) {
            System.out.println(e.getMessage());
        }
    }

    // EFFECTS: helper method for case "c"; throws IllegalInputException if the user's input does not follow the
    //          required format
    public void helperForCheckFriendTime() {
        try {
            checkFriendTime();
        } catch (IllegalInputException e) {
            System.out.println(e.getMessage());
        }
    }

    // EFFECTS: check indicated friend's valid time
    private void checkFriendTime() throws IllegalInputException {
        System.out.println("Type your name");
        String name = stringInput();
        if (!MyEvent.checkPersonName(name)) {
            throw new IllegalInputException();
        }
        System.out.println("Type your friend's name");
        String friendName = stringInput();
        if (!MyEvent.checkPersonName(friendName)) {
            throw new IllegalInputException();
        }
        if (!calendar.findName(friendName)) {
            System.out.println("No calendar found under this name. Redirect to main menu...");
            displayMenu();
        }
        System.out.println("Type the week number that you want to search");
        int weekNum = intInput();
        System.out.println("Type the week day that you want to search");
        int weekDay = intInput();
        printFriendValidTime(calendar.showFriendValidTime(friendName, name, weekNum, weekDay));
    }

    // EFFECTS: print all the events of indicated person
    private void printEvents() throws NoEventsException, IllegalInputException {
        System.out.println("Please type your name");
        String name = stringInput();
        if (!MyEvent.checkPersonName(name)) {
            throw new IllegalInputException();
        }
        List<MyEvent> myMyEvents = calendar.getMyEvents(name);

        if (myMyEvents.isEmpty()) {
            throw new NoEventsException();
        }
        System.out.println("-------------------------------------------------------------------------");
        System.out.println(String.format("%-10s %-10s %-10s %-10s %-10s %-10s", "personName",
                "eventName", "weekNum", "weekDay", "startTime", "endTime"));
        System.out.println("-------------------------------------------------------------------------");
        for (MyEvent e : myMyEvents) {
            System.out.println(String.format("%-10s %-10s %-10d %-10d %-10s %-10s", e.getPersonName(),
                    e.getEventName(), e.getWeekNum(), e.getWeekDay(), intToTime(e.getStartTime()),
                    intToTime(e.getEndTime())));
        }
        System.out.println("-------------------------------------------------------------------------");
    }


    // EFFECTS: write the calendar to the file
    private void saveCalendar() {
        try {
            jsonWriter.open();
            jsonWriter.write(calendar);
            jsonWriter.close();
            System.out.println("Saved Calendar to " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: read the file and load the previously saved calendar
    private void loadCalendar() {
        try {
            calendar = jsonReader.read();
            myEvents = calendar.getEvents();
            System.out.println("Loaded Calendar from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: calendar
    // EFFECTS: let users input their name, event, event start and end time to create multiple new events
    public void createEvent() throws IllegalInputException {
        input = new Scanner(System.in);

        System.out.println("please input your name");
        String name = stringInput();
        System.out.println("please input your event name");
        String eventName = stringInput();
        System.out.println("please input your week number");
        int weekNum = intInput();
        System.out.println("please input your week day");
        int weekDay = intInput();
        System.out.println("please input your event start time");
        int startTime = intInput();
        System.out.println("please input your event end time");
        int endTime = intInput();

        MyEvent myEvent = new MyEvent(name, eventName, weekNum, weekDay, startTime, endTime);
        if (!MyEvent.checkPersonName(name) || !MyEvent.checkWeekNum(weekNum) || !MyEvent.checkWeekDay(weekDay)
                || !MyEvent.checkTime(startTime, endTime)) {
            throw new IllegalInputException();
        }
        calendar.addEvent(myEvent);
    }


    // MODIFIES: calendar
    // EFFECTS: delete an event from the saved calendar
    public void deleteEvent() throws IllegalInputException {
        input = new Scanner(System.in);

        System.out.println("please input your name");
        String name = stringInput();
        System.out.println("please input your event name");
        String eventName = stringInput();
        System.out.println("please input your week number");
        int weekNum = intInput();
        System.out.println("please input your week day");
        int weekDay = intInput();
        System.out.println("please input your event start time");
        int startTime = intInput();
        System.out.println("please input your event end time");
        int endTime = intInput();
        if (!MyEvent.checkPersonName(name) || !MyEvent.checkWeekNum(weekNum) || !MyEvent.checkWeekDay(weekDay)
                || !MyEvent.checkTime(startTime, endTime)) {
            throw new IllegalInputException();
        }
        if (removeEvent(name, eventName, weekNum, weekDay, startTime, endTime)) {
            System.out.println("Successfully remove the event");
        } else {
            System.out.println("Event not found");
        }
    }

    // EFFECTS: print friend's valid time
    public void printFriendValidTime(ArrayList<TimeInterval> validTimeIntervals) {
        for (TimeInterval timeInterval : validTimeIntervals) {
            System.out.println("Friend's available time of today: " + intToTime(timeInterval.getStartTime())
                    + "-"
                    + intToTime(timeInterval.getEndTime()));
        }
    }

    // EFFECTS: helper method of deleteEvent. If the indicated event is found, remove the event and return true;
    //          else return false
    private boolean removeEvent(String name, String eventName, int weekNum, int weekDay, int startTime, int endTime) {
        for (int i = 0; i < myEvents.size(); i++) {
            if (myEvents.get(i).getPersonName().equals(name) && myEvents.get(i).getEventName().equals(eventName)
                    && myEvents.get(i).getWeekNum() == weekNum && myEvents.get(i).getWeekDay() == weekDay
                    && myEvents.get(i).getStartTime() == startTime && myEvents.get(i).getEndTime() == endTime) {
                myEvents.remove(myEvents.get(i));
                return true;
            }
        }
        return false;
    }

    // EFFECTS: change the int to time format string
    private String intToTime(int time) {
        return (" " + time / 100 + ":" + (time % 100) / 10 + (time % 100) % 10);
    }

    // EFFECTS: let user input an int and return it. if the format is wrong catches the InputMismatchException and
    //          let user input again until get the correct input
    private int intInput() {
        int num;
        Scanner input = new Scanner(System.in);
        while (true) {
            try {
                num = input.nextInt();
                return num;
            } catch (InputMismatchException e) {
                System.out.println("Input format error, please re-enter");
                input = new Scanner(System.in);
            }
        }
    }

    // EFFECTS: let user input a String and return it. if the format is wrong catches the InputMismatchException and
    //          let user input again until get the correct input
    private String stringInput() {
        String string;
        Scanner input = new Scanner(System.in);
        while (true) {
            try {
                string = input.next();
                return string;
            } catch (InputMismatchException e) {
                System.out.println("Input format error, please re-enter");
                input = new Scanner(System.in);
            }
        }
    }

}


