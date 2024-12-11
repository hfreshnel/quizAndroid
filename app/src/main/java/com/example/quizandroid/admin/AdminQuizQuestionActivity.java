package com.example.quizandroid.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizandroid.R;
import com.example.quizandroid.participant.ParticipantListQuizActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminQuizQuestionActivity extends AppCompatActivity {

    private int currentQuestionIndex = 0;
    private ArrayList<JSONObject> questionsList;

    // Dynamic click counters for options
    private int[] optionClicks = new int[4]; // [option1, option2, option3, option4]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz_question);

        // Handle ImageView click for terminerQuiz
        findViewById(R.id.terminerQuiz).setOnClickListener(v -> {
            // Create an Intent to navigate to ParticipantListQuizActivity
            Intent intent = new Intent(AdminQuizQuestionActivity.this, ParticipantListQuizActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.frameLayout2).setOnClickListener(v -> {
            // Create an Intent to navigate to ParticipantListQuizActivity
            Intent intent = new Intent(AdminQuizQuestionActivity.this, AdminClassementActivity.class);
            startActivity(intent);
        });

        TextView percentage1 = findViewById(R.id.percentage1);
        TextView percentage2 = findViewById(R.id.percentage2);
        TextView percentage3 = findViewById(R.id.percentage3);
        TextView percentage4 = findViewById(R.id.percentage4);
        TextView nQuestion = findViewById(R.id.nQuestion);
        TextView questionText = findViewById(R.id.question);

        percentage1.setVisibility(View.GONE);
        percentage2.setVisibility(View.GONE);
        percentage3.setVisibility(View.GONE);
        percentage4.setVisibility(View.GONE);

        questionsList = createSampleQuestions();

        updateQuestion(nQuestion, questionText);

        final boolean[] isPercentagesVisible = {false};

        // Handle percentage visibility
        findViewById(R.id.frameLayout).setOnClickListener(v -> {
            if (isPercentagesVisible[0]) {
                percentage1.setVisibility(View.GONE);
                percentage2.setVisibility(View.GONE);
                percentage3.setVisibility(View.GONE);
                percentage4.setVisibility(View.GONE);
            } else {
                JSONObject currentQuestion = questionsList.get(currentQuestionIndex);
                JSONObject results = calculateDynamicPercentages();

                try {
                    percentage1.setText(results.getInt("option1") + "%");
                    percentage2.setText(results.getInt("option2") + "%");
                    percentage3.setText(results.getInt("option3") + "%");
                    percentage4.setText(results.getInt("option4") + "%");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                percentage1.setVisibility(View.VISIBLE);
                percentage2.setVisibility(View.VISIBLE);
                percentage3.setVisibility(View.VISIBLE);
                percentage4.setVisibility(View.VISIBLE);
            }
            isPercentagesVisible[0] = !isPercentagesVisible[0];
        });

        // Handle "Question suivant" button
        findViewById(R.id.validerButton2).setOnClickListener(v -> {
            if (currentQuestionIndex < questionsList.size() - 1) {
                currentQuestionIndex++;
                updateQuestion(nQuestion, questionText);

                resetClickCounts(); // Reset click counts for the next question
                percentage1.setVisibility(View.GONE);
                percentage2.setVisibility(View.GONE);
                percentage3.setVisibility(View.GONE);
                percentage4.setVisibility(View.GONE);
                isPercentagesVisible[0] = false;
            }
        });

        // Handle option clicks
        findViewById(R.id.button1).setOnClickListener(v -> optionClicks[0]++);
        findViewById(R.id.button2).setOnClickListener(v -> optionClicks[1]++);
        findViewById(R.id.button3).setOnClickListener(v -> optionClicks[2]++);
        findViewById(R.id.button4).setOnClickListener(v -> optionClicks[3]++);
    }

    private ArrayList<JSONObject> createSampleQuestions() {
        ArrayList<JSONObject> questions = new ArrayList<>();
        try {
            JSONObject question1 = new JSONObject();
            question1.put("question", "Quelle est la capitale de la France ?");
            questions.add(question1);

            JSONObject question2 = new JSONObject();
            question2.put("question", "Combien font 2 + 2 ?");
            questions.add(question2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return questions;
    }

    private JSONObject calculateDynamicPercentages() {
        JSONObject results = new JSONObject();
        int totalClicks = optionClicks[0] + optionClicks[1] + optionClicks[2] + optionClicks[3];

        try {
            if (totalClicks > 0) {
                results.put("option1", (optionClicks[0] * 100) / totalClicks);
                results.put("option2", (optionClicks[1] * 100) / totalClicks);
                results.put("option3", (optionClicks[2] * 100) / totalClicks);
                results.put("option4", (optionClicks[3] * 100) / totalClicks);
            } else {
                results.put("option1", 0);
                results.put("option2", 0);
                results.put("option3", 0);
                results.put("option4", 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }

    private void updateQuestion(TextView nQuestion, TextView questionText) {
        try {
            JSONObject currentQuestion = questionsList.get(currentQuestionIndex);
            questionText.setText(currentQuestion.getString("question"));

            String questionNumber = (currentQuestionIndex + 1) + "/" + questionsList.size();
            nQuestion.setText(questionNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void resetClickCounts() {
        for (int i = 0; i < optionClicks.length; i++) {
            optionClicks[i] = 0;
        }
    }
}
