package com.example.quizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    TextView tvQuestion;
    RadioButton rbA, rbB, rbC, rbD;
    Button btnPrev, btnNext, btnSubmit;
    Context context;
    List<questions> list_question = new ArrayList<>();
    int currentIndex = 0;
    DatabaseHelper db;
    Map<Integer, Integer> userAnswer = new HashMap<>();
    List<answers> test_list;
    List<answers> currentAnswer;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_question);

        tvQuestion = findViewById(R.id.tvQuestion);
        rbA = findViewById(R.id.rbA);
        rbB = findViewById(R.id.rbB);
        rbC = findViewById(R.id.rbC);
        rbD = findViewById(R.id.rbD);

        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnSubmit = findViewById(R.id.btnSubmit);

        db = new DatabaseHelper(this);
        test_list = db.getTestAnswer();
        for (answers a : test_list) {
            Log.d("DEBUG_ANSWER",
                    "ID=" + a.id +
                            ", question_id=" + a.question_id +
                            ", text=" + a.answer_text +
                            ", correct=" + a.is_correct
            );
        }
        int quiz_id = getIntent().getIntExtra("quiz_id", -1);
        Log.d("Debug_id_quiz", "ID QUIZ" + quiz_id);

        list_question = db.getAllQuestionByQuizzId(String.valueOf(quiz_id));
        for (questions q : list_question) {
            Log.d("DEBUG_QUESTION",
                    "ID=" + q.id +
                            ", quiz_id=" + q.quiz_id +
                            ", text=" + q.question_text
            );
        }
        showQuestion();

        btnNext.setOnClickListener(v -> {
            saveAnswer();
            if(currentIndex < list_question.size() - 1){
                currentIndex++;
                showQuestion();
            }
        });

        btnPrev.setOnClickListener(v -> {
            saveAnswer();
            if(currentIndex > 0){
                currentIndex--;
                showQuestion();
            }
        });

        btnSubmit.setOnClickListener(v -> {
            saveAnswer();
            calScore();
        });
    }

    private void showQuestion(){
        questions q = list_question.get(currentIndex);

        tvQuestion.setText("Câu" + (currentIndex + 1) + ":" + q.question_text);

        currentAnswer = db.getAllAnswerByQuestionID(String.valueOf(q.id));
        for (answers ans : currentAnswer) {
            Log.d("DEBUG_QUESTION",
                    "ID=" + q.id +
                            ", quiz_id=" + ans.id +
                            ", text=" + ans.answer_text
            );
        }

        Log.d("DEBUG", "Question ID: " + q.id);
        Log.d("DEBUG", "Answers size: " + currentAnswer.size());

        rbA.setText("A. " + currentAnswer.get(0).answer_text);
        rbB.setText("B. " + currentAnswer.get(1).answer_text);
        rbC.setText("C. " + currentAnswer.get(2).answer_text);
        rbD.setText("D. " + currentAnswer.get(3).answer_text);

        RadioGroup group = findViewById(R.id.radioGroup);
        group.clearCheck();

        if(userAnswer.containsKey(q.id)){
            int selected = userAnswer.get(q.id);

            if(selected == 0) rbA.setChecked(true);
            if(selected == 1) rbB.setChecked(true);
            if(selected == 2) rbC.setChecked(true);
            if(selected == 3) rbD.setChecked(true);
        }
    }

    private void saveAnswer(){
        questions q = list_question.get(currentIndex);

        int selected = -1;
        if(rbA.isChecked()) selected = 0;
        if(rbB.isChecked()) selected = 1;
        if(rbC.isChecked()) selected = 2;
        if(rbD.isChecked()) selected = 3;

        if(selected != -1){
            userAnswer.put(q.id, selected);
        }
    }
    private void calScore(){
        int score = 0;
        for(questions q : list_question){
            List<answers> answers = db.getAllAnswerByQuestionID(String.valueOf(q.id));

            if(userAnswer.containsKey(q.id)){
                int userchoice = userAnswer.get(q.id);

                if(answers.get(userchoice).is_correct == 1){
                    score++;
                }
            }
        }
        int quiz_id = getIntent().getIntExtra("quiz_id", -1);
        SharedPreferences pref = getSharedPreferences("USER", MODE_PRIVATE);
        int user_id = pref.getInt("user_id", - 1);
        db.saveUserProcess(user_id, quiz_id, score, list_question.size());

        Toast.makeText(this, "Điểm: " + score + "/" + list_question.size(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(QuestionActivity.this, ResultScoreActivity.class);
        intent.putExtra("final_score", score);
        intent.putExtra("total_question", list_question.size());
        startActivity(intent);
    }
}
