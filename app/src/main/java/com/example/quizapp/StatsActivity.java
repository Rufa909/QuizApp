package com.example.quizapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.class_package.StatsData;
import com.example.quizapp.class_package.quiz_attempts;
import com.example.quizapp.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StatsActivity extends AppCompatActivity {

    private TextView tvAIResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        tvAIResult = findViewById(R.id.tvAIResult);

        SharedPreferences pref = getSharedPreferences("USER", MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);

        DatabaseHelper db = new DatabaseHelper(this);
        StatsData stats = db.getUserStats(userId);

        if (stats != null) {

            ((TextView) findViewById(R.id.tvTotalAttempts)).setText(String.valueOf(stats.totalAttempts));
            ((TextView) findViewById(R.id.tvTotalScore)).setText(String.valueOf(stats.totalScore));
            ((TextView) findViewById(R.id.tvAccuracy)).setText(stats.getAccuracyPercent() + "%");

            LinearLayout llRecent = findViewById(R.id.llRecentAttempts);

            for (quiz_attempts a : stats.recentAttempts) {
                View row = LayoutInflater.from(this).inflate(R.layout.item_history, llRecent, false);

                ((TextView) row.findViewById(R.id.tvQuizTitle))
                        .setText(a.quizTitle != null ? a.quizTitle : "Quiz");

                ((TextView) row.findViewById(R.id.tvScore))
                        .setText(a.score + "/" + a.total_questions);

                ((TextView) row.findViewById(R.id.tvDate)).setText("");

                llRecent.addView(row);
            }

            callAI(stats);
        }
    }

    private void callAI(StatsData stats) {

        tvAIResult.setText("AI đang phân tích...");

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json");

        String prompt = buildPrompt(stats);
        prompt = prompt.replace("\"", "\\\"").replace("\n", "\\n");

        String json = "{"
                + "\"model\":\"llama\","
                + "\"messages\":[{\"role\":\"user\",\"content\":\"" + prompt + "\"}]"
                + "}";

        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url("http://10.0.2.2:8033/v1/chat/completions")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        tvAIResult.setText("Lỗi: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String res = response.body().string();

                try {
                    JSONObject obj = new JSONObject(res);
                    JSONArray choices = obj.getJSONArray("choices");

                    String text = choices.getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");

                    runOnUiThread(() ->
                            tvAIResult.setText(text.trim())
                    );

                } catch (Exception e) {
                    runOnUiThread(() ->
                            tvAIResult.setText("Lỗi parse: " + res)
                    );
                }
            }
        });
    }

    private String buildPrompt(StatsData stats) {

        StringBuilder sb = new StringBuilder();

        sb.append("Dữ liệu học của user:\n");

        for (quiz_attempts q : stats.recentAttempts) {
            sb.append("- ")
                    .append(q.quizTitle)
                    .append(": ")
                    .append(q.score)
                    .append("/")
                    .append(q.total_questions)
                    .append("\n");
        }

        sb.append("\nTổng điểm: ")
                .append(stats.totalScore)
                .append("/")
                .append(stats.totalQuestions);

        sb.append("\n\nHãy trả lời theo format:\n");
        sb.append("Năng lực: ...\n");
        sb.append("Điểm mạnh: ...\n");
        sb.append("Điểm yếu: ...\n");
        sb.append("Gợi ý: ...\n");
        sb.append("Viết ngắn gọn, dễ hiểu.");

        return sb.toString();
    }
}