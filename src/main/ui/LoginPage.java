package ui;

import model.Calendar;
import model.MyEvent;
import persistence.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// REFERENCE: https://www.youtube.com/watch?v=Hiv3gwJC5kw
// Represents the login page
public class LoginPage extends JFrame implements ActionListener {
    private Calendar calendar;

    private JFrame frame;
    private JLabel lbName;
    private JLabel lbWeekNum;
    private JTextField tfName;
    private JTextField tfWeekNum;
    private JButton buttonCheck;
    private JButton buttonReset;
    private JButton buttonCreate;

    private static final String JSON_STORE = "./data/calendar.json";
    private JsonReader jsonReader;

    public LoginPage() throws FileNotFoundException {
        frame = new JFrame();

        lbName = new JLabel("Name: ");
        lbName.setBounds(70, 70, 75, 25);

        lbWeekNum = new JLabel("Week Num: ");
        lbWeekNum.setBounds(70, 120, 75, 25);

        tfName = new JTextField();
        tfName.setBounds(145, 70, 200, 25);
        tfName.setPreferredSize(new Dimension(80, 30));

        tfWeekNum = new JTextField();
        tfWeekNum.setBounds(145, 120, 200, 25);
        tfWeekNum.setPreferredSize(new Dimension(80, 30));

        buttonCreate = new JButton("Create Calendar");
        buttonCreate.setBounds(10, 180, 150, 25);
        buttonCreate.addActionListener(this);

        buttonCheck = new JButton("Check My Calendar");
        buttonCheck.setBounds(170, 180, 150, 25);
        buttonCheck.addActionListener(this);

        buttonReset = new JButton("Reset My Input");
        buttonReset.setBounds(330, 180, 150, 25);
        buttonReset.addActionListener(this);

        jsonReader = new JsonReader(JSON_STORE);
        frame.addWindowListener(new PrintLog());
        init();
    }

    // EFFECTS: init the frame
    public void init() {
        frame.setTitle("Welcome!");
        frame.setLayout(null);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUp();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Main.printEventLog();
            }
        });
    }

    // EFFECTS: add components to frame
    public void setUp() {
        frame.add(lbName);
        frame.add(lbWeekNum);
        frame.add(tfName);
        frame.add(tfWeekNum);
        frame.add(buttonCreate);
        frame.add(buttonCheck);
        frame.add(buttonReset);
    }

    // EFFECTS: perform the desired action when key is pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonReset) {
            resetInput();
        }
        if (e.getSource() == buttonCreate) {
            createCalendar();
        }

        if (e.getSource() == buttonCheck) {
            String name = tfName.getText();
            if (!MyEvent.checkPersonName(name)) {
                inputErrorMsg();
            }
            int weekNum = 0;
            if (MyEvent.isNumeric(tfWeekNum.getText())) {
                weekNum = Integer.parseInt(tfWeekNum.getText());
            } else {
                inputErrorMsg();
            }
            loadCalendar();
            calendarNotExist(name, weekNum);
            MainPage mainPage = getMainPage(name, weekNum);
            mainPage.runMainPage();

            frame.setVisible(false);
        }
    }

    // EFFECTS: create new calendar
    private void createCalendar() {
        loadCalendar();
        new CreateNewCalendarPage(calendar);
    }

    // EFFECTS: reset all the input in tf
    private void resetInput() {
        tfName.setText("");
        tfWeekNum.setText("");
    }

    // EFFECTS: get main page
    private MainPage getMainPage(String name, int weekNum) {
        MainPage mainPage = null;
        try {
            mainPage = new MainPage(name, weekNum, calendar);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        return mainPage;
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

    // EFFECTS: the behavior of calendar not exist
    private void calendarNotExist(String name, int weekNum) {
        if (!isCalendarExist(calendar, name, weekNum)) {
            if (isNameExist(calendar, name)) {
                JOptionPane.showMessageDialog(null,
                        "No Calendar found for the given week. Calendar is found under these weeks: "
                                + nameExists(calendar, name));
            } else {
                JOptionPane.showMessageDialog(null,
                        "No Calendar Found", "Warning", JOptionPane.WARNING_MESSAGE);
            }
            throw new RuntimeException();
        }
    }

    // EFFECTS: check if there is calendar under given name and week number
    private boolean isCalendarExist(Calendar calendar, String name, int weekNum) {
        for (MyEvent e : calendar.getMyEvents(name)) {
            if (e.getPersonName().equalsIgnoreCase(name) && e.getWeekNum() == weekNum) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: check if there is calendar under given name
    private boolean isNameExist(Calendar calendar, String name) {
        for (MyEvent e : calendar.getMyEvents(name)) {
            if (e.getPersonName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: return all the week numbers under the given name
    private String nameExists(Calendar calendar, String name) {
        List<String> weekNumbers = new ArrayList<>();
        for (MyEvent e : calendar.getMyEvents(name)) {
            if (e.getPersonName().equalsIgnoreCase(name) && !weekNumbers.contains(String.valueOf(e.getWeekNum()))) {
                weekNumbers.add(Integer.toString(e.getWeekNum()));
            }
        }
        return String.join(", ", weekNumbers);
    }


    // EFFECTS: load calendar from file
    private void loadCalendar() {
        try {
            calendar = jsonReader.read();
            System.out.println("Loaded Calendar from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
            JOptionPane.showMessageDialog(null,
                    "Unable to read from file: " + JSON_STORE, "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }


    public JFrame getFrame() {
        return frame;
    }
}
