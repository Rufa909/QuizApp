package com.example.quizapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton; // Thêm import ImageButton
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizapp.class_package.quiz_attempts;
import com.example.quizapp.database.DatabaseHelper;
import com.example.quizapp.xulydulieu.HistoryAdapter;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // 1. Ánh xạ ImageButton và xử lý sự kiện Back
        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 2. Lấy thông tin User từ SharedPreferences
        SharedPreferences pref = getSharedPreferences("USER", MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);

        // 3. Khởi tạo Database và lấy dữ liệu
        DatabaseHelper db = new DatabaseHelper(this);
        List<quiz_attempts> list = db.getUserAttemptsWithTitle(userId);

        // 4. Ánh xạ các View hiển thị
        RecyclerView rv = findViewById(R.id.recyclerHistory);
        TextView tvEmpty = findViewById(R.id.tvEmpty);

        // 5. Kiểm tra dữ liệu và hiển thị lên giao diện
        if (list == null || list.isEmpty()) {
            // Nếu không có dữ liệu: Hiện text thông báo, ẩn danh sách
            if (tvEmpty != null) tvEmpty.setVisibility(View.VISIBLE);
            if (rv != null) rv.setVisibility(View.GONE);
        } else {
            // Nếu có dữ liệu: Ẩn thông báo, hiện và setup RecyclerView
            if (tvEmpty != null) tvEmpty.setVisibility(View.GONE);
            if (rv != null) {
                rv.setVisibility(View.VISIBLE);
                rv.setLayoutManager(new LinearLayoutManager(this));
                rv.setAdapter(new HistoryAdapter(this, list));
            }
        }
    }
}