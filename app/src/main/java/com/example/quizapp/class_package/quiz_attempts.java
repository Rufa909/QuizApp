package com.example.quizapp.class_package;

public class quiz_attempts {
    public int id;
    public int user_id;
    public int quiz_id;
    public int score;
    public int total_questions;
    public String started_at;
    public String finished_at;
    public String quizTitle;

    public quiz_attempts() {}

    public quiz_attempts(int user_id, int quiz_id, int score, int total_questions) {
        this.user_id = user_id;
        this.quiz_id = quiz_id;
        this.score = score;
        this.total_questions = total_questions;
    }
}