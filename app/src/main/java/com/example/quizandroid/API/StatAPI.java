package com.example.quizandroid.API;

import static com.example.quizandroid.auth.LoginActivity.token;

import android.util.Log;

import okhttp3.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;


public class StatAPI {
    private static final String BASE_URL = "http://10.3.70.13:8080";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();



    // GET /public/quiz/{quiz_id}/classement
    // GET /public/quiz/{quiz_id}/stats
    public static JsonObject getQuizRanking(Long quizId) throws IOException {
        if (quizId == null || quizId <= 0) {
            throw new IllegalArgumentException("Invalid quizId");
        }

        // Build the request URL
        String url = String.format(BASE_URL + "/public/quiz/%d/classement", quizId);
        Log.d("StatAPI", "Request URL: " + url); // Log the URL being called

        // Build the GET request
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + token)
                .get()
                .build();

        Log.d("StatAPI", "Request built. URL: " + request.url());

        // Execute the request and handle the response
        try (Response response = client.newCall(request).execute()) {
            // Log the response details
            Log.d("StatAPI", "Response received. Code: " + response.code() + ", Message: " + response.message());

            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();

                // Log the response body for debugging
                Log.d("StatAPI", "Response Body: " + responseBody);

                // Parse the JSON and return it
                return gson.fromJson(responseBody, JsonObject.class);
            } else {
                // Log failure details
                Log.e("StatAPI", "Failed to retrieve classement. Code: " + response.code() + ", Message: " + response.message());
                throw new IOException("Failed to retrieve classement: " + response.message());
            }
        } catch (IOException e) {
            // Log the exception details
            Log.e("StatAPI", "Exception occurred while fetching classement.", e);
            throw new IOException("Error during API call to get classement.", e);
        }
    }


    // GET /public/quiz/{quiz_id}/stats
    public List<Object> getQuestionStats(Long quizId, Long questionId) throws IOException {
        if (quizId == null || questionId == null || quizId <= 0 || questionId <= 0) {
            throw new IllegalArgumentException("Invalid quizId or questionId");
        }

        String url = String.format(BASE_URL + "/public/quiz/%d/stats?questionId=%d", quizId, questionId);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), new TypeToken<List<Object>>() {}.getType());
            } else {
                throw new IOException("Failed to fetch stats: " + response.message());
            }
        }
    }

    // POST /public/quiz/{quiz_id}/choix
    public String submitChoice(Long quizId, Long personId, Long propositionId) throws IOException {
        if (quizId == null || personId == null || propositionId == null ||
                quizId <= 0 || personId <= 0 || propositionId <= 0) {
            throw new IllegalArgumentException("Invalid IDs provided");
        }

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("quizId", quizId);
        jsonBody.addProperty("personId", personId);
        jsonBody.addProperty("propositionId", propositionId);

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/public/quiz/" + quizId + "/choix")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string(); // Server confirmation message
            } else {
                throw new IOException("Failed to submit choice: " + response.message());
            }
        }
    }
}
