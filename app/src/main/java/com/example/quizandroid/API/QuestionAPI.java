package com.example.quizandroid.API;

import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;

public class QuestionAPI {
    private static final String BASE_URL = "http://192.168.1.247:8080";
    private static OkHttpClient client = new OkHttpClient();
    private static Gson gson = new Gson();

    public QuestionAPI() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    /**
     * @param id the quiz id
     * @return all ids of questions related to the auiz
     * @throws IOException
     */
    public static int[] getAllQuestionsIds(long id) throws IOException {
//        Request request = new Request.Builder()
//                .url(BASE_URL + "/admin/quiz/questionsids" + id)
//                .get()
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (response.isSuccessful() && response.body() != null) {
//                return gson.fromJson(response.body().string(), JsonObject.class);
//            } else {
//                throw new IOException("Failed to retrieve questions ids: " + response.message());
//            }
//        }
        return new int[]{1,2,3,4,5,6};
    }

    public static JsonObject getQuestion(long id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL+"/public/questions/"+id)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), JsonObject.class);
            } else {
                throw new IOException("Failed to retrieve question: " + response.message());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
