package persistence;

import org.json.JSONObject;

// interface for JsonReader and JsonWriter
// CREDIT: JsonSerializationDemo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}

