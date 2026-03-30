package com.example.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.class_package.answers;
import com.example.quizapp.class_package.questions;
import com.example.quizapp.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {

    TextView tvQuestion, tvQuestionNumber;
    RadioButton rbA, rbB, rbC, rbD;
    LinearLayout layoutA, layoutB, layoutC, layoutD;
    Button btnPrev, btnNext, btnSubmit;

    List<questions> list_question = new ArrayList<>();
    int currentIndex = 0;

    DatabaseHelper db;

    // Lưu đáp án người dùng chọn (question_id -> index 0-3)
    Map<Integer, Integer> userAnswer = new HashMap<>();
    // Lưu trạng thái đã trả lời chưa (question_id -> true/false)
    Map<Integer, Boolean> answeredMap = new HashMap<>();

    List<answers> currentAnswer;

    // Màu sắc
    private static final int COLOR_CORRECT   = 0xFFDFF5E3; // xanh nhạt
    private static final int COLOR_WRONG     = 0xFFFFE0E0; // đỏ nhạt
    private static final int COLOR_DEFAULT   = 0xFFFFFFFF; // trắng
    private static final int COLOR_CORRECT_BORDER = 0xFF27AE60;
    private static final int COLOR_WRONG_BORDER   = 0xFFE74C3C;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_question);

        tvQuestion      = findViewById(R.id.tvQuestion);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);

        rbA = findViewById(R.id.rbA);
        rbB = findViewById(R.id.rbB);
        rbC = findViewById(R.id.rbC);
        rbD = findViewById(R.id.rbD);

        // Layout cha của từng RadioButton (LinearLayout bọc label + radiobutton)
        layoutA = findViewById(R.id.layoutA);
        layoutB = findViewById(R.id.layoutB);
        layoutC = findViewById(R.id.layoutC);
        layoutD = findViewById(R.id.layoutD);

        btnNext   = findViewById(R.id.btnNext);
        btnPrev   = findViewById(R.id.btnPrev);
        btnSubmit = findViewById(R.id.btnSubmit);

        db = new DatabaseHelper(this);

        int quiz_id = getIntent().getIntExtra("quiz_id", -1);
        list_question = db.getAllQuestionByQuizzId(String.valueOf(quiz_id));

        if (list_question == null || list_question.size() == 0) {
            Toast.makeText(this, "Không có câu hỏi!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        layoutA.setOnClickListener(v -> handleSelectAnswer(0));
        layoutB.setOnClickListener(v -> handleSelectAnswer(1));
        layoutC.setOnClickListener(v -> handleSelectAnswer(2));
        layoutD.setOnClickListener(v -> handleSelectAnswer(3));

        // Click trực tiếp vào RadioButton
        rbA.setOnClickListener(v -> handleSelectAnswer(0));
        rbB.setOnClickListener(v -> handleSelectAnswer(1));
        rbC.setOnClickListener(v -> handleSelectAnswer(2));
        rbD.setOnClickListener(v -> handleSelectAnswer(3));

        showQuestion();

        btnNext.setOnClickListener(v -> {
            if (currentIndex < list_question.size() - 1) {
                currentIndex++;
                showQuestion();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showQuestion();
            }
        });

        btnSubmit.setOnClickListener(v -> calScore());
    }

    /**
     * Xử lý khi người dùng chọn đáp án.
     * Nếu câu này đã trả lời rồi thì bỏ qua.
     */
    private void handleSelectAnswer(int index) {
        questions q = list_question.get(currentIndex);

        // Đã trả lời rồi → không cho chọn lại
        if (answeredMap.containsKey(q.id)) return;

        // Lưu đáp án
        userAnswer.put(q.id, index);
        answeredMap.put(q.id, true);

        // Hiển thị highlight đúng/sai
        highlightAnswers(index);
    }

    /**
     * Highlight màu xanh/đỏ sau khi chọn.
     * Luôn hiển thị đáp án đúng màu xanh.
     * Nếu chọn sai thì lựa chọn của người dùng màu đỏ.
     */
    private void highlightAnswers(int selectedIndex) {
        if (currentAnswer == null || currentAnswer.size() < 4) return;

        LinearLayout[] layouts = {layoutA, layoutB, layoutC, layoutD};

        for (int i = 0; i < 4; i++) {
            boolean isCorrect  = currentAnswer.get(i).is_correct == 1;
            boolean isSelected = (i == selectedIndex);

            if (isCorrect) {
                // Đáp án đúng → xanh
                setOptionBackground(layouts[i], COLOR_CORRECT, COLOR_CORRECT_BORDER);
            } else if (isSelected) {
                // Người dùng chọn sai → đỏ
                setOptionBackground(layouts[i], COLOR_WRONG, COLOR_WRONG_BORDER);
            } else {
                // Các lựa chọn còn lại → giữ nguyên
                setOptionBackground(layouts[i], COLOR_DEFAULT, 0xFFE0E0E0);
            }
        }

        // Lock radio buttons (không cho click thêm)
        setAnswerEnabled(false);
    }

    private void setOptionBackground(LinearLayout layout, int bgColor, int borderColor) {
        // Đổi màu nền của LinearLayout
        layout.setBackgroundColor(bgColor);
        // Nếu bạn muốn đổi cả border, cần custom drawable lúc runtime.
        // Đây là cách đơn giản nhất với background color:
        layout.setBackgroundColor(bgColor);
    }

    private void setAnswerEnabled(boolean enabled) {
        rbA.setEnabled(enabled);
        rbB.setEnabled(enabled);
        rbC.setEnabled(enabled);
        rbD.setEnabled(enabled);
        layoutA.setClickable(enabled);
        layoutB.setClickable(enabled);
        layoutC.setClickable(enabled);
        layoutD.setClickable(enabled);
    }

    private void showQuestion() {
        questions q = list_question.get(currentIndex);

        tvQuestion.setText("Câu " + (currentIndex + 1) + ": " + q.question_text);
        tvQuestionNumber.setText((currentIndex + 1) + " / " + list_question.size());

        currentAnswer = db.getAllAnswerByQuestionID(String.valueOf(q.id));

        if (currentAnswer == null || currentAnswer.size() < 4) {
            Toast.makeText(this, "Câu hỏi chưa đủ đáp án!", Toast.LENGTH_SHORT).show();
            return;
        }

        rbA.setText(currentAnswer.get(0).answer_text);
        rbB.setText(currentAnswer.get(1).answer_text);
        rbC.setText(currentAnswer.get(2).answer_text);
        rbD.setText(currentAnswer.get(3).answer_text);

        // Reset màu nền về mặc định
        resetOptionColors();

        // Nếu câu này đã trả lời → khôi phục highlight & lock
        if (answeredMap.containsKey(q.id)) {
            int savedIndex = userAnswer.get(q.id);
            selectRadio(savedIndex);
            highlightAnswers(savedIndex);
        } else {
            // Chưa trả lời → bỏ chọn, cho phép click
            clearSelection();
            setAnswerEnabled(true);
        }

        // Disable nút Next nếu đang ở câu cuối
        if (currentIndex == list_question.size() - 1) {
            btnNext.setEnabled(false);
            btnNext.setAlpha(0.4f); // Làm mờ để người dùng biết đã disabled
        } else {
            btnNext.setEnabled(true);
            btnNext.setAlpha(1.0f);
        }

        // Disable nút Prev nếu đang ở câu đầu
        if (currentIndex == 0) {
            btnPrev.setEnabled(false);
            btnPrev.setAlpha(0.4f);
        } else {
            btnPrev.setEnabled(true);
            btnPrev.setAlpha(1.0f);
        }
    }

    private void resetOptionColors() {
        int defaultColor = COLOR_DEFAULT;
        layoutA.setBackgroundColor(defaultColor);
        layoutB.setBackgroundColor(defaultColor);
        layoutC.setBackgroundColor(defaultColor);
        layoutD.setBackgroundColor(defaultColor);
    }

    private void selectRadio(int index) {
        clearSelection();
        if (index == 0) rbA.setChecked(true);
        if (index == 1) rbB.setChecked(true);
        if (index == 2) rbC.setChecked(true);
        if (index == 3) rbD.setChecked(true);
    }

    private void clearSelection() {
        rbA.setChecked(false);
        rbB.setChecked(false);
        rbC.setChecked(false);
        rbD.setChecked(false);
    }

    private void calScore() {
        int score = 0;

        for (questions q : list_question) {
            List<answers> ans = db.getAllAnswerByQuestionID(String.valueOf(q.id));

            if (userAnswer.containsKey(q.id)) {
                int userChoice = userAnswer.get(q.id);
                if (ans != null && ans.size() > userChoice && ans.get(userChoice).is_correct == 1) {
                    score++;
                }
            }
        }

        int quiz_id = getIntent().getIntExtra("quiz_id", -1);
        SharedPreferences pref = getSharedPreferences("USER", MODE_PRIVATE);
        int user_id = pref.getInt("user_id", -1);

        db.saveUserProcess(user_id, quiz_id, score, list_question.size());

        Intent intent = new Intent(QuestionActivity.this, ResultScoreActivity.class);
        intent.putExtra("final_score", score);
        intent.putExtra("total_question", list_question.size());
        startActivity(intent);
    }
}