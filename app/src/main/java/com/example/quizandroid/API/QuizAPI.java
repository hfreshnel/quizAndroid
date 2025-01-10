package com.example.quizandroid.API;

import android.util.Log;

import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuizAPI {
    private static final String BASE_URL = "http://10.3.70.13:8080";
    private static OkHttpClient client = new OkHttpClient();
    private static Gson gson = new Gson();

    public QuizAPI() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public static JsonObject getAllQuizzes(Context context) throws IOException {
        // Log the start of the method
        Log.d("QuizAPI", "Starting getAllQuizzes method.");

        // Retrieve the token from SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("QuizAppPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("authToken", null);

        if (token == null) {
            throw new IOException("Token not found. Please log in again.");
        }

        // Build the GET request
        Request request = new Request.Builder()
                .url(BASE_URL + "/public/quiz") // Replace with your actual API endpoint
                .get()
                .addHeader("Authorization", "Bearer " + token) // Add the token to the Authorization header
                .build();

        Log.d("QuizAPI", "Request built. URL: " + request.url());

        try (Response response = client.newCall(request).execute()) {
            // Log the response details
            Log.d("QuizAPI", "Response received. Code: " + response.code() + ", Message: " + response.message());

            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();

                // Log the response body for debugging
                Log.d("QuizAPI", "Response Body: " + responseBody);

                // Parse the JSON and return it
                return gson.fromJson(responseBody, JsonObject.class);
            } else {
                // Log failure details
                Log.e("QuizAPI", "Failed to retrieve quizzes. Code: " + response.code() + ", Message: " + response.message());
                throw new IOException("Failed to retrieve quizzes: " + response.message());
            }
        } catch (IOException e) {
            // Log the exception details
            Log.e("QuizAPI", "Exception occurred while fetching quizzes.", e);
            throw new IOException("Error during API call to get quizzes.", e);
        }
    }




    public JsonObject getQuizDetails() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/public/quiz/" )
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

    public static JsonObject createQuiz(JsonObject quiz) throws IOException {
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
    public static void startQuiz(String quizId) throws IOException {
        String url = BASE_URL + "/quizzes/start/" + quizId;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create("", MediaType.parse("application/json")))
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
    }

}
