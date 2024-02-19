package ui;

import model.CalendarManage;
import model.Event;
import model.TimeInterval;

import java.util.ArrayList;
import java.util.Scanner;

public class CalendarApp {
    private Scanner input;

    private final CalendarManage calendarManage;
    private Event event;

    // start the app, initialize the calendarManage with some info saved inside for the check calendar method
    public CalendarApp() {
        calendarManage = new CalendarManage();
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
        runApp();
    }

    // EFFECTS: run the App
    public void runApp() {
        String cmd;
        input = new Scanner(System.in);
        input.useDelimiter("\n");


        displayMenu();

        while (true) {
            cmd = input.next();
            cmd = cmd.toLowerCase();

            if (cmd.equals("q")) {
                System.out.println("\nSee you next time!");
                System.exit(0);
            } else {
                proceedCmd(cmd);
            }
        }
    }

    // EFFECTS: display main menu
    public void displayMenu() {
        System.out.println("n -> create a new week calendar");
        System.out.println("c -> check my exist calendar");
        System.out.println("m -> modify my exist calendar");
        System.out.println("q -> quit");
    }

    // EFFECTS: proceed the command for main menu
    public void proceedCmd(String cmd) {
        switch (cmd) {
            case "n":
                doCreateCalendar();
                runApp();
                break;
            case "c":
                doCheckCalendar();
                runApp();
                break;
            case "m":
                doModifyCalendar();
                runApp();
                break;
            default:
                System.out.println("Selection not valid");
                runApp();
        }
    }

