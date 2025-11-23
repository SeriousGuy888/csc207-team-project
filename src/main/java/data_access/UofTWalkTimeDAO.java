package data_access;

import use_case.WalkTimeDataAccessInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class UofTWalkTimeDAO implements WalkTimeDataAccessInterface {

    // Cache to map "BA" -> [-79.397, 43.659] (Longitude, Latitude)
    private final Map<String, double[]> buildingCoordinates = new HashMap<>();
    private final HttpClient client;

    /**
     * Constructor loads the JSON file into memory.
     * @param jsonFilePath The path to your building_data.json file.
     */
    public UofTWalkTimeDAO(String jsonFilePath) throws IOException {
        this.client = HttpClient.newHttpClient();
        loadBuildingData("src/main/resources/BuildingCodes.json");
    }

    private void loadBuildingData(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        JSONArray data = new JSONArray(content);

        for (int i = 0; i < data.length(); i++) {
            JSONObject building = data.getJSONObject(i);
            String code = building.getString("code");
            double lat = building.getDouble("lat");
            double lon = building.getDouble("lon");

            // OSRM requires coordinates in [Longitude, Latitude] order
            buildingCoordinates.put(code, new double[]{lon, lat});
        }
    }

    @Override
    public int getWalkTime(String originCode, String destinationCode) throws IOException {
        // 1. Check if we know these buildings
        if (!buildingCoordinates.containsKey(originCode) || !buildingCoordinates.containsKey(destinationCode)) {
            return -1; // Return -1 to indicate "Building Unknown"
        }

        // 2. Get Coordinates
        double[] origin = buildingCoordinates.get(originCode);
        double[] dest = buildingCoordinates.get(destinationCode);

        // 3. Build OSRM API URL: /route/v1/foot/lon1,lat1;lon2,lat2
        String uriString = String.format(
                "http://router.project-osrm.org/route/v1/foot/%s,%s;%s,%s?overview=false",
                origin[0], origin[1], dest[0], dest[1]
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriString))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("API Request failed with status code: " + response.statusCode());
            }

            // 4. Parse Response
            JSONObject jsonResponse = new JSONObject(response.body());

            // OSRM returns duration in seconds (float). Cast to int.
            return (int) jsonResponse.getJSONArray("routes")
                    .getJSONObject(0).getFloat("duration");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("API call interrupted", e);
        } catch (Exception e) {
            // If the JSON parsing fails or network dies
            throw new IOException("Failed to calculate walk time", e);
        }
    }
}