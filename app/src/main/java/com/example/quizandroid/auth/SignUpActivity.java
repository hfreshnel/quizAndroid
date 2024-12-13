package com.example.quizandroid.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizandroid.R;
import com.example.quizandroid.model.Personne;
import com.example.quizandroid.API.ConnectionAPI;
import com.example.quizandroid.participant.ParticipantListQuizActivity;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button signUpButton;
    private TextView linkToLogin;
    private ConnectionAPI connectionAPI;
    private  EditText inputNom,inputPrenom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activiy);

        inputEmail = findViewById(R.id.input_email);
        inputNom= findViewById(R.id.input_first_name);
        inputPrenom= findViewById(R.id.input_name);


        inputPassword = findViewById(R.id.input_password);
        signUpButton = findViewById(R.id.register_button);
        linkToLogin = findViewById(R.id.link_to_login);

        connectionAPI = new ConnectionAPI();

        linkToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signUpButton.setOnClickListener(v -> attemptSignUp());
    }

    private void attemptSignUp() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String nom = inputNom.getText().toString().trim();
        String prenom = inputPrenom.getText().toString().trim();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("SignUpActivity", "Attempting to sign up with email: " + email);

        // Create Personne object
        Personne personne = new Personne();
        personne.setMail(email);
        personne.setNom(nom);
        personne.setPrenom(prenom);
        personne.setMdp(password);

        // Make the API call using ConnectionAPI
        new Thread(() -> {
            try {
                String response = connectionAPI.registerUser(personne);
                Log.d("SignUpActivity", "API Response: " + response);

                runOnUiThread(() -> {
                    Toast.makeText(SignUpActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, ParticipantListQuizActivity.class);
                    startActivity(intent);
                    finish();
                });
            } catch (IOException e) {
                Log.e("SignUpActivity", "API call failed", e);

                runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
