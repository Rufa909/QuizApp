package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.auth.LoginActivity;
import com.example.quizapp.database.DatabaseHelper;

public class AdminProfileActivity extends AppCompatActivity {
    TextView tv_username;
    ImageView btnback;
    Button btngLogout;
    RelativeLayout btnEditProfile;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_admin_profile);

        // Khởi tạo Database và Preferences
        db = new DatabaseHelper(this);
        SharedPreferences pref = getSharedPreferences("USER", MODE_PRIVATE);

        // Lấy thông tin từ SharedPref
        int userId = pref.getInt("user_id", -1);
        String username = pref.getString("username", "Admin");

        // Ánh xạ View
        tv_username = findViewById(R.id.adminName_Profile);
        btnback = findViewById(R.id.btnBack_AdminProfile);
        btngLogout = findViewById(R.id.btnLogoutAdmin);
        btnEditProfile = findViewById(R.id.layoutEditProfile);

        tv_username.setText(username);

        // Nút quay lại
        btnback.setOnClickListener(v -> finish());

        // Chức năng đổi tên
        btnEditProfile.setOnClickListener(v -> {
            showEditNameDialog(userId, pref);
        });

        // Chức năng đăng xuất
        btngLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(AdminProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void showEditNameDialog(int userId, SharedPreferences pref) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thay đổi tên hiển thị");

        // Tạo EditText để nhập tên
        final EditText input = new EditText(this);
        input.setHint("Nhập tên mới...");
        input.setPadding(50, 40, 50, 40);
        input.setText(tv_username.getText().toString());
        builder.setView(input);

        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (newName.isEmpty()) {
                Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
            } else {
                updateAdminName(userId, newName, pref);
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateAdminName(int userId, String newName, SharedPreferences pref) {
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", newName);

        // Cập nhật vào bảng users
        int rows = database.update("users", values, "id = ?", new String[]{String.valueOf(userId)});

        if (rows > 0) {
            // Cập nhật giao diện
            tv_username.setText(newName);

            // Cập nhật SharedPreferences để lưu trạng thái phiên đăng nhập
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username", newName);
            editor.apply();

            Toast.makeText(this, "Đổi tên thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID người dùng!", Toast.LENGTH_SHORT).show();
        }
    }
}