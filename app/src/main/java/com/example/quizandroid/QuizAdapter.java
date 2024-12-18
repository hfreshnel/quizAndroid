package com.example.quizandroid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private final List<Quiz> quizList;
    private final Context context; // Contexte pour démarrer l'activité

    public QuizAdapter(Context context, List<Quiz> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.quizTitle.setText(quiz.getTitle());
        holder.quizIcon.setImageResource(quiz.getIconResId());

        // Ajouter un événement de clic pour chaque élément
        holder.itemView.setOnClickListener(v -> {
            // Créer un Intent pour démarrer l'activité QuizDetailActivity
            Intent intent = new Intent(context, QuestionQuizActivity.class);
            intent.putExtra("quiz_name", quiz.getTitle());
            context.startActivity(intent); // Lancer l'activité
        });
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView quizTitle;
        ImageView quizIcon;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizTitle = itemView.findViewById(R.id.quiz_title);
            quizIcon = itemView.findViewById(R.id.quiz_icon);
        }
    }
}
