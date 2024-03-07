package exceptions;

// exception for no events when user trying to retrieve
public class NoEventsException extends Exception {
    public NoEventsException() {
        super("No events found. Redirect to the main menu...");
    }
}
