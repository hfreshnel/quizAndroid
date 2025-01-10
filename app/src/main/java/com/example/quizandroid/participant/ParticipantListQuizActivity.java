package com.example.quizandroid.participant;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizandroid.API.ConnectionAPI;
import com.example.quizandroid.API.QuizAPI;
import com.example.quizandroid.R;
import com.example.quizandroid.admin.AdminClassementActivity;
import com.example.quizandroid.model.Quiz;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParticipantListQuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuizAdapter adapter;
    private List<Quiz> quizList;
    private ConnectionAPI connectionAPI;
    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_quizzes);

        // Calculate the number of columns based on screen width
        int columns = calculateNumberOfColumns();
        recyclerView.setLayoutManager(new GridLayoutManager(this, columns));

        // Initialize quiz list and adapter
        quizList = new ArrayList<>();
        adapter = new QuizAdapter(quizList, quiz -> {
            if (quiz.getEtat() == 10) {
                // Start quiz activity
                Intent intent = new Intent(ParticipantListQuizActivity.this, ParticipantQuizActivity.class);
                intent.putExtra("quizTitle", quiz.getLibelle());
                startActivity(intent);
            } else if (quiz.getEtat() == 20) {
                // Open leaderboard activity
                Intent intent = new Intent(ParticipantListQuizActivity.this, AdminClassementActivity.class);
                intent.putExtra("quizId", quiz.getId()); // Pass the quiz ID
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        // Initialize ConnectionAPI
        connectionAPI = new ConnectionAPI();

        // Fetch quiz details
        fetchQuizDetails();
    }

    private int calculateNumberOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int cardWidth = getResources().getDimensionPixelSize(R.dimen.quiz_card_width); // Define in dimens.xml
        return Math.max(1, screenWidth / cardWidth);
    }

    private void fetchQuizDetails() {
        new Thread(() -> {
            try {
                // Fetch quizzes using QuizAPI
                JsonObject response = QuizAPI.getAllQuizzes();
                Log.d("ParticipantListQuizActivity", "Retrieved JSON: " + response.toString());

                // Extract the quiz list from the "data" key in the JSON response
                List<Quiz> quizzes = gson.fromJson(response.getAsJsonArray("data"), new TypeToken<List<Quiz>>() {}.getType());

                runOnUiThread(() -> updateRecyclerView(quizzes));
            } catch (IOException e) {
                // Handle errors on the main thread
                runOnUiThread(() -> Toast.makeText(this, "Failed to fetch quizzes: " + e.getMessage(), Toast.LENGTH_LONG).show());
                Log.e("ParticipantListQuizActivity", "Error fetching quizzes", e);
            }
        }).start();
    }

    private void updateRecyclerView(List<Quiz> quizzes) {
        quizList.clear();
        quizList.addAll(quizzes);
        adapter.notifyDataSetChanged();
    }
}