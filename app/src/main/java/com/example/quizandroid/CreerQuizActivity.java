package com.example.quizandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreerQuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_quiz);

        Button btnSauve = findViewById(R.id.save);
        Button btnQuestion = findViewById(R.id.question);
        EditText nom = findViewById(R.id.quiz_name);
        EditText questionCount = findViewById(R.id.question_count);

        // Sauvegarder le quiz et revenir à QuestionActivity
        btnSauve.setOnClickListener(v -> {
            String nomQuiz = nom.getText().toString();
            String nombreQuestionsStr = questionCount.getText().toString();

            // Validation des champs
            if (TextUtils.isEmpty(nomQuiz)) {
                Toast.makeText(this, "Veuillez entrer un nom pour le quiz.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(nombreQuestionsStr)) {
                Toast.makeText(this, "Veuillez entrer le nombre de questions.", Toast.LENGTH_SHORT).show();
                return;
            }

            int nombreQuestions;
            try {
                nombreQuestions = Integer.parseInt(nombreQuestionsStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Le nombre de questions doit être un entier.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Créer un Intent pour retourner les données à QuestionActivity
            Intent intent = new Intent();
            intent.putExtra("quiz_name", nomQuiz);
            intent.putExtra("question_count", nombreQuestions);

            // Définir le résultat et terminer cette activité
            setResult(RESULT_OK, intent);
            finish();
        });

        // Gestion du bouton Question
        btnQuestion.setOnClickListener(v -> {
            Toast.makeText(this, "Bouton Question cliqué", Toast.LENGTH_SHORT).show();
        });
    }
}
