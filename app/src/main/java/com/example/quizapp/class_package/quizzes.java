package com.example.quizapp.class_package;

public class quizzes {
    public int id;
    public String title;
    public String description;
    public int category_id;
    public String difficulty;
    public int created_by;
    public String created_at;

    public quizzes() {}

    public quizzes(String title, String description, int category_id, String difficulty, int created_by) {
        this.title = title;
        this.description = description;
        this.category_id = category_id;
        this.difficulty = difficulty;
        this.created_by = created_by;
    }
}