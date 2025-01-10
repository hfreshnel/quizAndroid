package com.example.quizandroid.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizandroid.API.ConnectionAPI;
import com.example.quizandroid.GetQuizForAdminActivity;
import com.example.quizandroid.R;
import com.example.quizandroid.admin.AdminQuizQuestionActivity;
import com.example.quizandroid.model.Personne;
import com.example.quizandroid.participant.ParticipantListQuizActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button loginButton;
    private TextView linkToRegister;

    private ConnectionAPI connectionAPI; // API instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.login_button);
        linkToRegister = findViewById(R.id.link_to_register);

        connectionAPI = new ConnectionAPI();

        loginButton.setOnClickListener(v -> attemptLogin());

        // Navigate to the SignUpActivity
        linkToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                // Call the API to log in the user
                String response = connectionAPI.loginUser(email, password);

                // Log the raw response for debugging
                Log.d("LoginActivity", "Response: " + response);

                // Parse the response
                JSONObject jsonResponse = new JSONObject(response);
                int code = jsonResponse.optInt("code", -1);
                String error = jsonResponse.optString("error", "Erreur inconnue");

                // Handle the response on the UI thread
                runOnUiThread(() -> {
                    if (code == 200) { // Successful response
                        Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                        try {
                            int role = jsonResponse.optInt("role", -1);
                            String token = jsonResponse.optString("token", null);

                            if (token != null) {
                                Log.d("LoginActivity", "Token: " + token); // Log the token for debugging
                            }

                            // Redirect based on the user's role
                            Intent intent;
                            if (role == 1000) { // Admin role
                                intent = new Intent(LoginActivity.this, AdminQuizQuestionActivity.class);
                            } else { // Simple user role
                                intent = new Intent(LoginActivity.this, ParticipantListQuizActivity.class);
                            }
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            Log.e("LoginActivity", "Error processing response", e);
                            Toast.makeText(LoginActivity.this, "Erreur lors de la connexion", Toast.LENGTH_SHORT).show();
                        }
                    } else { // Failed response
                        Toast.makeText(LoginActivity.this, "Erreur : " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (JSONException e) {
                Log.e("LoginActivity", "JSON Parsing Error", e);
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erreur JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                Log.e("LoginActivity", "Network Error", e);
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erreur réseau: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }


}