    // REQUIRES: for name and eventName input, user must input with only letters and space;
    //           for week num input, user must input an integer > 0;
    //           for week day input, user must input an integer > 0 && < 8;
    //           for time input, user must input integers. for example: 13:20 -> 1320
    // MODIFIES: calendarManage
    // EFFECTS: let users input their name, event, event start and end time to create multiple new events
    @SuppressWarnings("checkstyle:MethodLength")
    public void doCreateCalendar() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        while (true) {
            System.out.println("please input your name");
            String name = input.next();
            printNameError(name);
            System.out.println("please input your event name");
            String eventName = input.next();
            System.out.println("please input your week number");
            int weekNum = input.nextInt();
            printWeekNumError(weekNum);
            System.out.println("please input your week day");
            int weekDay = input.nextInt();
            printWeekDayError(weekDay);
            System.out.println("please input your event start time");
            int startTime = input.nextInt();
            System.out.println("please input your event end time");
            int endTime = input.nextInt();
            printTimeError(startTime, endTime);

            Event event = new Event(name, eventName, weekNum, weekDay, startTime, endTime);
            calendarManage.addEvent(event);
            System.out.println("Event created:");
            printEvent(event);
            System.out.println("c -> Create another event");
            System.out.println("press any key to go back to the main menu");
            if (!input.next().equalsIgnoreCase("c")) {
                break;
            }
        }
        // NOTE: user should be able to create multiple events after create the calendar.
        //       modify to meet the requirements later.
    }

    // REQUIRES: for name and eventName input, user must input with only letters and space;
    //           for week num input, user must input an integer > 0;
    //           for week day input, user must input an integer > 0 && < 8;
    //           for time input, user must input integers. for example: 13:20 -> 1320
    // EFFECTS: display the calendar for user; show the indicated friend's valid time for user
    @SuppressWarnings("checkstyle:MethodLength")
    public void doCheckCalendar() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        System.out.println("please input your name");
        String name = input.next();
        printNameError(name);
        if (!calendarManage.findName(name)) {
            System.out.println("No calendar found under this name. Redirect to main menu");
            runApp();
        }

        System.out.println("c -> check your schedule");
        System.out.println("s -> show your friend's valid time of a day base on your schedule");

        String choice = input.next();

        if (choice.equalsIgnoreCase("c")) {
            showAllMyEvents(name);
        } else if (choice.equalsIgnoreCase("s")) {
            System.out.println("Type your friend's name");
            String friendName = input.next();
            if (!calendarManage.findName(friendName)) {
                System.out.println("No calendar found under this name. Redirect to main menu");
                runApp();
            }
            System.out.println("Type the week number that you want to search");
            int weekNum = input.nextInt();
            printWeekNumError(weekNum);
            System.out.println("Type the week day that you want to search");
            int weekDay = input.nextInt();
            printWeekDayError(weekDay);
            //System.out.println(calendarManage.showFriendValidTime(friendName, name, weekNum, weekDay));
            printFriendValidTime(calendarManage.showFriendValidTime(friendName, name, weekNum, weekDay));
        } else {
            System.out.println("Selection not valid");
        }
    }

    // REQUIRES: for name and eventName input, user must input with only letters and space;
    //           for week num input, user must input an integer > 0;
    //           for week day input, user must input an integer > 0 && < 8;
    //           for time input, user must input integers. for example: 13:20 -> 1320
    // MODIFIES: calendarManage
    // EFFECTS: change or delete an event from the saved calendar
    @SuppressWarnings("checkstyle:MethodLength")
    public void doModifyCalendar() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        System.out.println("please input your name");
        String name = input.next();
        printNameError(name);
        showAllMyEvents(name);
        System.out.println("Type the event name that you want to modify");
        String eventName = input.next();
        System.out.println("Type the week number of the event");
        int weekNum = input.nextInt();
        printWeekNumError(weekNum);
        System.out.println("Type the week day of the event");
        int weekDay = input.nextInt();
        printWeekDayError(weekDay);
        System.out.println("Type the start time of the event");
        int startTime = input.nextInt();
        System.out.println("Type the end time of the event");
        int endTime = input.nextInt();
        printTimeError(startTime, endTime);

        Event event = calendarManage.findEvent(name, eventName, weekNum, weekDay, startTime, endTime);

        if (event == null) {
            System.out.println("Event not found");
            runApp();
        }
        System.out.println("y -> I want to change this event");
        System.out.println("d -> I want to delete this event");
        if (input.next().equalsIgnoreCase("y")) {
            System.out.println("Type the new name for the event");
            String eventNameNew = input.next();
            System.out.println("Type the new week number for the event");
            int weekNumNew = input.nextInt();
            printWeekNumError(weekNumNew);
            System.out.println("Type the new week day for the event");
            int weekDayNew = input.nextInt();
            printWeekDayError(weekDayNew);
            System.out.println("Type the new start time for the event");
            int startTimeNew = input.nextInt();
            System.out.println("Type the new end time for the event");
            int endTimeNew = input.nextInt();
            printTimeError(startTimeNew, endTimeNew);
            calendarManage.removeEvent(event);
            calendarManage.addEvent(new Event(name, eventNameNew, weekNumNew, weekDayNew, startTimeNew, endTimeNew));
        } else if (input.next().equalsIgnoreCase("d")) {
            calendarManage.removeEvent(event);
        } else {
            System.out.println("Selection not valid");
        }
        System.out.println("press any key to go back to the main menu");
    }

    // EFFECTS: print error message for illegal name
    public void printNameError(String name) {
        event = new Event(name, "event", 1, 1,0, 2359);
        if (!event.checkPersonName(name)) {
            System.out.println("Name can only contain letters and space. Redirect to main menu...");
            runApp();
        }
    }

    // EFFECTS: print error message for illegal week num
    public void printWeekNumError(int weekNum) {
        event = new Event("name", "event", weekNum, 1,0, 2359);
        if (!event.checkWeekNum(weekNum)) {
            System.out.println("Week number should be greater than 0. Redirect to main menu...");
            runApp();
        }
    }

    // EFFECTS: print error message for illegal week day
    public void printWeekDayError(int weekDay) {
        event = new Event("name", "event", 1, weekDay,0, 2359);
        if (!event.checkWeekDay(weekDay)) {
            System.out.println("Week day should be 1 to 7. Redirect to main menu...");
            runApp();
        }
    }

    // EFFECTS: print error message for illegal time
    public void printTimeError(int startTime, int endTime) {
        event = new Event("name", "event", 1, 1, startTime, endTime);
        if (!event.checkTime(startTime, endTime)) {
            System.out.println("Wrong time input. For example: should input 1548 for 15:48. Redirect to main menu...");
            runApp();
        }
    }

    // EFFECTS: show all the saved events of a given person
    public void showAllMyEvents(String name) {
        for (int i = 0; i < calendarManage.getCalendar().size(); i++) {
            if (calendarManage.getCalendar().get(i).getPersonName().equalsIgnoreCase(name)) {
                printEvent(calendarManage.getCalendar().get(i));
            }
        }
    }

    // EFFECTS: print an event
    public void printEvent(Event event) {
        System.out.println("Your name: " + event.getPersonName());
        System.out.println("Your event name: " + event.getEventName());
        System.out.println("Your event time: " + "Week " + event.getWeekNum() + ","
                + "Day " + event.getWeekDay() + ","
                + event.getStartTime() + "-" + event.getEndTime());
    }

    // EFFECTS: print friend's valid time
    public void printFriendValidTime(ArrayList<TimeInterval> validTimeIntervals) {
        for (TimeInterval timeInterval : validTimeIntervals) {
            System.out.println("Friend's available time of today: " + timeInterval.getStartTime()
                    + "-"
                    + timeInterval.getEndTime());
        }
    }

}


