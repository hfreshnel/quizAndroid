package com.example.quizandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizandroid.API.QuizAPI;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetQuizForAdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuizAdapter adapter;
    private final List<Quiz> quizList = new ArrayList<>();
    private List<Quiz> currentPageData = new ArrayList<>();
    private int currentPage = 1;
    private final int itemsPerPage = 6;
    private ActivityResultLauncher<Intent> createQuizLauncher;
    private final QuizAPI quizAPI = new QuizAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_quiz_admin);

        recyclerView = findViewById(R.id.recyclerView);
        Button btnPrevious = findViewById(R.id.btn_previous);
        Button btnNext = findViewById(R.id.btn_next);
        Button btnCreate = findViewById(R.id.button);

        // Configure RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new QuizAdapter(currentPageData);
        recyclerView.setAdapter(adapter);

        // Fetch quizzes from API
        fetchQuizzes();

        // Handle Previous button click
        btnPrevious.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                loadPage(currentPage);
            } else {
                Toast.makeText(this, "Vous êtes déjà à la première page", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Next button click
        btnNext.setOnClickListener(v -> {
            if (currentPage < getTotalPages()) {
                currentPage++;
                loadPage(currentPage);
            } else {
                Toast.makeText(this, "Vous êtes déjà à la dernière page", Toast.LENGTH_SHORT).show();
            }
        });

        createQuizLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Retrieve data from CreerQuizActivity
                        Intent data = result.getData();
                        String quizName = data.getStringExtra("quiz_name");
                        int questionCount = data.getIntExtra("question_count", 0);

                        // Add the new quiz to the list
                        Quiz newQuiz = new Quiz(quizName, R.drawable.ideas);
                        quizList.add(newQuiz);

                        // Reload the current page
                        loadPage(currentPage);
                        Toast.makeText(this, "Quiz ajouté : " + quizName, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Create quiz button
        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(GetQuizForAdminActivity.this, CreerQuizActivity.class);
            createQuizLauncher.launch(intent);
        });
    }

    private void fetchQuizzes() {
        new Thread(() -> {
            try {
                // Call the API to fetch quizzes
                JsonObject response = quizAPI.getAllQuizzes(1); // Replace with the actual admin ID
                JsonArray quizzes = response.getAsJsonArray("quizzes");

                quizList.clear();
                for (int i = 0; i < quizzes.size(); i++) {
                    JsonObject quizJson = quizzes.get(i).getAsJsonObject();
                    Quiz quiz = new Quiz(
                            quizJson.get("title").getAsString(),
                            R.drawable.ideas // Replace with an appropriate resource if available
                    );
                    quizList.add(quiz);
                }

                // Update UI on the main thread
                runOnUiThread(() -> {
                    loadPage(currentPage);
                    Toast.makeText(this, "Quizzes loaded successfully", Toast.LENGTH_SHORT).show();
                });

            } catch (IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Failed to load quizzes: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    // Load a specific page
    private void loadPage(int page) {
        currentPageData.clear();
        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, quizList.size());
        for (int i = startIndex; i < endIndex; i++) {
            currentPageData.add(quizList.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    // Calculate total pages
    private int getTotalPages() {
        return (int) Math.ceil((double) quizList.size() / itemsPerPage);
    }
}
