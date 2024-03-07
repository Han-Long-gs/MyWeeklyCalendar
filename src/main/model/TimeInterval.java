package model;

// represents a time interval
public class TimeInterval {
    private final int startTime;
    private final int endTime;

    public TimeInterval(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;

    }

    // SIMPLE GETTERS
    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

}
