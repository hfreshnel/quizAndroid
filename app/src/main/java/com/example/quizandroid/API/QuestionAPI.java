package com.example.quizandroid.API;

import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;

public class QuestionAPI {
    private static final String BASE_URL = "http://10.3.70.13:8080";
    private final OkHttpClient client;
    private final Gson gson;

    public QuestionAPI() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    // GET: Retrieve a specific question by ID
    public JsonObject getQuestionById(long questionId) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/public/questions/" + questionId)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), JsonObject.class);
            } else {
                throw new IOException("Failed to retrieve question: " + response.message());
            }
        }
    }

    // POST: Create a new question
    public JsonObject createQuestion(JsonObject question) throws IOException {
        RequestBody body = RequestBody.create(
                gson.toJson(question),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/admin/questions")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), JsonObject.class);
            } else {
                throw new IOException("Failed to create question: " + response.message());
            }
        }
    }

    // PUT: Update a specific question by ID
    public JsonObject updateQuestion(long questionId, JsonObject question) throws IOException {
        RequestBody body = RequestBody.create(
                gson.toJson(question),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/admin/questions/" + questionId)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), JsonObject.class);
            } else {
                throw new IOException("Failed to update question: " + response.message());
            }
        }
    }

    // DELETE: Delete a specific question by ID
    public JsonObject deleteQuestion(long questionId) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/admin/questions/" + questionId)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), JsonObject.class);
            } else {
                throw new IOException("Failed to delete question: " + response.message());
            }
        }
    }
}
