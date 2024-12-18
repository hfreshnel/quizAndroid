package com.example.quizandroid.API;

import okhttp3.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.example.quizandroid.model.Personne;

public class ConnectionAPI {
    private static final String BASE_URL = "https://example.com";
    private final OkHttpClient client;
    private final Gson gson;

    public ConnectionAPI() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public String registerUser(Personne personne) throws IOException {
        // Crear un mapa con los campos relevantes
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("mail", personne.getMail());
        requestBodyMap.put("mdp", personne.getMdp());
        requestBodyMap.put("prenom", personne.getPrenom());
        requestBodyMap.put("nom", personne.getNom());

        // Convertir el mapa a JSON
        String jsonBody = gson.toJson(requestBodyMap);

        // Crear el cuerpo de la solicitud
        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.get("application/json; charset=utf-8")
        );

        // Construir y ejecutar la solicitud
        Request request = new Request.Builder()
                .url(BASE_URL + "/public/auth/register")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string(); // Devuelve el mensaje del servidor
            } else {
                throw new IOException("Failed to register user: " + response.message());
            }
        }
    }


    public Personne loginUser(String mail, String mdp) throws IOException {
        // Crear un JsonObject con mail y mdp
        JsonObject loginPayload = new JsonObject();
        loginPayload.addProperty("mail", mail);
        loginPayload.addProperty("mdp", mdp);

        RequestBody body = RequestBody.create(
                gson.toJson(loginPayload),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/public/auth/login")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                // Obtener el código de respuesta (por si es necesario manejarlo)
                int statusCode = response.code();
                if (statusCode == 200) { // Verificar si es OK
                    // Convertir la respuesta JSON en un objeto Personne
                    String responseBody = response.body().string();
                    return gson.fromJson(responseBody, Personne.class);
                } else {
                    throw new IOException("Login failed with status code: " + statusCode);
                }
            } else {
                throw new IOException("Failed to login user: " + response.message());
            }
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
