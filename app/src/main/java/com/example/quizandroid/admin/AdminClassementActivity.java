package com.example.quizandroid.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizandroid.R;
import com.example.quizandroid.model.Personne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdminClassementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_classement);

        // Set click listener for the terminerQuiz button
        findViewById(R.id.terminerQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminClassementActivity.this, AdminQuizQuestionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Fetch quiz results and calculate the leaderboard
        List<Personne> participants = fetchQuizResults();
        calculateAndDisplayLeaderboard(participants);
    }

    private List<Personne> fetchQuizResults() {
        List<Personne> participants = new ArrayList<>();
        try {
            String jsonResponse = "{ \"classement\": [" +
                    "{\"id_personne\":1, \"nom\":\"Dupont\", \"prenom\":\"Jean\", \"score\":80, \"temps_moyen_question\":2.5}," +
                    "{\"id_personne\":2, \"nom\":\"Martin\", \"prenom\":\"Sophie\", \"score\":75, \"temps_moyen_question\":3.0}," +
                    "{\"id_personne\":3, \"nom\":\"Durand\", \"prenom\":\"Paul\", \"score\":70, \"temps_moyen_question\":3.4}," +
                    "{\"id_personne\":4, \"nom\":\"Moreau\", \"prenom\":\"Emma\", \"score\":65, \"temps_moyen_question\":4.0}," +
                    "{\"id_personne\":5, \"nom\":\"Bernard\", \"prenom\":\"Lucas\", \"score\":60, \"temps_moyen_question\":4.5}," +
                    "{\"id_personne\":6, \"nom\":\"Petit\", \"prenom\":\"Clara\", \"score\":55, \"temps_moyen_question\":5.0}," +
                    "{\"id_personne\":7, \"nom\":\"Roux\", \"prenom\":\"Nathan\", \"score\":50, \"temps_moyen_question\":5.5}," +
                    "{\"id_personne\":8, \"nom\":\"Fournier\", \"prenom\":\"Mélanie\", \"score\":45, \"temps_moyen_question\":6.0}," +
                    "{\"id_personne\":9, \"nom\":\"Blanc\", \"prenom\":\"Alice\", \"score\":40, \"temps_moyen_question\":6.5}," +
                    "{\"id_personne\":10, \"nom\":\"Garnier\", \"prenom\":\"Louis\", \"score\":35, \"temps_moyen_question\":7.0}" +
                    "]}";

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray classementArray = jsonObject.getJSONArray("classement");

            for (int i = 0; i < classementArray.length(); i++) {
                JSONObject participantObject = classementArray.getJSONObject(i);
                Personne participant = new Personne(
                        participantObject.getLong("id_personne"),
                        participantObject.getString("nom"),
                        participantObject.getString("prenom"),
                        null, // mail is not used here
                        null, // mdp is not used here
                        0 // role is not relevant here
                );
                participants.add(participant);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return participants;
    }

    private void calculateAndDisplayLeaderboard(List<Personne> participants) {
        // Sort by score, then by average time (assuming scores are included)
        Collections.sort(participants, new Comparator<Personne>() {
            @Override
            public int compare(Personne p1, Personne p2) {
                return Integer.compare(p2.getRole(), p1.getRole()); // Descending by score if roles map to scores
            }
        });

        // Update UI with sorted leaderboard
        updateRankView(R.id.first, participants.get(0));
        updateRankView(R.id.second, participants.get(1));
        updateRankView(R.id.third, participants.get(2));
        for (int i = 3; i < 10; i++) {
            int viewId = getResources().getIdentifier("rank" + (i + 1) + "Name", "id", getPackageName());
            updateRankView(viewId, participants.get(i), i + 1);
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
