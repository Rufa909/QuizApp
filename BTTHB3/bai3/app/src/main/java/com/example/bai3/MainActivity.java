package com.example.bai3;

import android.os.Bundle;
import android.graphics.Color;
import android.widget.ListView;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ListView lvContact;
    ArrayList<Contact> arrContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContact = findViewById(R.id.lvMaSinhVien);
        arrContact = new ArrayList<>();

        arrContact.add(new Contact("Bùi Viết Hùng Anh", "161250533502", Color.RED));
        arrContact.add(new Contact("Nguyễn Quốc Cường", "161250533405", Color.GREEN));
        arrContact.add(new Contact("Trần Duy Khánh", "151250533308", Color.BLUE));
        arrContact.add(new Contact("Lê Văn Tám", "161250533207", Color.GRAY));

        CustomAdapter customAdapter = new CustomAdapter(this, R.layout.row_listview, arrContact);

        lvContact.setAdapter(customAdapter);
    }
}