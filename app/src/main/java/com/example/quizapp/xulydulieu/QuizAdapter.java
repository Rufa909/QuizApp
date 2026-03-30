package com.example.quizapp.xulydulieu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.QuestionActivity;
import com.example.quizapp.R;
import com.example.quizapp.class_package.quizzes;
import com.example.quizapp.database.DatabaseHelper;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
    Context context;
    List<quizzes> list;
    DatabaseHelper db;

    public QuizAdapter(Context context, List<quizzes> list){
        this.context = context;
        this.list = list;
        this.db = new DatabaseHelper(context); // THÊM
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView quiz_Title, quiz_Des, quiz_Count, quiz_Difficulty; // THÊM count, difficulty
        Button btnStartQuiz;

        public ViewHolder(View itemView){
            super(itemView);
            quiz_Title      = itemView.findViewById(R.id.quiz_title);
            quiz_Des        = itemView.findViewById(R.id.quiz_description);
            quiz_Count      = itemView.findViewById(R.id.quiz_question_count); // THÊM
            quiz_Difficulty = itemView.findViewById(R.id.quiz_difficulty);     // THÊM
            btnStartQuiz    = itemView.findViewById(R.id.btnStartQuiz);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_quiz, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        quizzes q = list.get(position);

        holder.quiz_Title.setText(q.title);
        holder.quiz_Des.setText(q.description);
        holder.quiz_Difficulty.setText(q.difficulty);                              // THÊM

        int qCount = db.getQuestionCountByQuizId(q.id);                           // THÊM
        holder.quiz_Count.setText(qCount + " câu hỏi");                           // THÊM

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuestionActivity.class);
            intent.putExtra("quiz_id", q.id);
            context.startActivity(intent);
        });

        holder.btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuestionActivity.class);
            intent.putExtra("quiz_id", q.id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
