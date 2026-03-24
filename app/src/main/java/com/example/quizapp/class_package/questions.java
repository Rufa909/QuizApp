package com.example.quizapp.class_package;

public class questions {
    public int id;
    public int quiz_id;
    public String question_text;
    public String explanation;
    public String difficulty;

    public questions() {}

    public questions(int quiz_id, String question_text, String explanation, String difficulty) {
        this.quiz_id = quiz_id;
        this.question_text = question_text;
        this.explanation = explanation;
        this.difficulty = difficulty;
    }
}