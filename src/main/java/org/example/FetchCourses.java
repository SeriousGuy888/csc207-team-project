package org.example;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.time.Duration;

public class FetchCourses {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject payload = new JsonObject();
        JsonObject courseProps = new JsonObject();
        courseProps.addProperty("courseCode", "");
        courseProps.addProperty("courseTitle", "");
        courseProps.addProperty("courseSectionCode", "");
        payload.add("courseCodeAndTitleProps", courseProps);

        payload.add("departmentProps", new JsonArray());
        payload.add("campuses", new JsonArray());

        JsonArray sessions = new JsonArray();
        sessions.add("20259");
        sessions.add("20261");
        sessions.add("20259-20261");
        payload.add("sessions", sessions);

        payload.add("requirementProps", new JsonArray());
        payload.addProperty("instructor", "");
        payload.add("courseLevels", new JsonArray());
        payload.add("deliveryModes", new JsonArray());
        payload.add("dayPreferences", new JsonArray());
        payload.add("timePreferences", new JsonArray());

        JsonArray divisions = new JsonArray();
        String[] divs = new String[]{"APSC", "ARTSC", "FIS", "FPEH", "MUSIC", "ARCLA", "ERIN", "SCAR"};
        for (String d : divs) divisions.add(d);
        payload.add("divisions", divisions);

        payload.add("creditWeights", new JsonArray());
        payload.addProperty("availableSpace", false);
        payload.addProperty("waitListable", false);
        payload.addProperty("page", 1);
        payload.addProperty("pageSize", 9000);
        payload.addProperty("direction", "asc");

        String jsonPayload = gson.toJson(payload);

        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(Duration.ofSeconds(60))
                .build();

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonPayload, mediaType);

        // First send an OPTIONS request (useful for servers that expect an OPTIONS preflight)
        Request optionsRequest = new Request.Builder()
                .url("https://api.easi.utoronto.ca/ttb/getPageableCourses")
                .method("OPTIONS", null)
                // include common preflight headers (optional for server-side requests)
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "content-type")
                .build();

        try (Response optionsResponse = client.newCall(optionsRequest).execute()) {
            System.out.println("OPTIONS response: " + optionsResponse.code() + " " + optionsResponse.message());
            String allow = optionsResponse.header("Allow");
            if (allow != null) System.out.println("Allow: " + allow);
            // proceed with POST even if OPTIONS returns non-2xx; adapt logic as needed
        } catch (IOException e) {
            System.err.println("OPTIONS request failed: " + e.getMessage());
            // continue to POST or exit depending on your requirements
        }

        Request postRequest = new Request.Builder()
                .url("https://api.easi.utoronto.ca/ttb/getPageableCourses")
                .post(body)
                .build();

        try (Response response = client.newCall(postRequest).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Request failed: " + response.code() + " " + response.message());
                if (response.body() != null) System.err.println(response.body().string());
                System.exit(1);
            }

            String resp = response.body() != null ? response.body().string() : "";
            try {
                JsonElement je = JsonParser.parseString(resp);
                System.out.println(gson.toJson(je));
            } catch (Exception e) {
                // Not JSON; print raw
                System.out.println(resp);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
