package exceptions;

import javax.swing.*;

public class NoFriendFoundException extends Exception {
    public NoFriendFoundException() {
        super();
        JOptionPane.showMessageDialog(null, "No calendar found under this friend's name!");
    }
}
