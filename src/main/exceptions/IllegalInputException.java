package exceptions;

// exception for unexpected user's input
public class IllegalInputException extends Exception {
    public IllegalInputException() {
        super("Please check your input:\n"
                + "- name can only contain letters and space\n"
                + "- week number should be greater than 0\n"
                + "- time should be input as integer and start time < end time. For example: input 800 for 8:00\n"
                + "Redirect to main menu...\n\n");
    }
}
