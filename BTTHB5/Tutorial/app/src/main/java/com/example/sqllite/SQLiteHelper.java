package com.example.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "QLSinhVien";
    public static final int VERSION= 1;
    public static final String ID = "id";
    public static final String HO_VA_TEN = "ho_va_ten";
    public static final String TABLE_SINHVIEN = "table_sinhvien";
    public static final String CREATE_TABLE = "create table " + TABLE_SINHVIEN + "  ( " + ID + " integer primary key autoincrement , " + HO_VA_TEN + " text not null )";

    public SQLiteHelper(Context context, String databasename, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databasename, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE );
        onCreate(sqLiteDatabase);
    }

}

