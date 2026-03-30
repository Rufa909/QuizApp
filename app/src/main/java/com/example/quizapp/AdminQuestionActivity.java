package com.example.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.class_package.questions;
import com.example.quizapp.database.DatabaseHelper;
import com.example.quizapp.xulydulieu.AdminQuestionAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.*;

public class AdminQuestionActivity extends AppCompatActivity {

    private int quizId;
    private DatabaseHelper db;
    private RecyclerView recyclerView;
    private AdminQuestionAdapter adapter;
    private TextView tvTitle;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_question);

        db = new DatabaseHelper(this);
        quizId = getIntent().getIntExtra("quiz_id", -1);
        String quizTitle = getIntent().getStringExtra("quiz_title");

        tvTitle = findViewById(R.id.tvTitle);
        btnBack = findViewById(R.id.btnBack);

        if (quizTitle != null) {
            tvTitle.setText(quizTitle);
        }

        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadQuestions();

        findViewById(R.id.btnAddQuestion).setOnClickListener(v -> showAddQuestionDialog());
    }

    private void loadQuestions() {
        List<questions> list = db.getAllQuestionByQuizzId(String.valueOf(quizId));
        adapter = new AdminQuestionAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void showAddQuestionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.dialog_add_question, null);
        builder.setView(v);

        EditText etKeyword = v.findViewById(R.id.etAIKeyword);
        EditText etQues = v.findViewById(R.id.etQuestionContent);
        EditText etAns1 = v.findViewById(R.id.etAns1);
        EditText etAns2 = v.findViewById(R.id.etAns2);
        EditText etAns3 = v.findViewById(R.id.etAns3);
        EditText etAns4 = v.findViewById(R.id.etAns4);

        RadioButton rb1 = v.findViewById(R.id.rbAns1);
        RadioButton rb2 = v.findViewById(R.id.rbAns2);
        RadioButton rb3 = v.findViewById(R.id.rbAns3);
        RadioButton rb4 = v.findViewById(R.id.rbAns4);

        // --- FIX LỖI CHỌN NHIỀU ĐÁP ÁN ---
        RadioButton[] radioButtons = {rb1, rb2, rb3, rb4};
        for (RadioButton currentRb : radioButtons) {
            currentRb.setOnClickListener(view -> {
                // Duyệt qua danh sách, nếu không phải nút vừa nhấn thì bỏ chọn
                for (RadioButton rb : radioButtons) {
                    rb.setChecked(rb == currentRb);
                }
            });
        }
        // --------------------------------

        Button btnAI = v.findViewById(R.id.btnGenerateAI);

        btnAI.setOnClickListener(view -> {
            String topic = etKeyword.getText().toString().trim();
            if (topic.isEmpty()) {
                Toast.makeText(this, "Nhập chủ đề trước!", Toast.LENGTH_SHORT).show();
                return;
            }
            generateQuestionAI(topic, etQues, etAns1, etAns2, etAns3, etAns4, rb1, rb2, rb3, rb4);
        });

        builder.setPositiveButton("Lưu lại", (dialog, which) -> {
            String qText = etQues.getText().toString().trim();
            if (!qText.isEmpty()) {
                // Kiểm tra xem đã chọn đáp án đúng chưa
                if (!rb1.isChecked() && !rb2.isChecked() && !rb3.isChecked() && !rb4.isChecked()) {
                    Toast.makeText(this, "Vui lòng chọn 1 đáp án đúng!", Toast.LENGTH_SHORT).show();
                    return;
                }

                long qId = db.insertQuestion(quizId, qText, "No explanation", "Easy");
                db.insertAnswer(qId, etAns1.getText().toString(), rb1.isChecked() ? 1 : 0);
                db.insertAnswer(qId, etAns2.getText().toString(), rb2.isChecked() ? 1 : 0);
                db.insertAnswer(qId, etAns3.getText().toString(), rb3.isChecked() ? 1 : 0);
                db.insertAnswer(qId, etAns4.getText().toString(), rb4.isChecked() ? 1 : 0);
                loadQuestions();
                Toast.makeText(this, "Đã thêm!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void generateQuestionAI(String topic,
                                    EditText etQues,
                                    EditText et1, EditText et2,
                                    EditText et3, EditText et4,
                                    RadioButton rb1, RadioButton rb2,
                                    RadioButton rb3, RadioButton rb4) {

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String jsonRequestString = "";
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "llama");

            JSONArray messages = new JSONArray();
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", "Chỉ trả về JSON duy nhất: {\"question\":\"...\",\"A\":\"...\",\"B\":\"...\",\"C\":\"...\",\"D\":\"...\",\"correct\":\"A\"}. Chủ đề: " + topic);

            messages.put(message);
            requestBody.put("messages", messages);
            jsonRequestString = requestBody.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url("http://10.0.2.2:8033/v1/chat/completions")
                .post(RequestBody.create(jsonRequestString, JSON))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(AdminQuestionActivity.this, "Lỗi kết nối AI", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(AdminQuestionActivity.this, "Server AI lỗi", Toast.LENGTH_SHORT).show());
                    return;
                }

                String res = response.body().string();
                Log.d("AI_RAW", res);

                try {
                    JSONObject obj = new JSONObject(res);
                    String content = obj.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");

                    content = content.replace("```json", "").replace("```", "").trim();

                    int start = content.indexOf("{");
                    int end = content.lastIndexOf("}");
                    if (start != -1 && end != -1) {
                        content = content.substring(start, end + 1);
                    }

                    JSONObject ai = new JSONObject(content);

                    runOnUiThread(() -> {
                        etQues.setText(ai.optString("question", ""));
                        et1.setText(ai.optString("A", ""));
                        et2.setText(ai.optString("B", ""));
                        et3.setText(ai.optString("C", ""));
                        et4.setText(ai.optString("D", ""));

                        String correct = ai.optString("correct", "A");

                        // Đặt trạng thái các RadioButton dựa trên AI trả về
                        rb1.setChecked(correct.equalsIgnoreCase("A"));
                        rb2.setChecked(correct.equalsIgnoreCase("B"));
                        rb3.setChecked(correct.equalsIgnoreCase("C"));
                        rb4.setChecked(correct.equalsIgnoreCase("D"));
                    });

                } catch (Exception e) {
                    Log.e("AI_ERROR", "Parse error: " + e.getMessage());
                    runOnUiThread(() -> Toast.makeText(AdminQuestionActivity.this, "AI phản hồi sai định dạng JSON", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}