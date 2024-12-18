package com.example.quizandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizandroid.auth.LoginActivity;
import com.example.quizandroid.auth.SignUpActivity;
import com.example.quizandroid.model.Personne;

public class GetStartedActivity extends AppCompatActivity {
    private Button getStartedButton;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        // Initialize UI elements
        getStartedButton = findViewById(R.id.get_started_button);
        loginLink = findViewById(R.id.login_link);

        // Set click listener for "Commencer" button
        getStartedButton.setOnClickListener(v -> {
            // Navigate to LoginActivity
            Intent intent = new Intent(GetStartedActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Set click listener for "Vous avez déjà un compte ?" text
        loginLink.setOnClickListener(v -> {
            // Navigate to SignUpActivity
            Intent intent = new Intent(GetStartedActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}