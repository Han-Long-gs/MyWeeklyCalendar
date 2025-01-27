package persistence;

import org.json.JSONObject;

// interface for JsonReader and JsonWriter
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}

