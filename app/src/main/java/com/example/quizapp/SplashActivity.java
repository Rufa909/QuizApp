package com.example.quizapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        SharedPreferences prefs = getSharedPreferences("USER", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        String role = prefs.getString("role", "");

        if (userId != -1) {
            Intent intent;
            if ("ADMIN".equalsIgnoreCase(role)) {
                intent = new Intent(this, AdminMainActivity.class);
            } else {
                intent = new Intent(this, MainActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
