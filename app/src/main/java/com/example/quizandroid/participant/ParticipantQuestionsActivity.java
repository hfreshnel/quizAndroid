package com.example.quizandroid.participant;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizandroid.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ParticipantQuestionsActivity extends AppCompatActivity {

    private String correctAnswer;
    private Button selectedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_quiz);

        // Chaîne JSON pour les tests
        String jsonData = "{ \"question\": \"Quelle est la capitale de la France ?\", " +
                "\"options\": [\"Londres\", \"Berlin\", \"Paris\", \"Madrid\"], " +
                "\"correctAnswer\": \"Paris\" }";

        try {
            // Analyser les données JSON
            JSONObject jsonObject = new JSONObject(jsonData);
            String question = jsonObject.getString("question");
            correctAnswer = jsonObject.getString("correctAnswer");

            // Set question et options
            TextView questionView = findViewById(R.id.question);
            questionView.setText(question);

            Button button1 = findViewById(R.id.button1);
            Button button2 = findViewById(R.id.button2);
            Button button3 = findViewById(R.id.button3);
            Button button4 = findViewById(R.id.button4);
            Button validerButton = findViewById(R.id.validerButton);

            button1.setText(jsonObject.getJSONArray("options").getString(0));
            button2.setText(jsonObject.getJSONArray("options").getString(1));
            button3.setText(jsonObject.getJSONArray("options").getString(2));
            button4.setText(jsonObject.getJSONArray("options").getString(3));

            // Set click listeners
            button1.setOnClickListener(v -> selectAnswer(button1));
            button2.setOnClickListener(v -> selectAnswer(button2));
            button3.setOnClickListener(v -> selectAnswer(button3));
            button4.setOnClickListener(v -> selectAnswer(button4));

            // Set click listener pour le bouton valider
            validerButton.setOnClickListener(v -> validateAnswer());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Suivre le bouton sélectionné
    private void selectAnswer(Button button) {
        if (selectedButton != null) {
            // Réinitialise la couleur de fond du bouton précédemment sélectionné
            selectedButton.setBackgroundColor(Color.WHITE);
        }
        selectedButton = button; // Met à jour le bouton sélectionné
        selectedButton.setBackgroundColor(Color.parseColor("#b4d8fc"));
    }

    // Valide la réponse sélectionnée
    private void validateAnswer() {
        if (selectedButton != null) {
            if (selectedButton.getText().toString().equals(correctAnswer)) {
                selectedButton.setBackgroundColor(Color.parseColor("#A8D694")); // Vert pour correct
            } else {
                selectedButton.setBackgroundColor(Color.parseColor("#F18E8E")); // Rouge pour incorrect
            }
        }
    }
}
