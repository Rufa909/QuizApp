package com.example.quizapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.class_package.categories;
import com.example.quizapp.class_package.quiz_attempts;
import com.example.quizapp.class_package.quizzes;
import com.example.quizapp.database.DatabaseHelper;
import com.example.quizapp.xulydulieu.CategoryAdapter;
import com.example.quizapp.xulydulieu.QuizAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int userid;
    private String username;
    TextView userName;

    RecyclerView recycleView;
    QuizAdapter adapter_quiz;
    CategoryAdapter adapter_categories;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences pref = getSharedPreferences("USER", MODE_PRIVATE);
        userid = pref.getInt("user_id", -1);
        username = pref.getString("username", "");

        //lay thong tin nguoi dung gan vao text
        userName = findViewById(R.id.tvUserName);
        userName.setText(username);

        TextView tvProgress = findViewById(R.id.tvProgress);

        List<quiz_attempts> attempts = db.getUserAttempts(userid);

        if (attempts.size() == 0) {
            tvProgress.setText("Chưa có tiến trình");
        } else {
            // Ví dụ hiển thị tổng điểm / số quiz đã làm
            int totalScore = 0;
            for (quiz_attempts a : attempts) {
                totalScore += a.score;
            }
            tvProgress.setText("Đã làm " + attempts.size() + " quiz, tổng điểm: " + totalScore);
        }

        recycleView = findViewById(R.id.recyclerCategories);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);
        List<categories> list_categories = db.getAllCategories();

        adapter_categories = new CategoryAdapter(this, list_categories);

        recycleView.setAdapter(adapter_categories);
    }
}
