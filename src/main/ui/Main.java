package ui;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

// starts the app
public class Main {
    public static void main(String[] args) {
        try {
            new OpeningPage();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run the application: File not found");
            JOptionPane.showMessageDialog(null, "File not found!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            System.out.println("image not found");
        }
    }
}
