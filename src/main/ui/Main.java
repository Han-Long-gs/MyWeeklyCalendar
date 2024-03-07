package ui;

import java.io.FileNotFoundException;

// starts the app
public class Main {
    public static void main(String[] args) {
        try {
            new CalendarApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run the application: File not found");
        }
    }
}
