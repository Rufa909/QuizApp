package com.example.bai4;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtName, edtPhone;
    RadioButton rbMale;
    Button btnAdd;
    ListView lvContact;
    ArrayList<Contact> arrayList;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        rbMale = findViewById(R.id.rbMale);
        btnAdd = findViewById(R.id.btnAdd);
        lvContact = findViewById(R.id.lvContact);

        arrayList = new ArrayList<>();
        adapter = new ContactAdapter(this, R.layout.row_contact, arrayList);
        lvContact.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();
                boolean isMale = rbMale.isChecked();

                if (!name.isEmpty() && !phone.isEmpty()) {
                    arrayList.add(new Contact(name, phone, isMale));
                    adapter.notifyDataSetChanged();
                    edtName.setText("");
                    edtPhone.setText("");
                    edtName.requestFocus();
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đủ tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}