package com.example.quizapp.class_package;

public class answers {
    public int id;
    public int question_id;
    public String answer_text;
    public int is_correct; // 0 hoặc 1

    public answers() {}

    public answers(int question_id, String answer_text, int is_correct) {
        this.question_id = question_id;
        this.answer_text = answer_text;
        this.is_correct = is_correct;
    }
}