package com.example.quizandroid.API;

import android.util.Log;

import okhttp3.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.example.quizandroid.model.Personne;

import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionAPI {
    private static final String BASE_URL = "http://10.3.70.13:8080";
    private final OkHttpClient client;
    private final Gson gson;

    public ConnectionAPI() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public String registerUser(Personne personne) throws IOException {
        // Create a map with the fields
        Log.d("personne",personne.toString());
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("mail", personne.getMail());
        requestBodyMap.put("mdp", personne.getMdp());
        requestBodyMap.put("prenom", personne.getPrenom());
        requestBodyMap.put("nom", personne.getNom());

        // Convert the map to JSON
        String jsonBody = gson.toJson(requestBodyMap);

        // Debug: Log the request body
        Log.d("ConnectionAPI", "Request Body: " + jsonBody);

        // Create the request body
        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.get("application/json; charset=utf-8")
        );

        // Build and execute the request
        Request request = new Request.Builder()
                .url(BASE_URL + "/public/auth/register")
                .post(body)
                .build();

        // Debug: Log the request details
        Log.d("ConnectionAPI", "Sending request to URL: " + BASE_URL + "/public/auth/register");

        try (Response response = client.newCall(request).execute()) {
            // Debug: Log response details
            Log.d("ConnectionAPI", "Response Code: " + response.code());
            Log.d("ConnectionAPI", "Response Message: " + response.message());

            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                // Debug: Log response body
                Log.d("ConnectionAPI", "Response Body: " + responseBody);
                return responseBody; // Return server message
            } else {
                throw new IOException("Failed to register user. Code: " + response.code() + ", Message: " + response.message());
            }
        } catch (IOException e) {
            // Debug: Log the exception
            Log.e("ConnectionAPI", "Request failed", e);
            throw new IOException("Network error or invalid response", e);
        }
    }

    public String loginUser(String email, String password) throws IOException {
        Log.d("ConnectionAPI", "Attempting login with email: " + email);

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("email", email);
        requestBodyMap.put("password", password);

        // Convert the map to JSON
        String jsonBody = gson.toJson(requestBodyMap);

        // Debug: Log the request body
        Log.d("ConnectionAPI", "Request Body: " + jsonBody);

        // Create the request body
        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.get("application/json; charset=utf-8")
        );

        // Build and execute the request
        Request request = new Request.Builder()
                .url(BASE_URL + "/public/auth/login")
                .addHeader("Content-Type", "application/json") // Ensure the server recognizes JSON
                .post(body)
                .build();

        // Debug: Log the request details
        Log.d("ConnectionAPI", "Sending request to URL: " + BASE_URL + "/public/auth/login");

        try (Response response = client.newCall(request).execute()) {
            // Debug: Log response details
            Log.d("ConnectionAPI", "Response Code: " + response.code());
            Log.d("ConnectionAPI", "Response Message: " + response.message());

            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                // Debug: Log the complete response
                Log.d("ConnectionAPI", "Response Body: " + responseBody);

                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    String status = jsonResponse.optString("status", "error");

                    if ("success".equals(status)) {
                        // Handle successful login
                        // Example: Return Personne object or data from response
                        return jsonResponse.toString();
                    } else {
                        String message = jsonResponse.optString("message", "Unknown error occurred");
                        throw new IOException(message);
                    }
                } catch (JSONException e) {
                    Log.e("ConnectionAPI", "JSON Parsing error: ", e);
                    throw new IOException("Unexpected error parsing JSON response", e);
                }
            } else {
                throw new IOException("Failed to log in. Code: " + response.code() + ", Message: " + response.message());
            }
        } catch (IOException e) {
            // Debug: Log the exception
            Log.e("ConnectionAPI", "Request failed", e);
            throw new IOException("Network error or invalid response", e);
        }
    }




    public String logoutUser() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/public/auth/logout")
                .post(RequestBody.create(new byte[0], MediaType.get("application/json; charset=utf-8"))) // Body vacío
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string(); // Devuelve el mensaje del servidor
            } else {
                throw new IOException("Failed to logout user: " + response.message());
            }
        }
    }
}
