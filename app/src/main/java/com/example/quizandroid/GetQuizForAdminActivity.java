package com.example.quizandroid;

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
import com.example.quizandroid.admin.QuizAdapter; // Updated to use the admin QuizAdapter
import com.example.quizandroid.model.Quiz;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;

public class GetQuizForAdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuizAdapter adapter;
    private List<Quiz> quizList;
    private ConnectionAPI connectionAPI;
    private final Gson gson = new Gson();
    private String authToken; // To store the token

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_quiz_admin);

        // Retrieve the token from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);
        authToken = prefs.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(this, "Token not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish(); // End the activity if the token is missing
            return;
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Calculate the number of columns based on screen width
        int columns = calculateNumberOfColumns();
        recyclerView.setLayoutManager(new GridLayoutManager(this, columns));

        // Initialize quiz list and adapter
        quizList = new ArrayList<>();
        adapter = new QuizAdapter(quizList, quiz -> {
            Intent intent = new Intent(GetQuizForAdminActivity.this, GetQuizForAdminActivity.class);
            intent.putExtra("quizTitle", quiz.getLibelle());
            startActivity(intent);
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
        int cardWidth = getResources().getDimensionPixelSize(R.dimen.quiz_card_width);
        return Math.max(1, screenWidth / cardWidth);
    }

    private void fetchQuizDetails() {
        new Thread(() -> {
            try {
                // Pass the context to the API method
                JsonObject response = QuizAPI.getAllQuizzes(GetQuizForAdminActivity.this);

                Log.d("GetQuizForAdminActivity", "Retrieved JSON: " + response.toString());

                // Extract the quiz list from the "data" key in the JSON response
                List<Quiz> quizzes = gson.fromJson(response.getAsJsonArray("data"), new TypeToken<List<Quiz>>() {}.getType());

                // Filter quizzes to include only those with etat == 0
                List<Quiz> notStartedQuizzes = new ArrayList<>();
                for (Quiz quiz : quizzes) {
                    if (quiz.getEtat() == 0) {
                        notStartedQuizzes.add(quiz);
                    }
                }

                // Update the RecyclerView on the main thread
                runOnUiThread(() -> updateRecyclerView(notStartedQuizzes));
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this, "Failed to fetch quizzes: " + e.getMessage(), Toast.LENGTH_LONG).show());
                Log.e("GetQuizForAdminActivity", "Error fetching quizzes", e);
            }
        }).start();
    }


    private void updateRecyclerView(List<Quiz> quizzes) {
        quizList.clear();
        quizList.addAll(quizzes);
        adapter.notifyDataSetChanged();
    }
}
