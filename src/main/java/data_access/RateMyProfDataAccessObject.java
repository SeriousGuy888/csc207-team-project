package data_access;

import entity.Professor;
import use_case.ratemyprof.RateMyProfDataAccessInterface;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Class to retrieve RateMyProf data.
 */
public class RateMyProfDataAccessObject implements RateMyProfDataAccessInterface {

    private final OkHttpClient client = new OkHttpClient();
    private final String apiUrl = "https://www.ratemyprofessors.com/graphql";

    /**
     *
     * @param profFirstName the first name of the professor whose information is being searched for.
     * @param profLastName  the first name of the professor whose information is being searched for.
     * @return Professor object. If no professor is found, returns an empty professor object.
     */
    @Override
    public Professor getProfessorInfo(String profFirstName, String profLastName) throws RuntimeException {
        try {
            // Array of Strings for University of Toronto's school IDs on RateMyProf.
            // (Obtained through inspecting school search on RMP)
            // Order of IDs:
            // 1. University of Toronto,
            // 2. University of Toronto - St. George,
            // 3. University of Toronto - Mississauga,
            // 4. University of Toronto - Scarborough
            final String[] schoolIds = {"U2Nob29sLTEyMTg0", "U2Nob29sLTE0ODQ=", "U2Nob29sLTQ5Mjg=", "U2Nob29sLTQ5MTk="};

            // GraphQL query with variables
            JSONArray combinedEdges = new JSONArray();
            for (String schoolId : schoolIds) {
                String jsonBody = "{"
                        + "\"query\": \"query TeacherSearchResultsPageQuery($query: TeacherSearchQuery!) {"
                        + "search: newSearch { teachers(query: $query, first: 8) { edges { node { firstName lastName "
                        + "avgRating numRatings avgDifficulty department legacyId } } } } }\","
                        + "\"variables\": {"
                        + "\"query\": { \"text\": \"" + profFirstName + " " + profLastName + "\", \"schoolID\": \""
                        + schoolId + "\", \"fallback\": true, \"departmentID\": null }"
                        + "}"
                        + "}";

                // Build the request
                Request request = new Request.Builder()
                        .url(apiUrl)
                        .addHeader("Content-Type", "application/json")
                        .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                        .build();

                // Execute the request
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) {
                    throw new RuntimeException("RMP API error: " + response);
                }

                String json = response.body().string();
                JSONObject obj = new JSONObject(json);
                System.out.println(obj.toString());

                // Extract edges for this schoolId and add them to combinedEdges
                JSONArray edges = obj
                        .getJSONObject("data")
                        .getJSONObject("search")
                        .getJSONObject("teachers")
                        .getJSONArray("edges");

                for (int i = 0; i < edges.length(); i++) {
                    combinedEdges.put(edges.getJSONObject(i));
                }
            }

            if (combinedEdges.isEmpty()) {
                return Professor.emptyProfessor();
            }

            for (int i = 0; i < combinedEdges.length(); i++) {

                JSONObject teacherNode = combinedEdges.getJSONObject(i).getJSONObject("node");
                String firstName = teacherNode.getString("firstName");
                String lastName = teacherNode.getString("lastName");

                if (firstName.equalsIgnoreCase(profFirstName)
                        && lastName.equalsIgnoreCase(profLastName)) {
                    // Take the matching result and parses the data
                    String first = teacherNode.getString("firstName");
                    String last = teacherNode.getString("lastName");
                    double avgRating = teacherNode.optDouble("avgRating", -1);
                    double avgDifficulty = teacherNode.optDouble("avgDifficulty", -1);
                    int numRatings = teacherNode.optInt("numRatings", 0);
                    String department = teacherNode.has("department") ?
                            teacherNode.optString("department", "Unknown"): null;

                    int legacyId = teacherNode.getInt("legacyId");
                    String link = "https://www.ratemyprofessors.com/professor/" + legacyId;

                    // Return Professor object
                    return new Professor(first, last, avgRating, numRatings, avgDifficulty, department, link);
                }
            }

            return null;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
