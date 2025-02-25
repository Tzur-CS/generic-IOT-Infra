package rps;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JSONParserGson implements RPSParser<Map<String, String>, JsonObject> {
    /*
    example:
    { command: "regCompany",
      args: {
         name: "Apple",
         address: "343 indian road crescent"
         }
    }
    */

    @Override
    public Map<String, String> parse(JsonObject json) {
        Map<String, String> resultMap = new HashMap<>();

        if (json == null || !json.has("command") || !json.has("args")) {
            return null; // Return null if format is incorrect
        }

        Set<Map.Entry<String, JsonElement>> entries = json.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();
            JsonElement valueElement = entry.getValue();

            if (valueElement.isJsonObject()) {
                JsonObject nestedObject = valueElement.getAsJsonObject();
                for (Map.Entry<String, JsonElement> nestedEntry : nestedObject.entrySet()) {
                    resultMap.put(nestedEntry.getKey(), nestedEntry.getValue().getAsString());
                }
            } else {
                resultMap.put(key, valueElement.getAsString());
            }
        }

        return resultMap;
    }
}
