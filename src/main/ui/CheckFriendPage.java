package ui;

import model.Calendar;
import model.MyEvent;
import model.TimeInterval;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// represents the page for checking friend valid time
public class CheckFriendPage implements ActionListener {
    private Calendar calendar;
    private List<MyEvent> myEvents;
    private String userName;

    private JFrame frame;
    private JLabel lbFriendName;
    private JLabel lbWeekNum;
    private JLabel lbWeekDay;
    private JTextField tfFriendName;
    private JTextField tfWeekNum;
    private JTextField tfWeekDay;
    private JButton buttonCheck;
    private JButton buttonReset;


    public CheckFriendPage(String userName, Calendar calendar) {
        frame = new JFrame();
        this.userName = userName;
        this.calendar = calendar;

        inputSetup();

        init();
    }

    // EFFECTS: init variables
    private void inputSetup() {
        lbFriendName = new JLabel("Friend's Name: ");
        lbFriendName.setBounds(70, 70, 300, 25);

        lbWeekNum = new JLabel("Type the week # you want to search: ");
        lbWeekNum.setBounds(70, 120, 300, 25);

        lbWeekDay = new JLabel("Type the day you want to search: ");
        lbWeekDay.setBounds(70, 170, 300, 25);

        tfFriendName = new JTextField();
        tfFriendName.setBounds(500, 70, 200, 25);
        tfFriendName.setPreferredSize(new Dimension(80, 30));

        tfWeekNum = new JTextField();
        tfWeekNum.setBounds(500, 120, 200, 25);
        tfWeekNum.setPreferredSize(new Dimension(80, 30));

        tfWeekDay = new JTextField();
        tfWeekDay.setBounds(500, 170, 200, 25);
        tfWeekDay.setPreferredSize(new Dimension(80, 30));

        buttonCheck = new JButton("Check Friend's Valid Time");
        buttonCheck.setBounds(150, 250, 200, 25);
        buttonCheck.addActionListener(this);

        buttonReset = new JButton("Reset My Input");
        buttonReset.setBounds(450, 250, 200, 25);
        buttonReset.addActionListener(this);
    }

    // EFFECTS: init frame
    public void init() {
        frame.setTitle("Check Friend's Valid Time");
        frame.setLayout(null);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        setUp();
        frame.setVisible(true);
    }

    // EFFECTS: add components to frame
    public void setUp() {
        frame.add(lbFriendName);
        frame.add(lbWeekNum);
        frame.add(lbWeekDay);
        frame.add(tfFriendName);
        frame.add(tfWeekNum);
        frame.add(tfWeekDay);
        frame.add(buttonCheck);
        frame.add(buttonReset);
    }

    // EFFECTS: perform the desired action when key is pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonCheck) {
            checkFriendTime();
        }
        if (e.getSource() == buttonReset) {
            resetInput();
        }
    }

    // EFFECTS: behavior when press the check time button
    private void checkFriendTime() {
        String friendName = tfFriendName.getText();
        String weekNumString = tfWeekNum.getText();
        String weekDayString = tfWeekDay.getText();
        int weekNum = 0;
        int weekDay = 0;
        if (MyEvent.isNumeric(weekNumString) && MyEvent.isNumeric(weekDayString)) {
            weekNum = Integer.parseInt(tfWeekNum.getText());
            weekDay = Integer.parseInt(tfWeekDay.getText());
        } else {
            inputErrorMsg();
            throw new IllegalArgumentException();
        }
        if (!MyEvent.checkPersonName(friendName) || !MyEvent.checkWeekNum(weekNum) || !MyEvent.checkWeekDay(weekDay)) {
            inputErrorMsg();
            throw new IllegalArgumentException();
        }
        findFriend(friendName);
        showFriendValidTime(friendName, weekNum, weekDay);
    }

    // EFFECTS: behavior when press reset button
    private void resetInput() {
        tfFriendName.setText("");
        tfWeekNum.setText("");
        tfWeekDay.setText("");
    }

    // EFFECTS: show friend valid time on msg dialog
    private void showFriendValidTime(String friendName, int weekNum, int weekDay) {
        List<String> validTime = printFriendValidTime(
                calendar.showFriendValidTime(friendName, userName, weekNum, weekDay));
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : validTime) {
            stringBuilder.append(s).append("\n");
        }
        String msg = stringBuilder.toString().trim();
        JOptionPane.showMessageDialog(null, msg);
    }

    // EFFECTS: check if the friend exists
    public void findFriend(String friendName) {
        if (!calendar.findName(friendName)) {
            JOptionPane.showMessageDialog(null, "No calendar found under this friend's name!");
            throw new RuntimeException();
        }
    }

    // EFFECTS: print friend's valid time
    public List<String> printFriendValidTime(ArrayList<TimeInterval> validTimeIntervals) {
        List<String> validTimeInterval = new ArrayList<>();
        for (TimeInterval timeInterval : validTimeIntervals) {
            validTimeInterval.add("Friend's available time of today: " + intToTime(timeInterval.getStartTime())
                    + "-"
                    + intToTime(timeInterval.getEndTime()));
        }
        return validTimeInterval;
    }

    // EFFECTS: change the int to time format string
    private String intToTime(int time) {
        return (" " + time / 100 + ":" + (time % 100) / 10 + (time % 100) % 10);
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
}
