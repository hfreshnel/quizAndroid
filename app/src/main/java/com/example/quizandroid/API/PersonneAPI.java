package com.example.quizandroid.API;

import android.util.Log;

import okhttp3.*;
import java.io.IOException;
import java.util.ArrayList;

import com.example.quizandroid.model.Personne;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class PersonneAPI {
    private static final String BASE_URL = "http://10.3.70.13:8080";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();



    public ArrayList<Personne> getAllPersonnes() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/admin/personnes")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(),
                        new TypeToken<ArrayList<Personne>>() {}.getType());
            } else {
                throw new IOException("Failed to fetch personnes: " + response.message());
            }
        }
    }

    public static Personne getPersonneById(String id) throws IOException {
        // Build the request
        String url = BASE_URL + "/public/personnes/" + id;
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjowLCJpYXQiOjE3MzY1MDM4NjEsImV4cCI6MTczNjU5MDI2MX0.ecvJwShazQBBNX8RsurDxp3NV5BZc2QXTkf6MhXLVXo")
                .get()
                .build();

        Log.d("PersonneAPI", "Request URL: " + url); // Log the URL being called

        try (Response response = client.newCall(request).execute()) {
            // Log response details
            Log.d("PersonneAPI", "Response Code: " + response.code());
            Log.d("PersonneAPI", "Response Message: " + response.message());

            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                Log.d("PersonneAPI", "Response Body: " + responseBody); // Log the response body

                // Parse the JSON response manually
                JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                JsonObject dataObject = jsonObject.getAsJsonObject("data"); // Extract the "data" object

                Personne personne = new Personne();

                if (dataObject != null) {
                    if (dataObject.has("nom")) personne.setNom(dataObject.get("nom").getAsString());
                    if (dataObject.has("prenom")) personne.setPrenom(dataObject.get("prenom").getAsString());
                }

                Log.d("PersonneAPI", personne.toString()); // Log the Personne object
                return personne;
            } else {
                // Log failure details
                Log.e("PersonneAPI", "Failed to fetch personne with ID " + id + ": " + response.message());
                throw new IOException("Failed to fetch personne with ID " + id + ": " + response.message());
            }
        }
    }



    public Personne updatePersonne(String id, Personne personne) throws IOException {
        RequestBody body = RequestBody.create(
                gson.toJson(personne),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/public/personnes/" + id)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return gson.fromJson(response.body().string(), Personne.class);
            } else {
                throw new IOException("Failed to update personne with ID " + id + ": " + response.message());
            }
        }
    }

    public void deletePersonne(String id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/admin/personnes/" + id)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to delete personne with ID " + id + ": " + response.message());
            }
        }
    }

}
