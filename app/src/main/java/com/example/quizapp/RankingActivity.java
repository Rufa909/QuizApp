package com.example.quizapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizapp.class_package.RankingItem;
import com.example.quizapp.database.DatabaseHelper;
import com.example.quizapp.xulydulieu.RankingAdapter;
import java.util.List;

public class RankingActivity extends AppCompatActivity {
    private DatabaseHelper db; // Khai báo biến toàn cục để quản lý

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        db = new DatabaseHelper(this); // Khởi tạo
        List<RankingItem> list = db.getGlobalRanking();

        RecyclerView rv = findViewById(R.id.recyclerRanking);
        if (rv != null && list != null) {
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(new RankingAdapter(list));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close(); // QUAN TRỌNG: Đóng kết nối khi thoát Activity
        }
    }
}