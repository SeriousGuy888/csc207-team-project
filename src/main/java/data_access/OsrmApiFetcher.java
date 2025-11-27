package data_access;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class OsrmApiFetcher {
    private final OkHttpClient client;
    private static final String OSRM_BASE_URL = "http://router.project-osrm.org/route/v1/foot";

    public OsrmApiFetcher() {
        this.client = new OkHttpClient();
    }

    public int fetchDuration(double startLon, double startLat, double endLon, double endLat)
            throws IOException {
        String coordinates = startLon + "," + startLat + ";" + endLon + "," + endLat;
        String url = OSRM_BASE_URL + "/" + coordinates + "?overview=false";

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("OSRM API Error: " + response.code());
            }

            if (response.body() == null) {
                throw new IOException("API returned empty body");
            }

            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);

            if (!json.getString("code").equals("Ok")) {
                throw new IOException("OSRM Route Error: " + json.getString("code"));
            }

            JSONArray routes = json.getJSONArray("routes");
            if (routes.isEmpty()) {
                throw new IOException("No route found between points.");
            }

            double duration = routes.getJSONObject(0).getDouble("duration");
            return (int) Math.round(duration);
        }
    }
}