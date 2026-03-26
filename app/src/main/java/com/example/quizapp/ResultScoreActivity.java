package com.example.quizapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.database.DatabaseHelper;

public class ResultScoreActivity extends AppCompatActivity {
    Context context;
    int Score;
    DatabaseHelper db;
    Button btnHome;
    TextView tvMessage, tvScore;
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        setContentView(R.layout.activity_result_score);

        tvScore = findViewById(R.id.tvScore);
        tvMessage = findViewById(R.id.tvMessage);
        btnHome = findViewById(R.id.btnHome);

        int score = getIntent().getIntExtra("final_score", 0);
        int total = getIntent().getIntExtra("total_question", 0);

        tvMessage.setText("Điểm của bạn" + score);

        if(score == total){
            tvMessage.setText("Xuất sắc");
        } else if(score >= total / 2){
            tvMessage.setText("Khá tốt");
        } else {
            tvMessage.setText("Cần cố gắng hơn");
        }

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ResultScoreActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
