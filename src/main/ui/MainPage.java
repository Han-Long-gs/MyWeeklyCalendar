package ui;

import model.Calendar;
import model.Event;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;

// represents the GUI main page
public class MainPage extends JFrame implements ActionListener {
    // CONSTANTS
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 600;
    private static final Font TITLE_FONT = new Font("SF Pro", Font.PLAIN, 20);
    private static final Dimension BOX_SIZE = new Dimension(70, 20);
    JPanel centerPanel = new JPanel();
    private String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private String name;
    private int weekNum;
    private LoginPage loginPage;
    private Calendar calendar;
    private List<Event> allEvents;
    private List<Event> thisUserEvents;
    private static final String JSON_STORE = "./data/calendar.json";
    private JsonWriter jsonWriter;
    private JFrame frame;
    //Top Panel
    private JLabel lbThisWeek;
    private JLabel lbWeekChoose;
    private JComboBox cbWeekChoose;
    private JButton buttonConfirmWeekChoose;
    //Center Panel
//    private JPanel[] dayPanels;

    // Bottom Panel
    private JTextField tfWeekNum;
    private JComboBox cbDayNum;
    private JLabel lbWeekNum;
    private JLabel lbDayNum;
    private JLabel lbEventName;
    private JTextField tfEventName;
    private JTextField tfStartTime;
    private JTextField tfEndTime;
    private JLabel lbStartTime;
    private JLabel lbEndTime;
//    private JButton buttonAllEvents;
    private JButton buttonAddEvent;
    private JButton buttonDeleteEvent;
    private JButton buttonCheckFriend;
    private JButton buttonSaveAllChanges;
    private JButton buttonBack;

    // Constructor
    public MainPage(String name, int weekNum, Calendar calendar) throws FileNotFoundException {
        frame = new JFrame();
        this.name = name;
        this.weekNum = weekNum;
        this.calendar = calendar;
        this.allEvents = calendar.getEvents();
        this.thisUserEvents = calendar.getMyEvents(name);
        jsonWriter = new JsonWriter(JSON_STORE);
        loginPage = new LoginPage();
    }

    // EFFECTS: run main page
    public void runMainPage() {
        topVar();
        bottomVar();
        init();
    }

    // EFFECTS: init the main page
    public void init() {
        frame.setTitle("Weekly Calendar of " + name);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(new Color(139, 170, 202));

        topSetUp();
        centerSetUp();
        bottomSetUp();

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonAddEvent) {
            createEvent();
        }

        if (e.getSource() == buttonDeleteEvent) {
            deleteEvent();
        }

        if (e.getSource() == buttonSaveAllChanges) {
            saveCalendar();
        }

        if (e.getSource() == buttonBack) {
            loginPage.getFrame().setVisible(true);
            frame.setVisible(false);
        }

        if (e.getSource() == buttonCheckFriend) {
            checkFriendTime();
        }

