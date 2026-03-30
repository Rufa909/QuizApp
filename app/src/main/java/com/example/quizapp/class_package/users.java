package com.example.quizapp.class_package;

public class users {

    public int id;
    public String username;
    public String email;
    public String password;
    public String role;       // ADMIN / STUDENT
    public String avatar_url;
    public String created_at;

    public users() {
        this.role = "STUDENT";
    }

    public users(int id, String username, String email, String password,
                 String role, String avatar_url, String created_at) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.avatar_url = avatar_url;
        this.created_at = created_at;
    }

    public users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = "STUDENT";
    }

    public users(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}