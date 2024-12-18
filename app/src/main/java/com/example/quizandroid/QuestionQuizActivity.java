package com.example.quizandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizandroid.admin.VoirCreerQuestionActivity;

public class QuestionQuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Button btnAddQuestion = findViewById(R.id.btn_add_question);

        btnAddQuestion.setOnClickListener(v -> {
            Intent intent = new Intent(QuestionQuizActivity.this, VoirCreerQuestionActivity.class);
            startActivity(intent);
        });

        setContentView(R.layout.activity_question_quiz);



    }
}