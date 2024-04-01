package ui;

import model.Event;
import model.EventLog;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// starts the app
public class Main {

    public static void main(String[] args) {
        OpeningPage openingPage = null;
        try {
            openingPage = new OpeningPage();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run the application: File not found");
            JOptionPane.showMessageDialog(null, "File not found!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            System.out.println("image not found");
        }

        if (openingPage != null) {
            openingPage.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    printEventLog();
                }
            });
        }
    }

    static void printEventLog() {
        for (Event e : EventLog.getInstance()) {
            System.out.println(e);
        }
    }

}
