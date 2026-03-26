package com.example.quizapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.example.quizapp.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        SharedPreferences prefs = getSharedPreferences("USER", MODE_PRIVATE);
        int userId = prefs.getInt("id", -1);
        Log.d("user",String.valueOf(userId));
        if (userId != -1) {
            // Nếu đã login → chuyển qua HomeActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            // Chưa login → chuyển qua MainActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }
}
