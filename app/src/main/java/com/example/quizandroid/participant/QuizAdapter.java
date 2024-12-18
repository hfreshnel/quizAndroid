package com.example.quizandroid.participant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizandroid.R;
import com.example.quizandroid.model.Quiz;

import java.text.SimpleDateFormat;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private final List<Quiz> quizList;
    private final OnQuizClickListener listener;

    // Interface for handling quiz click events
    public interface OnQuizClickListener {
        void onQuizClick(Quiz quiz);
    }

    public QuizAdapter(List<Quiz> quizList, OnQuizClickListener listener) {
        this.quizList = quizList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz_card, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);

        // Set quiz title
        holder.title.setText(quiz.getLibelle());

        // Format the Timestamp to extract only the date part
        String date;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Define desired date format
            date = dateFormat.format(quiz.getDateDebutQuiz()); // Format the Timestamp
        } catch (Exception e) {
            date = "Invalid date"; // Fallback in case of an error
        }

        holder.description.setText(date);

        // Update button text based on "etat"
        switch (quiz.getEtat()) {
            case 10:
                holder.startButton.setText("Rejoindre"); // Join
                break;
            case 20:
                holder.startButton.setText("Consulter les statistiques"); // See statistics
                break;
            default:
                holder.startButton.setText("Commencer le quiz"); // Start the quiz
                break;
        }

        // Set click listener for the button
        holder.startButton.setOnClickListener(v -> listener.onQuizClick(quiz));
    }


    @Override
    public int getItemCount() {
        return quizList.size();
    }

    // ViewHolder for each quiz item (card)
    static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        Button startButton;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_quiz_title);
            description = itemView.findViewById(R.id.text_quiz_description);
            startButton = itemView.findViewById(R.id.button_start_quiz);
        }
    }
}
