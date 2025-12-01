package data_access;

import org.json.JSONArray;
import org.json.JSONObject;
import use_case.osrm_walktime.WalkTimeDataAccessInterface;
import use_case.osrm_walktime.WalkTimeService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class UofTWalkTimeDataAccessObject implements WalkTimeDataAccessInterface, WalkTimeService {

    private static final String BUILDING_FILE_PATH = "src/main/resources/BuildingCodes.json";

    // Maps "BA" -> [-79.39, 43.65] (Longitude, Latitude)
    private final Map<String, double[]> buildingCoordinates = new HashMap<>();

    private final OsrmApiFetcher apiFetcher;

    /**
     * Constructor.
     * Loads the JSON file immediately so we don't read from disk on every click.
     * @param apiFetcher The network tool we built earlier.
     */
    public UofTWalkTimeDataAccessObject(OsrmApiFetcher apiFetcher) throws IOException {
        this.apiFetcher = apiFetcher;
        loadBuildingData();
    }

    /**
     * Private helper to read the JSON file once at startup.
     */
    private void loadBuildingData() throws IOException {
        String content = Files.readString(Paths.get(BUILDING_FILE_PATH));
        JSONArray data = new JSONArray(content);

        for (int i = 0; i < data.length(); i++) {
            JSONObject building = data.getJSONObject(i);

            String code = building.getString("code");
            double lat = building.getDouble("lat");
            double lon = building.getDouble("lon");

            buildingCoordinates.put(code, new double[]{lon, lat});
        }
    }

    @Override
    public int getWalkTimeInSeconds(String originCode, String destinationCode)
            throws InvalidLocationException, ApiConnectionException {

        if (!buildingCoordinates.containsKey(originCode)) {
            throw new InvalidLocationException(originCode);
        }
        if (!buildingCoordinates.containsKey(destinationCode)) {
            throw new InvalidLocationException(destinationCode);
        }

        double[] start = buildingCoordinates.get(originCode);
        double[] end = buildingCoordinates.get(destinationCode);

        try {
            return apiFetcher.fetchDuration(start[0], start[1], end[0], end[1]);
        }
        catch (IOException e) {
            throw new ApiConnectionException("Failed to reach OSRM: " + e.getMessage());
        }
    }

    @Override
    public int calculateWalkingTime(String startCode, String endCode) {
        try {
            // Reuse the existing logic
            return getWalkTimeInSeconds(startCode, endCode);
        }
        catch (InvalidLocationException | ApiConnectionException e) {
            // Swallow exception and return specific failure code
            // Presenter will see -1 and just not print the label.
            System.err.println("Walking Service Error: " + e.getMessage());
            return -1;
        }
    }
}