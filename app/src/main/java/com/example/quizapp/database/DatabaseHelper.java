package com.example.quizapp.database;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "quiz.db", null, 2);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL" +
                ")");

        db.execSQL("CREATE TABLE categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT" +
                ")");

        db.execSQL("CREATE TABLE quizzes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "category_id INTEGER, " +
                "difficulty TEXT, " +
                "created_by INTEGER, " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE SET NULL, " +
                "FOREIGN KEY(created_by) REFERENCES users(id) ON DELETE SET NULL" +
                ")");

        db.execSQL("CREATE TABLE questions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "quiz_id INTEGER NOT NULL, " +
                "question_text TEXT NOT NULL, " +
                "explanation TEXT, " +
                "difficulty TEXT, " +
                "FOREIGN KEY(quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE" +
                ")");

        db.execSQL("CREATE TABLE answers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "question_id INTEGER NOT NULL, " +
                "answer_text TEXT NOT NULL, " +
                "is_correct INTEGER DEFAULT 0, " +
                "FOREIGN KEY(question_id) REFERENCES questions(id) ON DELETE CASCADE" +
                ")");

        db.execSQL("CREATE TABLE quiz_attempts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "quiz_id INTEGER, " +
                "score INTEGER, " +
                "total_questions INTEGER, " +
                "started_at TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "finished_at TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE SET NULL, " +
                "FOREIGN KEY(quiz_id) REFERENCES quizzes(id) ON DELETE SET NULL" +
                ")");

        db.execSQL("CREATE TABLE user_answers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "attempt_id INTEGER, " +
                "question_id INTEGER, " +
                "selected_answer_id INTEGER, " +
                "is_correct INTEGER DEFAULT 0, " +
                "FOREIGN KEY(attempt_id) REFERENCES quiz_attempts(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(question_id) REFERENCES questions(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(selected_answer_id) REFERENCES answers(id) ON DELETE SET NULL" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user_answers");
        db.execSQL("DROP TABLE IF EXISTS quiz_attempts");
        db.execSQL("DROP TABLE IF EXISTS answers");
        db.execSQL("DROP TABLE IF EXISTS questions");
        db.execSQL("DROP TABLE IF EXISTS quizzes");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public int loginUser(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "select id from users where username = ? and password = ?",
                new String[]{username, password}
        );

        if(cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            cursor.close();
            return userId;
        }

        cursor.close();
        return -1;
    }

    public boolean registerUser(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "select id from users where username = ?",
                new String[]{username}
        );

        if(cursor.moveToFirst()){
            cursor.close();
            return false;
        }

        cursor.close();

        db.execSQL(
                "Insert into users(username, password) values(?, ?)",
                new Object[] {username, password}
        );

        return true;
    }
}