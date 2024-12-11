package com.example.quizandroid.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizandroid.R;
import com.example.quizandroid.participant.ParticipantListQuizActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button loginButton;
    private TextView linkToRegister;

    // Static JSON data for testing
    private static final String MOCK_USER_EMAIL = "testuser@example.com";
    private static final String MOCK_USER_PASSWORD = "password123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.login_button);
        linkToRegister = findViewById(R.id.link_to_register);

        loginButton.setOnClickListener(v -> attemptLogin());

        // Set OnClickListener for "Créer maintenant" link
        linkToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulate a login attempt with static JSON data
        JSONObject jsonResponse = simulateLoginRequest(email, password);

        // Handle the simulated response
        try {
            String status = jsonResponse.getString("status");
            if ("success".equals(status)) {
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                    // Redirect to ParticipantListQuizActivity
                    Intent intent = new Intent(LoginActivity.this, ParticipantListQuizActivity.class);
                    startActivity(intent);
                    finish(); // Optional: Prevent going back to login
                });
            } else {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show());
            }
        } catch (JSONException e) {
            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erreur lors du traitement de la réponse", Toast.LENGTH_SHORT).show());
        }
    }

    // Simulate a login request with static data
    private JSONObject simulateLoginRequest(String email, String password) {
        JSONObject jsonResponse = new JSONObject();
        try {
            // Simulate a successful login if email and password match the static values
            if (MOCK_USER_EMAIL.equals(email) && MOCK_USER_PASSWORD.equals(password)) {
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Login successful");
                jsonResponse.put("user", new JSONObject().put("email", email));
            } else {
                jsonResponse.put("status", "failure");
                jsonResponse.put("message", "Invalid email or password");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
}

