package com.example.quizapp.xulydulieu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.AdminQuestionActivity;
import com.example.quizapp.R;
import com.example.quizapp.class_package.quizzes;
import com.example.quizapp.database.DatabaseHelper;

import java.util.List;

public class AdminQuizAdapter extends RecyclerView.Adapter<AdminQuizAdapter.ViewHolder> {
    Context context;
    List<quizzes> list;
    DatabaseHelper db;

    public AdminQuizAdapter(Context context, List<quizzes> list) {
        this.context = context;
        this.list = list;
        this.db = new DatabaseHelper(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, count, difficulty;
        Button btnManage;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.quiz_title);
            desc = itemView.findViewById(R.id.quiz_description);
            count = itemView.findViewById(R.id.quiz_question_count);
            difficulty = itemView.findViewById(R.id.quiz_difficulty);
            btnManage = itemView.findViewById(R.id.btnStartQuiz);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        quizzes quiz = list.get(position);

        holder.title.setText(quiz.title);
        holder.desc.setText(quiz.description);
        holder.difficulty.setText(quiz.difficulty);

        int qCount = db.getQuestionCountByQuizId(quiz.id); // dùng hàm count, không load cả list
        holder.count.setText(qCount + " câu hỏi");

        holder.btnManage.setText("Quản lý câu hỏi");
        holder.btnManage.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminQuestionActivity.class);
            intent.putExtra("quiz_id", quiz.id);
            intent.putExtra("quiz_title", quiz.title);
            context.startActivity(intent);
        });

        // THÊM MỚI: Long click để xóa Quiz
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa Quiz")
                    .setMessage("Bạn có chắc muốn xóa quiz '" + quiz.title + "' và toàn bộ câu hỏi bên trong?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (db.deleteQuiz(quiz.id)) {
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size());
                            Toast.makeText(context, "Đã xóa quiz", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() { return list.size(); }
}