package ui;

import model.Calendar;
import model.Event;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

// represents the page for creating new calendar
public class CreateNewCalendarPage implements ActionListener {
    private Calendar calendar;
    private JsonWriter jsonWriter;
    private static final String JSON_STORE = "./data/calendar.json";

    private JFrame frame;
    private JLabel lbName;
    private JLabel lbEventName;
    private JLabel lbWeekNum;
    private JLabel lbWeekDay;
    private JLabel lbStartTime;
    private JLabel lbEndTime;
    private JTextField tfName;
    private JTextField tfEventName;
    private JTextField tfWeekNum;
    private JTextField tfWeekDay;
    private JTextField tfStartTime;
    private JTextField tfEndTime;

    private JButton buttonCreate;
    private JButton buttonReset;


    public CreateNewCalendarPage(Calendar calendar) {
        frame = new JFrame();
        this.calendar = calendar;
        jsonWriter = new JsonWriter(JSON_STORE);

        inputSetup();

        init();
    }

    // EFFECTS: init variables
    private void inputSetup() {
        labelSetUp();

        textFieldSetUp();

        buttonCreate = new JButton("Create New Calendar");
        buttonCreate.setBounds(20, 230, 180, 25);
        buttonCreate.addActionListener(this);

        buttonReset = new JButton("Reset My Input");
        buttonReset.setBounds(210, 230, 180, 25);
        buttonReset.addActionListener(this);
    }

    // EFFECTS: init text fields
    private void textFieldSetUp() {
        tfName = new JTextField();
        tfName.setBounds(180, 30, 150, 25);
        tfName.setPreferredSize(new Dimension(80, 30));

        tfEventName = new JTextField();
        tfEventName.setBounds(180, 60, 150, 25);
        tfEventName.setPreferredSize(new Dimension(80, 30));

        tfWeekNum = new JTextField();
        tfWeekNum.setBounds(180, 90, 150, 25);
        tfWeekNum.setPreferredSize(new Dimension(80, 30));

        tfWeekDay = new JTextField();
        tfWeekDay.setBounds(180, 120, 150, 25);
        tfWeekDay.setPreferredSize(new Dimension(80, 30));

        tfStartTime = new JTextField();
        tfStartTime.setBounds(180, 150, 150, 25);
        tfStartTime.setPreferredSize(new Dimension(80, 30));

        tfEndTime = new JTextField();
        tfEndTime.setBounds(180, 180, 150, 25);
        tfEndTime.setPreferredSize(new Dimension(80, 30));
    }

    // EFFECTS: init labels
    private void labelSetUp() {
        lbName = new JLabel("Name: ");
        lbName.setBounds(70, 30, 100, 25);

        lbEventName = new JLabel("Event Name: ");
        lbEventName.setBounds(70, 60, 100, 25);

        lbWeekNum = new JLabel("Week Number: ");
        lbWeekNum.setBounds(70, 90, 100, 25);

        lbWeekDay = new JLabel("Week Day: ");
        lbWeekDay.setBounds(70, 120, 100, 25);

        lbStartTime = new JLabel("Start Time: ");
        lbStartTime.setBounds(70, 150, 100, 25);

        lbEndTime = new JLabel("End Time: ");
        lbEndTime.setBounds(70, 180, 100, 25);

    }

    // EFFECTS: init frame
    public void init() {
        frame.setTitle("Create New Calendar");
        frame.setLayout(null);
        frame.setSize(410, 310);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        setUp();
        frame.setVisible(true);
    }

    // EFFECTS: add components to frame
    public void setUp() {
        frame.add(lbName);
        frame.add(lbEventName);
        frame.add(lbWeekNum);
        frame.add(lbWeekDay);
        frame.add(lbStartTime);
        frame.add(lbEndTime);
        frame.add(tfName);
        frame.add(tfEventName);
        frame.add(tfWeekNum);
        frame.add(tfWeekDay);
        frame.add(tfStartTime);
        frame.add(tfEndTime);
        frame.add(buttonCreate);
        frame.add(buttonReset);
    }

    // EFFECTS: perform the actions when key is pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonCreate) {
            createNewCalendar();
        }
        if (e.getSource() == buttonReset) {
            resetInput();
        }
    }

    // EFFECTS: behavior when press the check time button
    private void createNewCalendar() {
        String name = tfName.getText();
        String eventName = tfEventName.getText();
        String weekNumString = tfWeekNum.getText();
        String weekDayString = tfWeekDay.getText();
        String startTimeString = tfStartTime.getText();
        String endTimeString = tfEndTime.getText();
        int weekNum = 0;
        int weekDay = 0;
        int startTime = 0;
        int endTime = 0;
        if (Event.isNumeric(weekNumString) && Event.isNumeric(weekDayString)
                && Event.isNumeric(startTimeString) && Event.isNumeric(endTimeString)) {
            weekNum = Integer.parseInt(weekNumString);
            weekDay = Integer.parseInt(weekDayString);
            startTime = Integer.parseInt(startTimeString);
            endTime = Integer.parseInt(endTimeString);
        } else {
            inputErrorMsg();
            throw new IllegalArgumentException();
        }
        checkInputValid(name, weekNum, weekDay, startTime, endTime);
        calendar.addEvent(new Event(name, eventName, weekNum, weekDay, startTime, endTime));
        saveCalendar();
    }

    // EFFECTS: check if the input is valid and the name is not in the calendar yet
    private void checkInputValid(String name, int weekNum, int weekDay, int startTime, int endTime) {
        if (!Event.checkPersonName(name) || !Event.checkWeekNum(weekNum)
                || !Event.checkWeekDay(weekDay) || !Event.checkTime(startTime, endTime)) {
            inputErrorMsg();
            throw new IllegalArgumentException();
        }
        if (calendar.findName(name)) {
            JOptionPane.showMessageDialog(null, "Person already exists. Please use check calendar");
            throw new IllegalArgumentException();
        }
    }

    // EFFECTS: behavior when press reset button
    private void resetInput() {
        tfName.setText("");
        tfEventName.setText("");
        tfWeekNum.setText("");
        tfWeekDay.setText("");
        tfStartTime.setText("");
        tfEndTime.setText("");
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
}

