package com.example.quizandroid.API;

import okhttp3.*;
import com.google.gson.*;
import com.example.quizandroid.model.Proposition;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

public class PropositionAPI {
    private static final String BASE_URL = "https://example.com";
    private final OkHttpClient client;
    private final Gson gson;

    public PropositionAPI() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    // GET /public/propositions
    public List<Proposition> getAllPropositions() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/public/propositions")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), new TypeToken<List<Proposition>>() {}.getType());
            } else {
                throw new IOException("Failed to fetch propositions: " + response.message());
            }
        }
    }

    // GET /public/propositions/{id}
    public Proposition getPropositionById(Long id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/public/propositions/" + id)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), Proposition.class);
            } else {
                throw new IOException("Failed to fetch proposition: " + response.message());
            }
        }
    }

    // POST /admin/propositions
    public Proposition addProposition(Proposition proposition) throws IOException {
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("libelle", proposition.getLibelle());
        jsonBody.addProperty("correct", proposition.getCorrect());

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/admin/propositions")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), Proposition.class);
            } else {
                throw new IOException("Failed to add proposition: " + response.message());
            }
        }
    }

    // PUT /admin/propositions/{id}
    public Proposition updateProposition(Long id, Proposition proposition) throws IOException {
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("libelle", proposition.getLibelle());
        jsonBody.addProperty("correct", proposition.getCorrect());

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/admin/propositions/" + id)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), Proposition.class);
            } else {
                throw new IOException("Failed to update proposition: " + response.message());
            }
        }
    }

    // DELETE /admin/propositions/{id}
    public String deleteProposition(Long id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/admin/propositions/" + id)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string(); // Confirmation message
            } else {
                throw new IOException("Failed to delete proposition: " + response.message());
            }
        }
    }

    // GET /public/questions/{questionid}/propositions
    public List<Proposition> getPropositionsByQuestionId(Long questionId) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/public/questions/" + questionId + "/propositions")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), new TypeToken<List<Proposition>>() {}.getType());
            } else {
                throw new IOException("Failed to fetch propositions for question: " + response.message());
            }
        }
    }
}
