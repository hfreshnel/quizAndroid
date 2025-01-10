package com.example.quizandroid.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizandroid.R;
import com.example.quizandroid.model.Quiz;

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
        // Inflate the custom layout for admin quiz items
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);

        // Set quiz title
        holder.quizTitle.setText(quiz.getLibelle());

        // Set a placeholder or dynamic icon if needed
        // Currently using the default icon in the layout
        // If needed, implement logic to set specific icons based on quiz data

        // Handle click event
        holder.itemView.setOnClickListener(v -> listener.onQuizClick(quiz));
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    // ViewHolder for admin quiz items
    static class QuizViewHolder extends RecyclerView.ViewHolder {
        ImageView quizIcon;
        TextView quizTitle;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizIcon = itemView.findViewById(R.id.quiz_icon);
            quizTitle = itemView.findViewById(R.id.quiz_title);
        }
    }
}
