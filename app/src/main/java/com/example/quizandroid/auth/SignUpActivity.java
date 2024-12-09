package com.example.quizandroid.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizandroid.R;
import com.example.quizandroid.participant.ParticipantListQuizActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button signUpButton;

    // Static data for testing (e.g., mock email already exists)
    private static final String MOCK_EXISTING_EMAIL = "testuser@example.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activiy);

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        signUpButton = findViewById(R.id.register_button);

        signUpButton.setOnClickListener(v -> attemptSignUp());
    }

    private void attemptSignUp() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulate a sign-up attempt with static data
        JSONObject jsonResponse = simulateSignUpRequest(email, password);

        // Handle the simulated response
        try {
            String status = jsonResponse.getString("status");
            if ("success".equals(status)) {
                runOnUiThread(() -> {
                    Toast.makeText(SignUpActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    // Redirect to UserMainActivity
                    Intent intent = new Intent(SignUpActivity.this, ParticipantListQuizActivity.class);
                    startActivity(intent);
                    finish();  // Optional: Finish current activity to prevent going back to sign-up
                });
            } else {
                runOnUiThread(() -> {
                    try {
                        Toast.makeText(SignUpActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (JSONException e) {
            runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Erreur lors du traitement de la réponse", Toast.LENGTH_SHORT).show());
        }
    }

    // Simulate a sign-up request with static data
    private JSONObject simulateSignUpRequest(String email, String password) {
        JSONObject jsonResponse = new JSONObject();
        try {
            // Simulate a failure if the email is already in use
            if (MOCK_EXISTING_EMAIL.equals(email)) {
                jsonResponse.put("status", "failure");
                jsonResponse.put("message", "Cet email est déjà utilisé");
            } else {
                // Simulate successful sign-up
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Inscription réussie");
                jsonResponse.put("user", new JSONObject().put("email", email));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
}