package com.example.quizandroid.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminQuizQuestionActivity extends AppCompatActivity {

    private int currentQuestionIndex = 0;
    private ArrayList<JSONObject> questionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz_question);

        // Get references to the percentage TextViews
        TextView percentage1 = findViewById(R.id.percentage1);
        TextView percentage2 = findViewById(R.id.percentage2);
        TextView percentage3 = findViewById(R.id.percentage3);
        TextView percentage4 = findViewById(R.id.percentage4);
        TextView nQuestion = findViewById(R.id.nQuestion);
        TextView questionText = findViewById(R.id.question);

        // Initially hide percentage TextViews
        percentage1.setVisibility(View.GONE);
        percentage2.setVisibility(View.GONE);
        percentage3.setVisibility(View.GONE);
        percentage4.setVisibility(View.GONE);

        // Create sample questions and answers
        questionsList = createSampleQuestions();

        // Display the first question
        updateQuestion(nQuestion, questionText);

        // Variable to track whether percentages are shown
        final boolean[] isPercentagesVisible = {false};

        // Set an OnClickListener on the FrameLayout with the percent icon
        findViewById(R.id.frameLayout).setOnClickListener(v -> {
            try {
                if (isPercentagesVisible[0]) {
                    // Hide percentages
                    percentage1.setVisibility(View.GONE);
                    percentage2.setVisibility(View.GONE);
                    percentage3.setVisibility(View.GONE);
                    percentage4.setVisibility(View.GONE);
                } else {
                    // Show percentages
                    JSONObject currentQuestion = questionsList.get(currentQuestionIndex);
                    JSONObject results = calculatePercentages(currentQuestion);

                    percentage1.setText(results.getInt("option1") + "%");
                    percentage2.setText(results.getInt("option2") + "%");
                    percentage3.setText(results.getInt("option3") + "%");
                    percentage4.setText(results.getInt("option4") + "%");

                    percentage1.setVisibility(View.VISIBLE);
                    percentage2.setVisibility(View.VISIBLE);
                    percentage3.setVisibility(View.VISIBLE);
                    percentage4.setVisibility(View.VISIBLE);
                }
                isPercentagesVisible[0] = !isPercentagesVisible[0];
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        // Set an OnClickListener for the "Question suivant" button
        findViewById(R.id.validerButton2).setOnClickListener(v -> {
            if (currentQuestionIndex < questionsList.size() - 1) {
                currentQuestionIndex++;
                updateQuestion(nQuestion, questionText);

                // Reset percentages visibility
                percentage1.setVisibility(View.GONE);
                percentage2.setVisibility(View.GONE);
                percentage3.setVisibility(View.GONE);
                percentage4.setVisibility(View.GONE);
                isPercentagesVisible[0] = false;
            }
        });
    }

    private ArrayList<JSONObject> createSampleQuestions() {
        ArrayList<JSONObject> questions = new ArrayList<>();
        try {
            // Question 1
            JSONObject question1 = new JSONObject();
            question1.put("question", "What is the capital of France?");
            question1.put("answers", new JSONObject()
                    .put("option1", 20) // 20 votes
                    .put("option2", 50) // 50 votes
                    .put("option3", 15) // 15 votes
                    .put("option4", 15) // 15 votes
            );
            questions.add(question1);

            // Question 2
            JSONObject question2 = new JSONObject();
            question2.put("question", "What is 2 + 2?");
            question2.put("answers", new JSONObject()
                    .put("option1", 60)
                    .put("option2", 20)
                    .put("option3", 10)
                    .put("option4", 10)
            );
            questions.add(question2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return questions;
    }

    private JSONObject calculatePercentages(JSONObject question) throws JSONException {
        JSONObject results = new JSONObject();
        JSONObject answers = question.getJSONObject("answers");
        int totalVotes = answers.getInt("option1") + answers.getInt("option2") +
                answers.getInt("option3") + answers.getInt("option4");

        if (totalVotes > 0) {
            results.put("option1", (answers.getInt("option1") * 100) / totalVotes);
            results.put("option2", (answers.getInt("option2") * 100) / totalVotes);
            results.put("option3", (answers.getInt("option3") * 100) / totalVotes);
            results.put("option4", (answers.getInt("option4") * 100) / totalVotes);
        } else {
            results.put("option1", 0);
            results.put("option2", 0);
            results.put("option3", 0);
            results.put("option4", 0);
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
}
