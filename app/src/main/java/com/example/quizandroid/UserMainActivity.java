package com.example.quizandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuizAdapter adapter;
    private List<Quiz> quizList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_quizzes);

        // Calculate the number of columns based on screen width
        int columns = calculateNumberOfColumns();

        // Set RecyclerView to display items in a grid layout with dynamic columns
        recyclerView.setLayoutManager(new GridLayoutManager(this, columns));

        // Create the list of quizzes
        quizList = new ArrayList<>();
        quizList.add(new Quiz("Math Quiz", "Test your math skills."));
        quizList.add(new Quiz("Science Quiz", "Challenge your knowledge of science."));
        quizList.add(new Quiz("History Quiz", "Explore historical facts."));

        // Create and set the adapter for the RecyclerView
        adapter = new QuizAdapter(quizList, quiz -> {
            // Handle click event when a quiz card is clicked
            Intent intent = new Intent(UserMainActivity.this, QuizActivity.class);
            intent.putExtra("quizTitle", quiz.getTitle()); // Pass quiz title or ID
            startActivity(intent);
        });

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }

    private int calculateNumberOfColumns() {
        // Get the screen width
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;

        // Calculate the number of columns based on the screen width and card width
        int cardWidth = getResources().getDimensionPixelSize(R.dimen.quiz_card_width); // Set this in dimens.xml
        int numberOfColumns = screenWidth / cardWidth;

        // Return at least 1 column
        return Math.max(1, numberOfColumns);
    }
}
