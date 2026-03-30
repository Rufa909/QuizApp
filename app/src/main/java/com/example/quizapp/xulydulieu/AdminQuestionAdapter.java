package com.example.quizapp.xulydulieu;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.class_package.answers;
import com.example.quizapp.class_package.questions;
import com.example.quizapp.database.DatabaseHelper;

import java.util.List;

public class AdminQuestionAdapter extends RecyclerView.Adapter<AdminQuestionAdapter.ViewHolder> {
    Context context;
    List<questions> list;
    DatabaseHelper db;

    public AdminQuestionAdapter(Context context, List<questions> list) {
        this.context = context;
        this.list = list;
        this.db = new DatabaseHelper(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        questions q = list.get(position);
        holder.tvText.setText((position + 1) + ". " + q.question_text);

        // Xóa câu hỏi
        holder.btnDelete.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(context)
                    .setTitle("Xóa câu hỏi")
                    .setMessage("Bạn có chắc muốn xóa câu hỏi này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (db.deleteQuestion(q.id)) {
                            list.remove(position);
                            notifyItemRemoved(position);         // ← fix: dùng notifyItemRemoved
                            notifyItemRangeChanged(position, list.size()); // cập nhật số thứ tự
                            Toast.makeText(context, "Đã xóa câu hỏi", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        // THÊM MỚI: Click vào item để sửa câu hỏi
        holder.itemView.setOnClickListener(v -> showEditQuestionDialog(q, position));
    }

    // Dialog sửa câu hỏi
    private void showEditQuestionDialog(questions q, int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_add_question, null);

        EditText etQues = v.findViewById(R.id.etQuestionContent);
        EditText etAns1 = v.findViewById(R.id.etAns1);
        EditText etAns2 = v.findViewById(R.id.etAns2);
        EditText etAns3 = v.findViewById(R.id.etAns3);
        EditText etAns4 = v.findViewById(R.id.etAns4);
        RadioButton rb1 = v.findViewById(R.id.rbAns1);
        RadioButton rb2 = v.findViewById(R.id.rbAns2);
        RadioButton rb3 = v.findViewById(R.id.rbAns3);
        RadioButton rb4 = v.findViewById(R.id.rbAns4);

        // Điền dữ liệu cũ vào form
        etQues.setText(q.question_text);

        List<answers> ansList = db.getAllAnswerByQuestionID(String.valueOf(q.id));
        RadioButton[] rbs = {rb1, rb2, rb3, rb4};
        EditText[] ets = {etAns1, etAns2, etAns3, etAns4};

        for (int i = 0; i < ansList.size() && i < 4; i++) {
            ets[i].setText(ansList.get(i).answer_text);
            rbs[i].setChecked(ansList.get(i).is_correct == 1);
        }

        new AlertDialog.Builder(context)
                .setTitle("Sửa câu hỏi")
                .setView(v)
                .setPositiveButton("Lưu lại", (dialog, which) -> {
                    String newText = etQues.getText().toString().trim();
                    if (newText.isEmpty()) {
                        Toast.makeText(context, "Nội dung không được để trống", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Xóa câu hỏi cũ (kéo theo xóa answers cũ nhờ ON DELETE CASCADE)
                    // rồi insert lại với nội dung mới
                    int quizId = q.quiz_id;
                    db.deleteQuestion(q.id);

                    long newQId = db.insertQuestion(quizId, newText, q.explanation, q.difficulty);
                    db.insertAnswer(newQId, etAns1.getText().toString().trim(), rb1.isChecked() ? 1 : 0);
                    db.insertAnswer(newQId, etAns2.getText().toString().trim(), rb2.isChecked() ? 1 : 0);
                    db.insertAnswer(newQId, etAns3.getText().toString().trim(), rb3.isChecked() ? 1 : 0);
                    db.insertAnswer(newQId, etAns4.getText().toString().trim(), rb4.isChecked() ? 1 : 0);

                    // Cập nhật object trong list
                    q.question_text = newText;
                    list.set(position, q);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Đã cập nhật câu hỏi!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;
        View btnDelete;

        public ViewHolder(View v) {
            super(v);
            tvText = v.findViewById(R.id.tvQuestionText);
            btnDelete = v.findViewById(R.id.imgDeleteQuestion);
        }
    }
}