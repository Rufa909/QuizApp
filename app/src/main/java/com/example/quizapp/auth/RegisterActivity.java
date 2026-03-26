package com.example.quizapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizapp.R;
import com.example.quizapp.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText edtUser, edtPass;
    Button btnRegister, btnGoLogin;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        setContentView(R.layout.activity_register);

        edtUser = findViewById(R.id.edUser);
        edtPass = findViewById(R.id.edPass);

        btnGoLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        DatabaseHelper db = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {
            String user = edtUser.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Nhập đủ đi", Toast.LENGTH_SHORT).show();
                return;
            }

            if(db.registerUser(user, pass)){
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Đã có tài khoản vui lòng đăng nhập", Toast.LENGTH_LONG).show();
            }
        });
        btnGoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
