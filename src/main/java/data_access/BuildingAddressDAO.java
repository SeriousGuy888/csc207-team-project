package data_access;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads building codes and their associated addresses from the buildingCodes.json
 * file located in the resources directory.
 * NOTE: For simple JSON parsing without external libraries (like Gson/Jackson),
 * this implementation uses regular expressions to extract the Code and Address
 * fields directly from the file content.
 */
public class BuildingAddressDAO {

    private final Map<String, String> buildingAddresses;

    public BuildingAddressDAO() {
        this.buildingAddresses = loadBuildingAddresses();
    }

    /**
     * Loads the building codes and addresses from the JSON file into a Map.
     * @return An unmodifiable Map of Building Code (String) to Address (String).
     */
    private Map<String, String> loadBuildingAddresses() {
        Map<String, String> addresses = new HashMap<>();

        try {
            String jsonContent = readJsonFileContent("/BuildingCodes.json"); // Case sensitivity fixed here

            // Regex to find: "Code": "CODE_VALUE", ... "Address (as found)": "ADDRESS_VALUE"
            Pattern pattern = Pattern.compile(
                    "\"Code\"\\s*:\\s*\"([^\"]+)\"\\s*,.*?\"Address \\(as found\\)\"\\s*:\\s*\"([^\"]+)\"",
                    Pattern.DOTALL
            );
            Matcher matcher = pattern.matcher(jsonContent);

            while (matcher.find()) {
                String code = matcher.group(1);
                String address = matcher.group(2);
                addresses.put(code, address);
            }

        } catch (IOException e) {
            System.err.println("ERROR: Could not read BuildingCodes.json. Addresses will be unavailable.");
            // Print stack trace for debugging purposes
            e.printStackTrace();
            // Return an empty map on failure
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(addresses);
    }

    /**
     * Reads the entire content of a file from the application's resources directory.
     * @param fileName The path to the file in the resources folder (e.g., /BuildingCodes.json).
     * @return The file content as a single String.
     * @throws IOException If the file cannot be read.
     */
    private String readJsonFileContent(String fileName) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + fileName + ". Make sure it is in src/main/resources and the case is correct.");
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public String getAddressByCode(String code) {
        return buildingAddresses.get(code);
    }
}