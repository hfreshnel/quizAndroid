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
                String response = connectionAPI.loginUser(email, password);

                // Parse the response
                JSONObject jsonResponse = new JSONObject(response);
                int code = jsonResponse.getInt("code");
                String error = jsonResponse.isNull("error") ? "Erreur inconnue" : jsonResponse.optString("error");

                if (code == 200) {
                    JSONObject personData = jsonResponse.getJSONObject("data");
                    String mail = personData.optString("mail", "Email non fourni");
                    int role = personData.optInt("role", -1);

                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Connexion réussie : " + mail, Toast.LENGTH_SHORT).show();

                        Intent intent = (role == 1000)
                                ? new Intent(LoginActivity.this, GetQuizForAdminActivity.class)
                                : new Intent(LoginActivity.this, ParticipantListQuizActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erreur réseau: " + e.getMessage(), Toast.LENGTH_LONG).show());
                Log.e("LoginActivity", "Network error", e);
            } catch (JSONException e) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erreur JSON: " + e.getMessage(), Toast.LENGTH_LONG).show());
                Log.e("LoginActivity", "JSON parsing error", e);
            }
        }).start();
    }

}

