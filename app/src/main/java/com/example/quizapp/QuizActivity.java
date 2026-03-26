package com.example.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.class_package.quizzes;
import com.example.quizapp.database.DatabaseHelper;
import com.example.quizapp.xulydulieu.QuizAdapter;

import java.util.List;

public class QuizActivity extends AppCompatActivity {
    int quizId;
    TextView tv;
    int category_id;
    DatabaseHelper db;
    QuizAdapter adapter_quizz;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_quiz);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerQuiz);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        category_id = getIntent().getIntExtra("category_id", -1);
        Log.d("", String.valueOf(category_id));
        List<quizzes> list_quizess = db.getAllQuizByCateID(String.valueOf(category_id));

        adapter_quizz = new QuizAdapter(this, list_quizess);
        recyclerView.setAdapter(adapter_quizz);

        ImageButton btn_back = findViewById(R.id.btnBack);
        btn_back.setOnClickListener(v -> {
            finish();
        });
    }
}
