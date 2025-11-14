package interface_adapter.workbook_serialisation;

import entity.Workbook;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class JsonWorkbookSerialiser implements WorkbookSerialiser {
    private final String VERSION_KEY = "version";
    private final String TIMETABLES_KEY = "timetables";

    @Override
    public String serialise(Workbook workbook) {
        JSONObject jo = new JSONObject();
        jo.put(VERSION_KEY, "0");

        Collection<String> serialisedTimetables = new ArrayList<>();
        workbook.getTimetables().forEach(timetable -> {

        });

        jo.put(TIMETABLES_KEY, serialisedTimetables);
        return jo.toString();
    }

    /**
     * @param jsonString Workbook data serialised as JSON.
     */
    @Override
    public Workbook deserialise(String jsonString) throws IllegalArgumentException {
        JSONObject jo;
        try {
            jo = new JSONObject(jsonString);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Could not parse provided string as JSON.");
        }

        try {
            String version = jo.getString(VERSION_KEY);
            if(!version.equals("0")) {
                throw new IllegalArgumentException("Incorrect version.");
            }
        } catch (JSONException e) {
            throw new IllegalArgumentException("Invalid \"" + VERSION_KEY + "\" in provided in JSON string.");
        }

        Workbook workbook = new Workbook(new ArrayList<>());

        return null;
    }
}
