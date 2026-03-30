package com.example.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.quizapp.class_package.categories;
import com.example.quizapp.database.DatabaseHelper;
import com.example.quizapp.xulydulieu.AdminCategoryAdapter;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    private TextView tvAdminName, tvCountUser, tvCountCategory;
    private ShapeableImageView imgAdminProfile;
    private RecyclerView recyclerView;
    private View cardQuickAction;

    private AdminCategoryAdapter categoryAdapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_layout_main);

        db = new DatabaseHelper(this);

        initViews();
        displayAdminInfo();
        updateDashboard();
        setupClickListeners();
    }

    private void initViews() {
        tvAdminName = findViewById(R.id.tvAdminName);
        tvCountUser = findViewById(R.id.tvCountUserAdmin);
        tvCountCategory = findViewById(R.id.tvCountCategoryAdmin);
        imgAdminProfile = findViewById(R.id.imgAdminProfile);
        recyclerView = findViewById(R.id.recyclerAdminCategories);
        cardQuickAction = findViewById(R.id.cardQuickAction); // Nút tạo mới
    }

    // Hàm cập nhật cả con số và danh sách RecyclerView
    private void updateDashboard() {
        // Cập nhật con số thống kê
        int totalStudents = db.getStudentCount();
        int totalCategories = db.getCategoryCount();

        if (tvCountUser != null) tvCountUser.setText(String.valueOf(totalStudents));
        if (tvCountCategory != null) tvCountCategory.setText(String.valueOf(totalCategories));

        // Cập nhật danh sách RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<categories> listCategories = db.getAllCategories();
        categoryAdapter = new AdminCategoryAdapter(this, listCategories);
        recyclerView.setAdapter(categoryAdapter);

    }

    private void displayAdminInfo() {
        SharedPreferences prefs = getSharedPreferences("USER", MODE_PRIVATE);
        String name = prefs.getString("username", "Admin Hệ Thống");
        tvAdminName.setText(name);
    }

    private void setupClickListeners() {
        imgAdminProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminProfileActivity.class));
        });

        cardQuickAction.setOnClickListener(v -> showAddCategoryDialog());
    }

    // Hiển thị Dialog thêm Category mới
    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText etName = dialogView.findViewById(R.id.etCategoryName);
        EditText etDesc = dialogView.findViewById(R.id.etCategoryDesc);
        Button btnSave = dialogView.findViewById(R.id.btnSaveCategory);

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("Không được để trống");
                return;
            }

            // Gọi hàm insert từ DatabaseHelper
            long result = db.insertCategory(name, desc);
            if (result != -1) {
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                updateDashboard();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Lỗi khi thêm danh mục", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDashboard();
    }
}