        if (e.getSource() == buttonConfirmWeekChoose) {
            changeWeekView();
        }

    }

    // EFFECTS: create an event and add it to the existing events
    public void createEvent() {
        String eventName = tfEventName.getText();
        String weekNumString = tfWeekNum.getText();
        String startTimeString = tfStartTime.getText();
        String endTimeString = tfEndTime.getText();
        int weekNum = 0;
        int weekDay = 0;
        int startTime = 0;
        int endTime = 0;
        if (Event.isNumeric(weekNumString) && Event.isNumeric(startTimeString) && Event.isNumeric(endTimeString)) {
            weekNum = Integer.parseInt(tfWeekNum.getText());
            weekDay = weekStringToNum((String) cbDayNum.getSelectedItem());
            startTime = Integer.parseInt(tfStartTime.getText());
            endTime = Integer.parseInt(tfEndTime.getText());
        } else {
            inputErrorMsg();
            throw new IllegalArgumentException();
        }
        createEventHelper(weekNum, eventName, weekDay, startTime, endTime);
    }

    // EFFECTS: helper method for create event
    private void createEventHelper(int weekNum, String eventName, int weekDay, int startTime, int endTime) {
        if (!Event.checkWeekDay(weekDay) || !Event.checkTime(startTime, endTime)) {
            inputErrorMsg();
            throw new IllegalArgumentException();
        }
        Event event = new Event(name, eventName, weekNum, weekDay, startTime, endTime);
        Event e1 = null;

        for (Event e : allEvents) {
            if (e.equals(event)) {
                e1 = e;
            }
        }

        if (e1 != null) {
            JOptionPane.showMessageDialog(null, "Event already exists!");
        } else {
            calendar.addEvent(event);
            JOptionPane.showMessageDialog(null, "Event successfully created!");
        }
        updateCenterPanel();
    }

    // EFFECTS: remove an event from the existing events
    public void deleteEvent() {
        String eventName = tfEventName.getText();
        String weekNumString = tfWeekNum.getText();
        String startTimeString = tfStartTime.getText();
        String endTimeString = tfEndTime.getText();
        int weekDay = 0;
        int startTime = 0;
        int endTime = 0;
        if (Event.isNumeric(weekNumString) && Event.isNumeric(startTimeString) && Event.isNumeric(endTimeString)) {
            int weekNum = Integer.parseInt(tfWeekNum.getText());
            weekDay = weekStringToNum((String) cbDayNum.getSelectedItem());
            startTime = Integer.parseInt(tfStartTime.getText());
            endTime = Integer.parseInt(tfEndTime.getText());
        } else {
            inputErrorMsg();
            throw new IllegalArgumentException();
        }
        deleteEventHelper(eventName, weekDay, startTime, endTime);
    }

    // EFFECTS: helper method for delete event
    private void deleteEventHelper(String eventName, int weekDay, int startTime, int endTime) {
        if (!Event.checkWeekDay(weekDay) || !Event.checkTime(startTime, endTime)) {
            inputErrorMsg();
            throw new IllegalArgumentException();
        }
        Event event = new Event(name, eventName, weekNum, weekDay, startTime, endTime);
        Event e1 = null;
        for (Event e : allEvents) {
            if (e.equals(event)) {
                e1 = e;
            }
        }

        if (e1 != null) {
            calendar.removeEvent(e1);
            JOptionPane.showMessageDialog(null, "Event successfully removed!");
            centerSetUp();
        } else {
            JOptionPane.showMessageDialog(null, "Event not found...");
        }
        updateCenterPanel();
    }

    // EFFECTS: returns the corresponded week day String of the input week index
    public int weekStringToNum(String weekDay) {
        switch (weekDay) {
            case "Mon": return 1;
            case "Tue": return 2;
            case "Wed": return 3;
            case "Thu": return 4;
            case "Fri": return 5;
            case "Sat": return 6;
            case "Sun": return 7;
            default: return 0;
        }
    }

    // EFFECTS: write the calendar to the file
    private void saveCalendar() {
        try {
            jsonWriter.open();
            jsonWriter.write(calendar);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null, "Saved Calendar to " + JSON_STORE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Save failed...");
        }
    }

    // EFFECTS: check friend's valid time and print on the window
    public void checkFriendTime() {
        new CheckFriendPage(name, calendar);
    }

    // EFFECTS: change the center panel to the input week
    public void changeWeekView() {
        weekNum = Integer.parseInt(cbWeekChoose.getSelectedItem().toString());

        List<Event> tempList = new ArrayList<>();

        for (Event e : allEvents) {
            if (e.getWeekNum() == weekNum && e.getPersonName().equalsIgnoreCase(name)) {
                tempList.add(e);
            }
        }
        thisUserEvents = tempList;
        updateCenterPanel();
    }

    // EFFECTS: reset and update the center panel
    private void updateCenterPanel() {
        while (centerPanel.getComponentCount() > 0) {
            centerPanel.remove(0);
        }
        centerSetUp();
        lbThisWeek.setText("Week: " + weekNum);
        frame.add(centerPanel);
        frame.setVisible(true);
    }

    // MODIFIES: this.cbWeekChoose
    // EFFECTS: set up the week selection base on the user's name
    public void addToWeekChoose() {
        List<String> weekNumbers = new ArrayList<>();
        for (Event e : thisUserEvents) {
            if (!weekNumbers.contains(String.valueOf(e.getWeekNum()))) {
                weekNumbers.add(String.valueOf(e.getWeekNum()));
            }
        }

        for (String wn : weekNumbers) {
            this.cbWeekChoose.addItem(wn);
        }
    }


    // EFFECTS: set up the center panel of the page
    public void centerSetUp() {
        centerPanel.setLayout(new GridLayout(1, 7));
        centerPanel.setBackground(new Color(139, 170, 202));

        String[] weekDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (int i = 0; i < 7; i++) {
            JPanel dayPanel = renderDayPanel(weekDays[i], i + 1);
            centerPanel.add(dayPanel);
        }

        frame.add(centerPanel, BorderLayout.CENTER);
    }

    // EFFECTS: render the day panel on the center panel
    public JPanel renderDayPanel(String dayName, int weekDay) {
        String[] events = eventsForToday(weekDay);
        JPanel dayPanel = new JPanel(new BorderLayout());
        dayPanel.setBackground(new Color(139, 170, 202));
        JLabel label = new JLabel(dayName, SwingConstants.CENTER);
        JList<String> list = new JList<>(events);
        list.setCellRenderer(new MyRenderer());
        dayPanel.add(label, BorderLayout.NORTH);
        dayPanel.add(new JScrollPane(list), BorderLayout.CENTER);

        return dayPanel;
    }

    // Top Panel: lbThisWeek; buttonLastWeek; buttonNextWeek
    // EFFECTS: set up the top panel
    public void topSetUp() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.setBackground(new Color(139, 170, 202));

        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        firstRow.setBackground(new Color(139, 170, 202));
        firstRow.setBackground(new Color(139, 170, 202));
        firstRow.add(lbThisWeek);
        topPanel.add(firstRow);

        JPanel secondRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        secondRow.add(lbWeekChoose);
        secondRow.add(cbWeekChoose);
        secondRow.add(buttonConfirmWeekChoose);
        topPanel.add(secondRow);

        frame.add(topPanel, BorderLayout.NORTH);
    }

    // EFFECTS: returns a String[] of all the events name of the given day;
    //          helper method for renderDayPanel
    public String[] eventsForToday(int weekDay) {
        List<String> events = new ArrayList<>();
        reorderEvents();
        for (Event e : calendar.getMyEvents(name)) {
            if (e.getWeekNum() == weekNum && e.getWeekDay() == weekDay) {
                events.add(e.getEventName() + " " + eventStartEndTime(e));
            }
        }
        return events.toArray(new String[0]);
    }

    // EFFECTS: returns a string combining start and end time
    public String eventStartEndTime(Event e) {
        return (intToTime(e.getStartTime()) + " - " + intToTime(e.getEndTime()));
    }

    // EFFECTS: change the int to time format string
    private String intToTime(int time) {
        return (" " + time / 100 + ":" + (time % 100) / 10 + (time % 100) % 10);
    }

    // EFFECTS: return a list of event of the user that is ascending ordered according to start time
    private void reorderEvents() {
        Comparator<Event> startTimeComparator = new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return Integer.compare(o1.getStartTime(), o2.getStartTime());
            }
        };
        Collections.sort(thisUserEvents, startTimeComparator);
    }

    // Bottom Panel: tfWeekNum; cbDayNum; lbWeekNum; lbDayNum; tfStartTime; tfEndTime; lbStartTime; lbEndTime
    //               buttonAddEvent; buttonDeleteEvent
    public void bottomSetUp() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1));
        bottomPanel.setBackground(new Color(139, 170, 202));

        JPanel firstRow = formFirstRow();

        bottomPanel.add(firstRow);

        JPanel secondRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        secondRow.setBackground(new Color(139, 170, 202));
        secondRow.add(buttonAddEvent);
        secondRow.add(buttonDeleteEvent);
        secondRow.add(buttonCheckFriend);
        secondRow.add(buttonSaveAllChanges);
        secondRow.add(buttonBack);

        bottomPanel.add(secondRow);

        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    // EFFECTS: create the first row of the bottom panel
    private JPanel formFirstRow() {
        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        firstRow.setBackground(new Color(139, 170, 202));
        firstRow.add(lbWeekNum);
        firstRow.add(tfWeekNum);
        firstRow.add(lbDayNum);
        firstRow.add(cbDayNum);
        firstRow.add(lbEventName);
        firstRow.add(tfEventName);
        firstRow.add(lbStartTime);
        firstRow.add(tfStartTime);
        firstRow.add(lbEndTime);
        firstRow.add(tfEndTime);
        return firstRow;
    }

    // EFFECTS: print illegal input msg
    private static void inputErrorMsg() {
        JOptionPane.showMessageDialog(null, "Please check your input:\n"
                + "- Field can not be empty!\n"
                + "- Name can only contain letters and space.\n"
                + "- Week number should be greater than 0.\n"
                + "- Week day should be between 1 and 7.\n"
                + "Please input again...\n\n", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: init all the variables that relate to the top panel
    public void topVar() {
        lbThisWeek = new JLabel("Week " + weekNum);
        lbThisWeek.setFont(TITLE_FONT);
        lbWeekChoose = new JLabel("Choose Your Week: ");
        cbWeekChoose = new JComboBox<>();
        addToWeekChoose();
        buttonConfirmWeekChoose = new JButton("Confirm");
        buttonConfirmWeekChoose.addActionListener(this);
    }


    // EFFECTS: init all the variables that relate to the bottom panel
    public void bottomVar() {
        bottomInputVar();
        bottomButtonVar();
    }

    // EFFECTS: init bottom buttons
    private void bottomButtonVar() {
        buttonAddEvent = new JButton("Add Event");
        buttonAddEvent.setPreferredSize(new Dimension(100, 40));
        buttonAddEvent.addActionListener(this); //LOL I FORGOT TO ADD AND WONDERING WHY IT WON'T WORK
        buttonDeleteEvent = new JButton("Delete Event");
        buttonDeleteEvent.setPreferredSize(new Dimension(100, 40));
        buttonDeleteEvent.addActionListener(this);
        buttonSaveAllChanges = new JButton("Save Changes");
        buttonSaveAllChanges.setPreferredSize(new Dimension(100, 40));
        buttonSaveAllChanges.addActionListener(this);
        buttonBack = new JButton("Back");
        buttonBack.setPreferredSize(new Dimension(100, 40));
        buttonBack.addActionListener(this);
        buttonCheckFriend = new JButton("Check Friend's Valid Time");
        buttonCheckFriend.setPreferredSize(new Dimension(200, 40));
        buttonCheckFriend.addActionListener(this);

    }

    // EFFECTS: init bottom text fields and combo box
    private void bottomInputVar() {
        tfWeekNum = new JTextField();
        tfWeekNum.setPreferredSize(BOX_SIZE);
        cbDayNum = new JComboBox<>(days);
        cbDayNum.setPreferredSize(new Dimension(100, 40));
        lbWeekNum = new JLabel("Week Number: ");
        lbDayNum = new JLabel("Day of the Week: ");
        lbEventName = new JLabel("Event Name: ");
        tfEventName = new JTextField();
        tfEventName.setPreferredSize(BOX_SIZE);
        tfStartTime = new JTextField();
        tfStartTime.setPreferredSize(BOX_SIZE);
        tfEndTime = new JTextField();
        tfEndTime.setPreferredSize(BOX_SIZE);
        lbStartTime = new JLabel("Start Time: ");
        lbEndTime = new JLabel("End Time: ");
    }
}