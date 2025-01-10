package com.example.quizandroid.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizandroid.R;
import com.example.quizandroid.model.Personne;
import com.example.quizandroid.API.StatAPI;
import com.example.quizandroid.API.PersonneAPI;
import com.example.quizandroid.participant.ParticipantListQuizActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminClassementActivity extends AppCompatActivity {

    private static final String TAG = "AdminClassementActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_classement);

        // Set click listener for the terminerQuiz button
        findViewById(R.id.terminerQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminClassementActivity.this, ParticipantListQuizActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Fetch and display leaderboard from API
        fetchLeaderboard();
    }

    private void fetchLeaderboard() {
        new Thread(() -> {
            try {
                // Fetch leaderboard data from the API
                List<Personne> participants = new ArrayList<>();
                PersonneAPI personneAPI = new PersonneAPI();

                // Fetch the JSON response from the ranking API
                JsonObject response = StatAPI.getQuizRanking(1L); // Replace 1L with actual quiz ID

                if (response != null && response.has("data")) {
                    JsonArray rankingData = response.getAsJsonArray("data");

                    for (com.google.gson.JsonElement element : rankingData) {
                        JsonObject participantJson = element.getAsJsonObject();

                        // Get person details from PersonneAPI using personId
                        String personId = participantJson.get("personId").getAsString();
                        Log.d(TAG, "Retrieved Participant id : ID=" + personId);

                        Personne personne = PersonneAPI.getPersonneById(personId);

                        // Populate additional details
                        personne.setRole(participantJson.get("ranking").getAsInt());
                        participants.add(personne);

                        Log.d(TAG, "Retrieved Participant: ID=" + personId + ", Name=" + personne.getNom() + " " + personne.getPrenom() + ", Ranking=" + participantJson.get("ranking").getAsInt());

                    }

                    // Update the UI on the main thread
                    runOnUiThread(() -> displayLeaderboard(participants));
                }
            } catch (IOException e) {
                Log.e(TAG, "Failed to fetch leaderboard: ", e);
            }
        }).start();
    }

    private void displayLeaderboard(List<Personne> participants) {
        // Update UI with leaderboard data
        if (!participants.isEmpty()) {
            updateRankView(R.id.first, participants.get(0));
            if (participants.size() > 1) updateRankView(R.id.second, participants.get(1));
            if (participants.size() > 2) updateRankView(R.id.third, participants.get(2));

            for (int i = 3; i < participants.size() && i < 10; i++) {
                int viewId = getResources().getIdentifier("rank" + (i + 1) + "Name", "id", getPackageName());
                updateRankView(viewId, participants.get(i), i + 1);
            }
        }
    }

    private void updateRankView(int viewId, Personne participant) {
        TextView rankTextView = findViewById(viewId);
        rankTextView.setText(participant.getNom() + " " + participant.getPrenom());
    }

    private void updateRankView(int viewId, Personne participant, int rank) {
        TextView rankTextView = findViewById(viewId);
        rankTextView.setText(rank + ". " + participant.getNom() + " " + participant.getPrenom());
    }
}