package com.example.quizandroid.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizandroid.API.QuestionAPI;
import com.example.quizandroid.API.QuizAPI;
import com.example.quizandroid.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    private TextView tvReceivedQuizJoinedConfirmation, tvReceivedFirstQuestion, tvReceivedNextQuestion, tvReceivedStats,tvReceivedClassement , tvReceivedAnswer,tvReceivedEndQuiz ;
    private Button btnJoinQuiz, btnNextQuestion, btnShowAnswer, btnShowStats, btnEndQuiz2, btnStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Initialisation des textViews
        tvReceivedQuizJoinedConfirmation = findViewById(R.id.tvReceivedQuizJoinedConfirmation);
        tvReceivedFirstQuestion = findViewById(R.id.tvReceivedFirstQuestion);
        tvReceivedNextQuestion = findViewById(R.id.tvReceivedNextQuestion);
        tvReceivedStats = findViewById(R.id.tvReceivedStats);
        tvReceivedClassement = findViewById(R.id.tvReceivedClassement);
        tvReceivedAnswer = findViewById(R.id.tvReceivedAnswer);
        tvReceivedEndQuiz = findViewById(R.id.tvReceivedEndQuiz);

        // Initialisation des boutons
        btnJoinQuiz = findViewById(R.id.btnJoinQuiz);
        btnStartQuiz =  findViewById(R.id.btnStartQuiz);
        btnNextQuestion = findViewById(R.id.btnNextQuestion2);
        btnShowAnswer = findViewById(R.id.btnShowAnswer2);
        btnShowStats = findViewById(R.id.btnShowStats2);
        btnEndQuiz2 = findViewById(R.id.btnEndQuiz2);

        /*** Se connecter au websocket ***/

        //Connexion au WebSocket
        WebSocketManager socketManager = WebSocketManager.getInstance();

        /*** Ecouter des messages venant du socket ***/

        //Ecouter la confirmation qu'on a rejoint le quiz
        socketManager.on("joinedQuiz", dataReceived -> {
            if (dataReceived.length > 0 && dataReceived[0] instanceof JSONObject) {
                try {
                    JSONObject data = (JSONObject) dataReceived[0];
                    String quizId = data.getString("quizId");
                    String message = data.getString("message");

                    runOnUiThread(() -> {
                        tvReceivedQuizJoinedConfirmation.setText(tvReceivedQuizJoinedConfirmation.getText() + " " + message);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Ecouter le debut du quiz
        //dataReceived est l'objet renvoyé par le socket
        socketManager.on("quizStarted", dataReceived -> {
            if (dataReceived.length > 0 && dataReceived[0] instanceof JSONObject) {
                try {
                    //Extraction des informations importantes recues par le socket
                    JsonObject question = socketManager.extractQuestion2(dataReceived);

                    //Seules les modifications de l'interface utilisateur doivent etre dans runOnUiThread
                    //Les autres traitements doivent etre faits ailleurs
                    runOnUiThread(() -> {
                        tvReceivedFirstQuestion.setText(tvReceivedFirstQuestion.getText()+ " "+ question);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Ecouter les nouvelles questions
        socketManager.on("nextQuestion", dataReceived -> {
            //Extraire les données utiles qui sont contenues dans l'objet JSON dataReceived
            //Ensuite mettre à jour l'interface utilisateur

            if (dataReceived.length > 0 && dataReceived[0] instanceof JSONObject) {
                try {
                    //Extraction des informations importantes recues par le socket
                    JsonObject question = socketManager.extractQuestion2(dataReceived);

                    //Seules les modifications de l'interface utilisateur doivent etre dans runOnUiThread
                    //Les autres traitements doivent etre faits ailleurs
                    runOnUiThread(() -> {
                        tvReceivedNextQuestion.setText(tvReceivedNextQuestion.getText()+ " "+ question);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Ecouter la bonne réponse
        socketManager.on("showAnswer", dataReceived -> {
            //Ici on peut afficher la bonne reponse en cherchant la proposition qui a pour champ correct=true
            //Sachant que les propositions sont contnues dans l'objet question renvoyé par l'api
        });

        // Ecouter les statistiques
        socketManager.on("showStatistique", dataReceived -> {
            //Extraire les données utiles qui sont contenues dans l'objet JSON dataReceived
            //Ensuite mettre à jour l'interface utilisateur
            if (dataReceived.length > 0 && dataReceived[0] instanceof JSONObject) {
                try {
                    //Extraction des informations importantes recues par le socket
                    JSONObject statistiques = socketManager.extractStatistique(dataReceived);

                    //Seules les modifications de l'interface utilisateur doivent etre dans runOnUiThread
                    //Les autres traitements doivent etre faits ailleurs
                    runOnUiThread(() -> {
                        tvReceivedStats.setText(tvReceivedStats.getText()+ " "+ statistiques);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Ecouter le classement
        socketManager.on("showClassement", dataReceived -> {
            //Extraire les données utiles qui sont contenues dans l'objet JSON dataReceived
            //Ensuite mettre à jour l'interface utilisateur
        });

        // Ecouter la fin du quiz
        socketManager.on("quizEnded", dataReceived -> {
            //Extraire les données utiles qui sont contenues dans l'objet JSON dataReceived
            //Ensuite mettre à jour l'interface utilisateur
            runOnUiThread(() -> {
                tvReceivedEndQuiz.setText(tvReceivedEndQuiz.getText()+ " "+ "Oui");
            });
        });


        /*** Publication des messages au socket ***/

        // Rejoindre un quiz
        btnJoinQuiz.setOnClickListener(v -> {
            try {
                JSONObject data =  new JSONObject();
                data.put("quizId", 1);

                socketManager.emit("joinQuiz", data);
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de l'envoi du message joinQuiz", e);
            }
        });

        //Démarrer un quiz
        btnStartQuiz.setOnClickListener(v ->{
            new Thread(() -> {
                try {
                    int[] questionsIds = QuestionAPI.getAllQuestionsIds(1);
                    JsonObject firstQuestion = QuestionAPI.getQuestion(questionsIds[1]);

                    // Simulation de l'objet renvoyé par l'api des questions
                    // Créer l'objet principal
                    JSONObject mainObject = new JSONObject();

                    // Ajouter le champ "data"
                    JSONObject dataObject = new JSONObject();
                    dataObject.put("id", 1);
                    dataObject.put("libelle", "Combien font 2 + 2 ?");

                    // Ajouter les propositions (tableau JSON)
                    JSONArray propositionsArray = new JSONArray();

                    // Première proposition
                    JSONObject proposition1 = new JSONObject();
                    proposition1.put("id", 1);
                    proposition1.put("correct", 0);
                    proposition1.put("libelle", "3");
                    propositionsArray.put(proposition1);

                    // Deuxième proposition
                    JSONObject proposition2 = new JSONObject();
                    proposition2.put("id", 2);
                    proposition2.put("correct", 1);
                    proposition2.put("libelle", "4");
                    propositionsArray.put(proposition2);

                    // Troisième proposition
                    JSONObject proposition3 = new JSONObject();
                    proposition3.put("id", 3);
                    proposition3.put("correct", 0);
                    proposition3.put("libelle", "5");
                    propositionsArray.put(proposition3);

                    // Quatrième proposition
                    JSONObject proposition4 = new JSONObject();
                    proposition4.put("id", 4);
                    proposition4.put("correct", 0);
                    proposition4.put("libelle", "6");
                    propositionsArray.put(proposition4);

                    // Ajouter les propositions à "data"
                    dataObject.put("propositions", propositionsArray);

                    // Ajouter "data" à l'objet principal
                    mainObject.put("data", dataObject);
                    mainObject.put("code", 200);
                    mainObject.put("error", JSONObject.NULL);

                    // Créer l'objet final attendu par le socket
                    JSONObject data = new JSONObject();
                    data.put("quizId", 1);
                    data.put("firstQuestion", firstQuestion);

                    socketManager.emit("startQuiz", data);
                } catch (Exception e) {
                    Log.e(TAG, "Erreur lors de l'envoi du message startQuiz", e);
                }
            }).start();
        });

        // Passer à la question suivante
        btnNextQuestion.setOnClickListener(v -> {
            try {
                //Simulation de l'objet renvoyé par l'api des questions
                // Créer l'objet principal
                JSONObject mainObject = new JSONObject();

                // Ajouter le champ "data"
                JSONObject dataObject = new JSONObject();
                dataObject.put("id", 1);
                dataObject.put("libelle", "Quelle est la capitale de la France ?");

                // Ajouter les propositions (tableau JSON)
                JSONArray propositionsArray = new JSONArray();

                // Première proposition
                JSONObject proposition1 = new JSONObject();
                proposition1.put("id", 1);
                proposition1.put("correct", 0);
                proposition1.put("libelle", "Marseille");
                propositionsArray.put(proposition1);

                // Deuxième proposition
                JSONObject proposition2 = new JSONObject();
                proposition2.put("id", 2);
                proposition2.put("correct", 1);
                proposition2.put("libelle", "Paris");
                propositionsArray.put(proposition2);

                // Troisième proposition
                JSONObject proposition3 = new JSONObject();
                proposition3.put("id", 3);
                proposition3.put("correct", 0);
                proposition3.put("libelle", "Lyon");
                propositionsArray.put(proposition3);

                // Quatrième proposition
                JSONObject proposition4 = new JSONObject();
                proposition4.put("id", 4);
                proposition4.put("correct", 0);
                proposition4.put("libelle", "Rouen");
                propositionsArray.put(proposition4);

                // Ajouter les propositions à "data"
                dataObject.put("propositions", propositionsArray);

                // Ajouter "data" à l'objet principal
                mainObject.put("data", dataObject);
                mainObject.put("code", 200);
                mainObject.put("error", JSONObject.NULL);

                // Créer l'objet final attendu par le socket
                JSONObject data = new JSONObject();
                data.put("quizId",1);
                data.put("nextQuestion", mainObject);

                // Envoyer l'objet structuré
                socketManager.emit("nextQuestion", data);
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de l'envoi du message nextQuestion", e);
            }
        });

        //Afficher la bonne réponse
        btnShowAnswer.setOnClickListener(v -> {
            try{
                JSONObject data =  new JSONObject();
                data.put("quizId", 1);

                socketManager.emit("showAnswer", data);
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de l'envoi du message showAnswer", e);
            }
        });

        //Envoyer les statistiques
        btnShowStats.setOnClickListener(v->{
            try{
                JSONObject data = new JSONObject();

                // Simulation d'un objet retourné par l'api lorsqu'on demande les stats
                JSONObject statistiquesObject = new JSONObject();
                // Création de l'array "Statistiques"
                JSONArray statistiquesArray = new JSONArray();
                // Création des objets individuels pour chaque statistique
                JSONObject stat1 = new JSONObject();
                stat1.put("personId", 3);
                stat1.put("correctAnswers", 2);
                stat1.put("totalResponseTime", 2100740.0);
                stat1.put("ranking", 1);
                JSONObject stat2 = new JSONObject();
                stat2.put("personId", 2);
                stat2.put("correctAnswers", 2);
                stat2.put("totalResponseTime", 2100900.0);
                stat2.put("ranking", 2);
                JSONObject stat3 = new JSONObject();
                stat3.put("personId", 1);
                stat3.put("correctAnswers", 1);
                stat3.put("totalResponseTime", 1050310.0);
                stat3.put("ranking", 3);
                // Ajout des statistiques dans l'array
                statistiquesArray.put(stat1);
                statistiquesArray.put(stat2);
                statistiquesArray.put(stat3);
                // Ajout de l'array et autres informations
                statistiquesObject.put("Statistiques", statistiquesArray);
                statistiquesObject.put("code", 200);
                statistiquesObject.put("error", JSONObject.NULL);

                //Construction de l'objet attendu par le socket
                data.put("quizId", 1);
                data.put("statistiques", statistiquesObject);

                socketManager.emit("showStatistique", data);
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de l'envoi du message showStats", e);
            }
        });

        //Terminer le quiz
        btnEndQuiz2.setOnClickListener(v->{
            try{
                JSONObject data = new JSONObject();
                data.put("quizId", 1);
                socketManager.emit("endQuiz",data);
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de l'envoi du message endQuiz", e);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Désactiver les écouteurs WebSocket
        WebSocketManager socketManager = WebSocketManager.getInstance();
        socketManager.off("joinedQuiz");
        socketManager.off("quizStarted");
        socketManager.off("nextQuestion");
        socketManager.off("showAnswer");
        socketManager.off("showStatistique");
        socketManager.off("showClassement");
        socketManager.off("endQuiz");

        Log.d(TAG, "Écouteurs WebSocket désactivés");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Réabonnez-vous aux événements WebSocket
        WebSocketManager socketManager = WebSocketManager.getInstance();
        /*** Reecouter les messages venant du socket ***/


        Log.d(TAG, "Écouteurs WebSocket réactivés");
    }
}
