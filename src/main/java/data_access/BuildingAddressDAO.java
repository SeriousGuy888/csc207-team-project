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

public class BuildingAddressDAO implements AddressDataAccessInterface {

    private final Map<String, String> buildingAddresses;

    public BuildingAddressDAO() {
        this.buildingAddresses = loadBuildingAddresses();
    }

    private Map<String, String> loadBuildingAddresses() {
        Map<String, String> addresses = new HashMap<>();

        try {
            String jsonContent = readJsonFileContent("/BuildingCodes.json");
            JSONArray buildings = new JSONArray(jsonContent);

            for (int i = 0; i < buildings.length(); i++) {
                try {
                    JSONObject building = buildings.getJSONObject(i);

                    // Only add if both keys explicitly exist
                    if (building.has("Code") && building.has("Address (as found)")) {
                        String code = building.getString("Code");
                        String address = building.getString("Address (as found)");
                        addresses.put(code, address);
                    }
                } catch (org.json.JSONException e) {
                    // Skip malformed entries silently
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(addresses);
    }

    private String readJsonFileContent(String fileName) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Override
    public Optional<String> getAddressByCode(String code) {
        return Optional.ofNullable(buildingAddresses.get(code));
    }
}