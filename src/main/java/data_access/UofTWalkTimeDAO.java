package data_access;

import org.json.JSONArray;
import org.json.JSONObject;
import use_case.WalkTimeDataAccessInterface;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object for retrieving walking times between UofT buildings.
 * Uses OSRM (Open Source Routing Machine) API for route calculations.
 */
public class UofTWalkTimeDAO implements WalkTimeDataAccessInterface {

    private static final String OSRM_BASE_URL = "http://router.project-osrm.org/route/v1/foot";
    private static final String DEFAULT_JSON_PATH = "src/main/resources/BuildingCodes.json";

    private final Map<String, double[]> buildingCoordinates = new HashMap<>();
    private final HttpClient client;

    /**
     * Constructor that loads building data from the default JSON file.
     */
    public UofTWalkTimeDAO() throws IOException {
        this(DEFAULT_JSON_PATH);
    }

    /**
     * Constructor that allows specifying a custom JSON file path.
     * Useful for testing.
     */
    public UofTWalkTimeDAO(String jsonFilePath) throws IOException {
        this.client = HttpClient.newHttpClient();
        loadBuildingData(jsonFilePath);
    }

    /**
     * Loads building coordinate data from JSON file into memory.
     */
    private void loadBuildingData(String path) throws IOException {
        String content = Files.readString(Paths.get(path));
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
        return getWalkTimeInSeconds(originCode, destinationCode);
    }

    @Override
    public int getWalkTimeInSeconds(String originCode, String destinationCode) throws IOException {
        // Validate building codes exist
        validateBuildingCode(originCode);
        validateBuildingCode(destinationCode);

        // Get coordinates
        double[] origin = buildingCoordinates.get(originCode);
        double[] dest = buildingCoordinates.get(destinationCode);

        // Query OSRM API
        String routeResponse = queryOSRMAPI(origin, dest);

        // Parse and return duration
        return parseDurationFromResponse(routeResponse);
    }

    /**
     * Validates that a building code exists in our database.
     * @throws IllegalArgumentException if building code is unknown
     */
    private void validateBuildingCode(String buildingCode) {
        if (!buildingCoordinates.containsKey(buildingCode)) {
            throw new IllegalArgumentException("Unknown building code!");
        }
    }

    /**
     * Queries the OSRM API for walking route between two coordinates.
     * @return JSON response as string
     */
    private String queryOSRMAPI(double[] origin, double[] dest) throws IOException {
        String uriString = String.format(
                "%s/%s,%s;%s,%s?overview=false",
                OSRM_BASE_URL,
                origin[0], origin[1],
                dest[0], dest[1]
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriString))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("OSRM API request failed with status code: " + response.statusCode());
            }

            return response.body();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("API call interrupted", e);
        }
    }

    /**
     * Parses the walking duration from OSRM API JSON response.
     * @return duration in seconds
     */
    private int parseDurationFromResponse(String jsonResponse) throws IOException {
        try {
            JSONObject response = new JSONObject(jsonResponse);

            if (!response.has("routes") || response.getJSONArray("routes").isEmpty()) {
                throw new IOException("No route found in OSRM response");
            }

            double duration = response.getJSONArray("routes")
                    .getJSONObject(0)
                    .getDouble("duration");

            return (int) Math.round(duration);

        } catch (Exception e) {
            throw new IOException("Failed to parse OSRM response: " + e.getMessage(), e);
        }
    }
}