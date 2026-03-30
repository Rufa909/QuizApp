package com.example.quizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.class_package.quizzes;
import com.example.quizapp.database.DatabaseHelper;
import com.example.quizapp.xulydulieu.AdminQuizAdapter;

import java.util.List;

public class AdminQuizActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdminQuizAdapter adapter;
    private DatabaseHelper db;
    private int categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz);

        db = new DatabaseHelper(this);

        // Nhận dữ liệu từ AdminMainActivity
        categoryId = getIntent().getIntExtra("category_id", -1);
        categoryName = getIntent().getStringExtra("category_name");

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(categoryName != null ? categoryName : "Quản lý Quiz");

        recyclerView = findViewById(R.id.recyclerQuiz);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Nút thêm Quiz
        findViewById(R.id.btnAddQuiz).setOnClickListener(v -> showAddQuizDialog());

        loadQuizData();
    }

    private void loadQuizData() {
        List<quizzes> quizList = db.getAllQuizByCateID(String.valueOf(categoryId));
        adapter = new AdminQuizAdapter(this, quizList);
        recyclerView.setAdapter(adapter);
    }

    private void showAddQuizDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        EditText etTitle = v.findViewById(R.id.etCategoryName);
        EditText etDesc = v.findViewById(R.id.etCategoryDesc);
        Button btnSave = v.findViewById(R.id.btnSaveCategory);

        etTitle.setHint("Nhập tên Quiz mới");
        btnSave.setText("Tạo Quiz");

        btnSave.setOnClickListener(view -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (!title.isEmpty()) {
                // Bạn cần viết hàm db.insertQuiz(categoryId, title, desc) trong DatabaseHelper
                long result = db.insertQuiz(categoryId, title, desc, "Easy");
                if (result != -1) {
                    Toast.makeText(this, "Thêm Quiz thành công!", Toast.LENGTH_SHORT).show();
                    loadQuizData(); // Load lại list
                    dialog.dismiss();
                }
            } else {
                etTitle.setError("Tên không được để trống");
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadQuizData();
    }
}