package com.example.quizandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class CreerQuizActivity extends AppCompatActivity {

    private EditText quizName;
    private Button btnAddQuestion, btnTerminer;
    private LinearLayout questionContainer;
    private List<EditText> questionFields = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_quiz);

        // Initialize Views
        quizName = findViewById(R.id.quiz_name);
        btnAddQuestion = findViewById(R.id.add_question);
        btnTerminer = findViewById(R.id.terminer);
        questionContainer = findViewById(R.id.question_container);

        // Add Initial Question Field
        addQuestionField();

        // Add Question Button Listener
        btnAddQuestion.setOnClickListener(v -> addQuestionField());

        // Terminer Button Listener
        btnTerminer.setOnClickListener(v -> finalizeQuiz());
    }

    private void addQuestionField() {
        // Create a new EditText for the question
        EditText newQuestionField = new EditText(this);

        // Apply the same style as the "Nom du Quiz" field
        newQuestionField.setHint("Entrez une question");
        newQuestionField.setPadding(16, 16, 16, 16);
        newQuestionField.setBackgroundResource(R.drawable.rounded_edittext); // Set the same background
        newQuestionField.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newQuestionField.setInputType(android.text.InputType.TYPE_CLASS_TEXT); // Same input type

        // Add a margin for spacing between questions
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) newQuestionField.getLayoutParams();
        params.setMargins(0, 16, 0, 0); // Add top margin
        newQuestionField.setLayoutParams(params);

        // Add the EditText to the container
        questionContainer.addView(newQuestionField);
        questionFields.add(newQuestionField); // Keep track of dynamically added fields
    }


    private void finalizeQuiz() {
        String quizTitle = quizName.getText().toString().trim();

        // Validate quiz name
        if (quizTitle.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer le nom du quiz.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Collect questions
        JsonArray questionsArray = new JsonArray();
        for (EditText questionField : questionFields) {
            String questionText = questionField.getText().toString().trim();
            if (!questionText.isEmpty()) {
                questionsArray.add(questionText);
            }
        }

        // Validate questions
        if (questionsArray.size() == 0) {
            Toast.makeText(this, "Veuillez ajouter au moins une question.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare request payload
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("libelle", quizTitle);
        requestBody.add("questions", questionsArray);

        // Log the JSON for debugging
        Log.d("CreerQuizActivity", "Request Body: " + requestBody.toString());

        // Send to API (for demonstration purposes, just showing a success message)
        Toast.makeText(this, "Quiz créé avec succès!", Toast.LENGTH_SHORT).show();
    }
}
