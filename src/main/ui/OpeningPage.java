package ui;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

// represents the opening page of the app
public class OpeningPage extends JFrame {
    private JFrame frame;
    private Timer timer;

    // represents the opening page
    public OpeningPage() throws IOException {
        timer = new Timer();

        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon backgroundImage = new ImageIcon("./src/main/ui/calendar_pic.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        frame.add(backgroundLabel);

        frame.setVisible(true);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                frame.setVisible(false);
                timer.cancel();
                try {
                    new LoginPage();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 5000);

    }

}
