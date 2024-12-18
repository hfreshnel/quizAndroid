package com.example.quizandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private static final int CREATE_QUIZ_REQUEST = 1; // Code de requête pour l'activité CreerQuizActivity

    private RecyclerView recyclerView;
    private QuizAdapter adapter;
    private List<Quiz> quizList = new ArrayList<>();
    private List<Quiz> currentPageData = new ArrayList<>();
    private int currentPage = 1;
    private final int itemsPerPage = 6;
    private ActivityResultLauncher<Intent> createQuizLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        recyclerView = findViewById(R.id.recyclerView);
        Button btnPrevious = findViewById(R.id.btn_previous);
        Button btnNext = findViewById(R.id.btn_next);
        Button btnCreate = findViewById(R.id.button);

        // Initialiser les données statiques des quiz
        initializeQuizData();

        // Configurer RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new QuizAdapter(this, currentPageData);
        recyclerView.setAdapter(adapter);

        // Charger la première page
        loadPage(currentPage);

        // Gérer les clics des boutons Précédent et Suivant
        btnPrevious.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                loadPage(currentPage);
            } else {
                Toast.makeText(this, "Vous êtes déjà à la première page", Toast.LENGTH_SHORT).show();
            }
        });

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
                        // Récupérer les données de CreerQuizActivity
                        Intent data = result.getData();
                        String quizName = data.getStringExtra("quiz_name");
                        int questionCount = data.getIntExtra("question_count", 0);

                        // Ajouter le nouveau quiz à la liste
                        Quiz newQuiz = new Quiz(quizName , R.drawable.ideas);
                        quizList.add(newQuiz);

                        // Recharger la page actuelle
                        loadPage(currentPage);
                        Toast.makeText(this, "Quiz ajouté : " + quizName, Toast.LENGTH_SHORT).show();
                    }
                }
        );


        // Bouton pour créer un nouveau quiz
        btnCreate.setOnClickListener(v -> {

                Intent intent = new Intent(QuestionActivity.this, CreerQuizActivity.class);
                createQuizLauncher.launch(intent); // Lancer l'activité avec le launcher

// Lancer CreerQuizActivity avec un code de requête
        });
    }

    // Méthode appelée lorsque CreerQuizActivity renvoie un résultat
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_QUIZ_REQUEST && resultCode == RESULT_OK) {
            // Récupérer les données envoyées par CreerQuizActivity
            String quizName = data.getStringExtra("quiz_name");
            int questionCount = data.getIntExtra("question_count", 0);

            // Ajouter le nouveau quiz à la liste
            Quiz newQuiz = new Quiz(quizName + " (" + questionCount + " questions)", R.drawable.ideas);
            quizList.add(newQuiz);

            // Recharger la page actuelle
            loadPage(currentPage);
            Toast.makeText(this, "Quiz ajouté : " + quizName, Toast.LENGTH_SHORT).show();
        }
    }

    // Initialiser la liste complète des quiz
    private void initializeQuizData() {
        for (int i = 1; i <= 20; i++) { // Supposons qu'il y ait 20 quiz
            quizList.add(new Quiz("Quiz " + i, R.drawable.ideas));
        }
    }

    // Charger une page spécifique
    private void loadPage(int page) {
        currentPageData.clear();
        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, quizList.size());
        for (int i = startIndex; i < endIndex; i++) {
            currentPageData.add(quizList.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    // Calculer le nombre total de pages
    private int getTotalPages() {
        return (int) Math.ceil((double) quizList.size() / itemsPerPage);
    }
}
