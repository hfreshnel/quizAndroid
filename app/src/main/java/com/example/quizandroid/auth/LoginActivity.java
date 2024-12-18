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

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform login in a background thread
        new Thread(() -> {
            try {
                // Call the API to log in the user
                String response = connectionAPI.loginUser(email, password);

                // Parse the response
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");

                // Handle the response on the UI thread
                runOnUiThread(() -> {
                    if ("success".equals(status)) {
                        Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();

                        // Extract user details
                        try {
                            JSONObject userJson = jsonResponse.getJSONObject("user");
                            int role = userJson.getInt("role");

                            // Redirect based on the user's role
                            Intent intent;
                            if (role == 1000) {
                                intent = new Intent(LoginActivity.this, AdminQuizQuestionActivity.class);
                            } else {
                                intent = new Intent(LoginActivity.this, ParticipantListQuizActivity.class);
                            }
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            Log.e("LoginActivity", "Error parsing user details", e);
                            Toast.makeText(LoginActivity.this, "Erreur lors de la connexion", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // This block is unlikely to be reached with a 200 status code
                        Toast.makeText(LoginActivity.this, "Échec de la connexion", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show());
                Log.e("LoginActivity", "Network error", e);
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erreur inattendue", Toast.LENGTH_SHORT).show());
                Log.e("LoginActivity", "Unexpected error", e);
            }
        }).start();
    }
}
