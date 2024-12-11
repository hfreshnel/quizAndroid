package com.example.quizandroid.API;

import okhttp3.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

public class StatAPI {
    private static final String BASE_URL = "https://example.com";
    private final OkHttpClient client;
    private final Gson gson;

    public StatAPI() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    // GET /public/quiz/{quizId}/stats
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

    // GET /public/quiz/{quizId}/classement
    public List<Object> getQuizRanking(Long quizId, Long questionId) throws IOException {
        if (quizId == null || questionId == null || quizId <= 0 || questionId <= 0) {
            throw new IllegalArgumentException("Invalid quizId or questionId");
        }

        String url = String.format(BASE_URL + "/public/quiz/%d/classement?questionId=%d", quizId, questionId);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), new TypeToken<List<Object>>() {}.getType());
            } else {
                throw new IOException("Failed to fetch ranking: " + response.message());
            }
        }
    }

    // POST /public/quiz/{quizId}/choix
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
