package com.example.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.class_package.quizzes;
import com.example.quizapp.database.DatabaseHelper;
import com.example.quizapp.xulydulieu.QuizAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private int category_id;
    private DatabaseHelper db;
    private QuizAdapter adapter_quizz;
    private RecyclerView recyclerView;
    private List<quizzes> list_quizess = new ArrayList<>(); // Khởi tạo list rỗng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        db = new DatabaseHelper(this);

        // Ánh xạ View
        recyclerView = findViewById(R.id.recyclerQuiz);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Thiết lập Adapter ngay từ đầu
        adapter_quizz = new QuizAdapter(this, list_quizess);
        recyclerView.setAdapter(adapter_quizz);

        // Lấy category_id từ Intent
        category_id = getIntent().getIntExtra("category_id", -1);
        Log.d("QuizActivity_Debug", "ID nhận được: " + category_id);

        if (category_id == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID danh mục!", Toast.LENGTH_SHORT).show();
        }

        ImageButton btn_back = findViewById(R.id.btnBack);
        btn_back.setOnClickListener(v -> finish());

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (category_id != -1) {
            List<quizzes> dataFromServer = db.getAllQuizByCateID(String.valueOf(category_id));
            if (dataFromServer != null) {
                list_quizess.clear();
                list_quizess.addAll(dataFromServer);
                adapter_quizz.notifyDataSetChanged();
                Log.d("QuizActivity_Debug", "Số lượng câu hỏi: " + list_quizess.size());
            }
        }
    }
}