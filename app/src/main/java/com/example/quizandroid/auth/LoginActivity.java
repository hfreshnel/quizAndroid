package com.example.quizandroid.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizandroid.R;
import com.example.quizandroid.participant.UserMainActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make the network request (using OkHttp here)
        sendLoginRequest(email, password);
    }

    private void sendLoginRequest(String email, String password) {
        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url("http://api-url/public/auth/login")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // Mock response (replace with actual backend response once available)
                        String mockResponse = "{ \"status\": \"success\", \"message\": \"Login successful\", \"user\": { \"id\": 1, \"name\": \"Seifbh\", \"email\": \"" + email + "\" } }";
                        JSONObject jsonResponse = new JSONObject(mockResponse);

                        // Handle successful login response
                        String status = jsonResponse.getString("status");
                        if ("success".equals(status)) {
                            runOnUiThread(() -> {
                                Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                                // Redirect to UserMainActivity
                                Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                                startActivity(intent);
                                finish();  // Optional: Finish current activity to prevent going back to login
                            });
                        } else {
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show());
                        }

                    } catch (JSONException e) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erreur lors du traitement de la réponse", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
