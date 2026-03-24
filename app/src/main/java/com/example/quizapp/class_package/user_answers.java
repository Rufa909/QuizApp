package com.example.quizapp.class_package;

public class user_answers {
    public int id;
    public int attempt_id;
    public int question_id;
    public int selected_answer_id;
    public int is_correct;

    public user_answers() {}

    public user_answers(int attempt_id, int question_id, int selected_answer_id, int is_correct) {
        this.attempt_id = attempt_id;
        this.question_id = question_id;
        this.selected_answer_id = selected_answer_id;
        this.is_correct = is_correct;
    }
}