package com.example.quizapp.class_package;

public class users {

    public int id;
    public String username;
    public String email;
    public String password;
    public String role;       // ADMIN / STUDENT
    public String avatar_url;
    public String created_at;

    // Constructor rỗng (bắt buộc)
    public users() {
    }

    // Constructor đầy đủ
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

    // Constructor khi tạo user mới (không cần id)
    public users(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}