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

public class UserActivity extends AppCompatActivity {
    TextView tv_username;
    ImageView btnback;
    Button btngLogout;
    RelativeLayout btnEditUser;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_user);

        db = new DatabaseHelper(this);
        SharedPreferences pref = getSharedPreferences("USER", MODE_PRIVATE);

        // Lấy userId và username
        int userId = pref.getInt("user_id", -1);
        String username = pref.getString("username", "");

        tv_username = findViewById(R.id.userName_Profile);
        tv_username.setText(username);

        btnback = findViewById(R.id.btnBack_UserProfile);
        btngLogout = findViewById(R.id.btnLogout);
        btnEditUser = findViewById(R.id.layoutEditUser);

        btnback.setOnClickListener(v -> finish());

        // Chức năng đổi tên User
        btnEditUser.setOnClickListener(v -> {
            showEditNameDialog(userId, pref);
        });

        // Đăng xuất
        btngLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void showEditNameDialog(int userId, SharedPreferences pref) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đổi tên người dùng");

        final EditText input = new EditText(this);
        input.setHint("Nhập tên mới");
        input.setText(tv_username.getText().toString());
        input.setPadding(50, 40, 50, 40);
        builder.setView(input);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                updateUserName(userId, newName, pref);
            } else {
                Toast.makeText(this, "Vui lòng nhập tên!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateUserName(int userId, String newName, SharedPreferences pref) {
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", newName);

        // Cập nhật SQLite
        int result = database.update("users", values, "id = ?", new String[]{String.valueOf(userId)});

        if (result > 0) {
            tv_username.setText(newName);

            // Cập nhật SharedPreferences
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username", newName);
            editor.apply();

            Toast.makeText(this, "Đã cập nhật tên!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }
}