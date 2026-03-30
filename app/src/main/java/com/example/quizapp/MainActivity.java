package com.example.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable; // Thêm thư viện này
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView; // Thay đổi từ Lottie sang ImageView
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.class_package.categories;
import com.example.quizapp.class_package.quiz_attempts;
import com.example.quizapp.database.DatabaseHelper;
import com.example.quizapp.xulydulieu.CategoryAdapter;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int userid;
    private String username;

    TextView userName, tvProgress;
    RecyclerView recycleView;

    CategoryAdapter adapter_categories;
    DatabaseHelper db;
    ImageView find_penguin;
    AnimationDrawable penguinAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DatabaseHelper(this);

        SharedPreferences pref = getSharedPreferences("USER", MODE_PRIVATE);
        userid = pref.getInt("user_id", -1);
        username = pref.getString("username", "");

        userName = findViewById(R.id.tvUserName);
        userName.setText(username);

        db = new DatabaseHelper(this);

        tvProgress = findViewById(R.id.tvProgress);

        List<quiz_attempts> attempts = db.getUserAttempts(userid);

        if (attempts == null || attempts.size() == 0) {
            tvProgress.setText("Chưa có tiến trình");
        } else {
            int totalScore = 0;
            for (quiz_attempts a : attempts) {
                totalScore += a.score;
            }
            tvProgress.setText("Đã làm " + attempts.size() + " quiz, tổng điểm: " + totalScore);
        }

        ShapeableImageView imgProfile = findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
        });

        find_penguin = findViewById(R.id.animPenguin);

        if (find_penguin.getDrawable() instanceof AnimationDrawable) {
            penguinAnimation = (AnimationDrawable) find_penguin.getDrawable();

            find_penguin.post(() -> {
                if (penguinAnimation != null) {
                    penguinAnimation.start();
                }
            });
        }

        recycleView = findViewById(R.id.recyclerCategories);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        List<categories> list_categories = db.getAllCategories();

        adapter_categories = new CategoryAdapter(this, list_categories);
        recycleView.setAdapter(adapter_categories);

        View navView = findViewById(R.id.layout_navigation);
        BottomNavHelper.setup(this, navView, "home");
    }
}