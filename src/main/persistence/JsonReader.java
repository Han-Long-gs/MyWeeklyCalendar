package persistence;

import model.Event;
import model.Calendar;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// represents a reader that reads calendar from JSON data stored in file
// CREDIT: JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Calendar that user saved from file and return it
    //          throws IOException if an error occurs reading from file
    public Calendar read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCalendar(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    //          throws IOException if an error occurs reading from file
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }


    // MODIFIES: calendar
    // EFFECTS: parses events from JSON object and adds them to calendar
    private Calendar parseCalendar(JSONObject jsonObject) {
        String dataname = jsonObject.getString("calendarName");
        Calendar calendar = new Calendar(dataname);
        addEvents(calendar, jsonObject);
        return calendar;
    }

    // MODIFIES: calendar
    // EFFECTS: helper method of parseCalendar().
    //          retrieve all the events saved under eventList and adds them to calendar
    private void addEvents(Calendar calendar, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("eventList");
        for (Object json : jsonArray) {                    // why Object not JSONObject
            JSONObject nextEvent = (JSONObject) json;
            addEvent(calendar, nextEvent);
        }
    }

    // MODIFIES: calendar
    // EFFECTS: helper method of addEvents. add each event to the calendar
    private void addEvent(Calendar calendar, JSONObject jsonObject) {
        String name = jsonObject.getString("personName");
        String eventName = jsonObject.getString("eventName");
        int weekNum = jsonObject.getInt("weekNum");
        int weekDay = jsonObject.getInt("weekDay");
        int startTime = jsonObject.getInt("startTime");
        int endTime = jsonObject.getInt("endTime");
        Event event = new Event(name, eventName, weekNum, weekDay, startTime, endTime);
        calendar.addEvent(event);
    }

}

