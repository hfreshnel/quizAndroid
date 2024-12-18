package com.example.quizandroid.API;

import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;

public class QuestionAPI {
    private static final String BASE_URL = "https://example.com";
    private final OkHttpClient client;
    private final Gson gson;

    public QuestionAPI() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public JsonObject getAllQuizzes(long id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/admin/quiz/" + id)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), JsonObject.class);
            } else {
                throw new IOException("Failed to retrieve quizzes: " + response.message());
            }
        }
    }

    public JsonObject getQuizDetails(long id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/public/quiz/" + id)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), JsonObject.class);
            } else {
                throw new IOException("Failed to retrieve quiz details: " + response.message());
            }
        }
    }

    public JsonObject createQuiz(JsonObject quiz) throws IOException {
        RequestBody body = RequestBody.create(
                gson.toJson(quiz),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/admin/quiz")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), JsonObject.class);
            } else {
                throw new IOException("Failed to create quiz: " + response.message());
            }
        }
    }

    public JsonObject updateQuiz(long id, JsonObject quiz) throws IOException {
        RequestBody body = RequestBody.create(
                gson.toJson(quiz),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/admin/quiz/" + id)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), JsonObject.class);
            } else {
                throw new IOException("Failed to update quiz: " + response.message());
            }
        }
    }

    public JsonObject deleteQuiz(long id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/admin/quiz/" + id)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), JsonObject.class);
            } else {
                throw new IOException("Failed to delete quiz: " + response.message());
            }
        }
    }
}
