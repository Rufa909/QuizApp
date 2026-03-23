package com.example.sqllite;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText edt_name;
    private Button btn_add;
    private LinkedList<SinhVien> mSinhViens;
    private Listview_Adapter listViewAdapter;
    private Dao_SinhVien dao_sinhVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.lv);
        edt_name = (EditText) findViewById(R.id.edt_name);
        btn_add = (Button) findViewById(R.id.btn_add);


        dao_sinhVien = new Dao_SinhVien(this);
        mSinhViens = new LinkedList<>();
        loadData(0);
        listViewAdapter = new Listview_Adapter(this, R.layout.item_sinhvien, mSinhViens);
        listView.setAdapter(listViewAdapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_name.getText().toString().length() > 2) {
                    SinhVien sinhVien = new SinhVien(edt_name.getText().toString());
                    boolean x = dao_sinhVien.insert_SinhVien(sinhVien);
                    if (x) {
                        edt_name.setText("");
                        refresh(1);
                    }
                }

            }
        });
        listViewAdapter.setHandlerButton(new Listview_Adapter.HandlerButton() {
            @Override
            public void setOnclickEdit(int positon) {

                final SinhVien sinhVien =mSinhViens.get(positon);
                View viewdialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_edit,null,false);
                AlertDialog.Builder alertDialog =new AlertDialog.Builder(MainActivity.this);
                alertDialog.setView(viewdialog);

                final EditText edt_hoten = (EditText) viewdialog.findViewById(R.id.edt_edit_name);
                edt_hoten.setText(sinhVien.getHovaten());
                alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dao_sinhVien.updateSinhVien(sinhVien.getId(),edt_hoten.getText().toString());
                        refresh(1);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

            }

            @Override
            public void setOnclickDelete(int positon) {
                final SinhVien sinhVien = mSinhViens.get(positon);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Bạn muốn xóa " + sinhVien.getHovaten() + " khỏi danh sách ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao_sinhVien.deleteSinhVien(sinhVien.getId());
                        refresh(1);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
            }
        });

    }

    private void loadData(int orderby) {

        mSinhViens = dao_sinhVien.getAllSinhVien(orderby);

    }

    private void refresh(int orderby) {
        mSinhViens.clear();
        mSinhViens.addAll(dao_sinhVien.getAllSinhVien(orderby));
        listViewAdapter.notifyDataSetChanged();

    }

}
