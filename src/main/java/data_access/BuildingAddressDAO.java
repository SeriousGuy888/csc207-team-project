package data_access;

import org.json.JSONArray;
import org.json.JSONObject;
import use_case.AddressDataAccessInterface;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implements the data access interface using a local JSON file.
 * This class is responsible for the "detail" of reading the file
 * and parsing the JSON. It uses the org.json library for parsing.
 */
public class BuildingAddressDAO implements AddressDataAccessInterface {

    private final Map<String, String> buildingAddresses;

    public BuildingAddressDAO() {
        // Load the map from the JSON file at initialization.
        this.buildingAddresses = loadBuildingAddresses();
    }

    /**
     * Loads the building codes and addresses from the JSON file using org.json.
     *
     * @return An unmodifiable Map of Building Code (String) to Address (String).
     */
    private Map<String, String> loadBuildingAddresses() {
        Map<String, String> addresses = new HashMap<>();

        try {
            // Read the JSON file content
            String jsonContent = readJsonFileContent("/BuildingCodes.json");
            JSONArray buildings = new JSONArray(jsonContent);

            // Iterate over each JSON object in the array
            for (int i = 0; i < buildings.length(); i++) {
                JSONObject building = buildings.getJSONObject(i);

                // Safely get the code and address, ensuring both keys exist
                if (building.has("Code") && building.has("Address (as found)")) {
                    String code = building.getString("Code");
                    String address = building.getString("Address (as found)");
                    addresses.put(code, address);
                }
            }

        } catch (IOException e) {
            System.err.println("ERROR: Could not read BuildingCodes.json. Addresses will be unavailable.");
            e.printStackTrace();
            return Collections.emptyMap();
        } catch (org.json.JSONException e) {
            // This catch block handles errors from new JSONArray() or .getString()
            System.err.println("ERROR: Could not parse BuildingCodes.json. Check file format.");
            e.printStackTrace();
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(addresses);
    }

    /**
     * Reads the entire content of a file from the application's resources directory.
     * (This helper method was good, no change needed).
     */
    private String readJsonFileContent(String fileName) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + fileName + ". Make sure it is in src/main/resources and the case is correct.");
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Returns an Optional instead of a raw String or null.
     */
    @Override
    public Optional<String> getAddressByCode(String code) {
        // .ofNullable() cleanly handles cases where the code is not in the map,
        // returning an empty Optional if get(code) returns null.
        return Optional.ofNullable(buildingAddresses.get(code));
    }
}