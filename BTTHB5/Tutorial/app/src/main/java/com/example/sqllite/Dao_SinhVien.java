package com.example.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;

public class Dao_SinhVien {
    private SQLiteHelper dbHelper;
    private SQLiteDatabase database;

    private void open() {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }
    public Dao_SinhVien(Context context) {
        dbHelper = new SQLiteHelper(context,SQLiteHelper.DATABASE_NAME,null,SQLiteHelper.VERSION);
    }


    public SinhVien getSinhVien(int id) {
        open();
        Cursor cursor = database.query(SQLiteHelper.TABLE_SINHVIEN, new String[]{SQLiteHelper.ID, SQLiteHelper.HO_VA_TEN}, SQLiteHelper.ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        SinhVien obj = null;
        while (!cursor.isAfterLast()) {
            obj = set(cursor);
            cursor.moveToNext();
        }
        close();
        return obj;
    }

    public LinkedList<SinhVien> getAllSinhVien(int orderby_status) {
        open();
        LinkedList<SinhVien> ls = new LinkedList<>();
        Cursor cursor = database.query(SQLiteHelper.TABLE_SINHVIEN, new String[]{SQLiteHelper.ID, SQLiteHelper.HO_VA_TEN}, null, null, null, null, (orderby_status == 0)? null : SQLiteHelper.ID+" DESC " );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SinhVien obj = set(cursor);
            ls.add(obj);
            cursor.moveToNext();
        }
        close();
        return ls;
    }

    public boolean updateSinhVien(int id, String new_hovaten)
    {
        open();
        ContentValues contentValue =new ContentValues();
        contentValue.put(SQLiteHelper.HO_VA_TEN,new_hovaten);
        int result =database.update(SQLiteHelper.TABLE_SINHVIEN,contentValue,SQLiteHelper.ID+ " = ?",new String[]{String.valueOf(id)});
        return result !=-1;
    }

    public void deleteall() {
        open();
        database.delete(SQLiteHelper.TABLE_SINHVIEN, null, null);
        close();
    }

    public void deleteSinhVien(int id) {
        open();
        database.delete(SQLiteHelper.TABLE_SINHVIEN, SQLiteHelper.ID +" = ?", new String[]{String.valueOf(id)});
        close();
    }

    public boolean insert_SinhVien(SinhVien sinhVien) {
        // deleteall();
        long result = 0;
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.HO_VA_TEN, sinhVien.getHovaten());
        result = database.insert(SQLiteHelper.TABLE_SINHVIEN, null, contentValues);
        close();
        return result != -1;
    }
    private SinhVien set(Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.ID));
        String hovaten = cursor.getString(cursor.getColumnIndex(SQLiteHelper.HO_VA_TEN));
        SinhVien obj = new SinhVien(id, hovaten);
        return obj;
    }

}
