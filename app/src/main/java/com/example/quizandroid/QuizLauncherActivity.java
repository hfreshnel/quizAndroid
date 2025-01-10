package com.example.quizandroid;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizandroid.API.QuizAPI;

import java.io.IOException;

public class QuizLauncherActivity extends AppCompatActivity {

    private TextView quizTitle, quizDescription;
    private Button startQuizButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_launcher);

        quizTitle = findViewById(R.id.quiz_title);
        quizDescription = findViewById(R.id.quiz_description);
        startQuizButton = findViewById(R.id.start_quiz_button);

        // Retrieve the quiz data from the Intent
        String title = getIntent().getStringExtra("quizTitle");
        String description = getIntent().getStringExtra("quizDescription");

        quizTitle.setText(title);
        quizDescription.setText(description);

        // Handle "Start this quiz" button
        startQuizButton.setOnClickListener(v -> startQuiz());
    }

    private void startQuiz() {
        // Call the backend to mark the quiz as started
        new Thread(() -> {
            try {
                QuizAPI.startQuiz(getIntent().getStringExtra("quizId"));
                runOnUiThread(() -> {
                    Toast.makeText(this, "Quiz started successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after starting the quiz
                });
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this, "Failed to start quiz: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
