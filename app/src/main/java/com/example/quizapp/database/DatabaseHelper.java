package com.example.quizapp.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quizapp.class_package.RankingItem;
import com.example.quizapp.class_package.StatsData;
import com.example.quizapp.class_package.answers;
import com.example.quizapp.class_package.categories;
import com.example.quizapp.class_package.questions;
import com.example.quizapp.class_package.quiz_attempts;
import com.example.quizapp.class_package.quizzes;
import com.example.quizapp.class_package.users;

import java.util.ArrayList;
import java.util.List;

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
                "password TEXT NOT NULL," +
                "email TEXT, " +
                "role TEXT DEFAULT 'STUDENT', " +
                "avatar_url TEXT" +
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
        insertSampleData(db);
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
    private void insertSampleData(SQLiteDatabase db) {

        db.execSQL("INSERT INTO users (id, username, password, role) VALUES (1, 'admin', '123', 'ADMIN')");

        db.execSQL("INSERT INTO categories (name, description) VALUES ('Java', 'Lập trình Java')");
        db.execSQL("INSERT INTO categories (name, description) VALUES ('Android', 'Lập trình Android')");

        db.execSQL("INSERT INTO quizzes (title, description, category_id, difficulty, created_by) " +
                "VALUES ('Java cơ bản', 'Quiz Java', 1, 'Easy', 1)");

        db.execSQL("INSERT INTO quizzes (title, description, category_id, difficulty, created_by) " +
                "VALUES ('Android UI', 'Quiz Android', 2, 'Medium', 1)");

        db.execSQL("INSERT INTO questions (quiz_id, question_text, explanation, difficulty) " +
                "VALUES (1, 'Java là gì?', 'Ngôn ngữ lập trình', 'Easy')");

        Cursor c1 = db.rawQuery("SELECT last_insert_rowid()", null);
        c1.moveToFirst();
        int q1Id = c1.getInt(0);
        c1.close();

        db.execSQL("INSERT INTO answers VALUES (NULL, " + q1Id + ", 'Ngôn ngữ lập trình', 1)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q1Id + ", 'Hệ điều hành', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q1Id + ", 'Database', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q1Id + ", 'Trình duyệt', 0)");

        db.execSQL("INSERT INTO questions (quiz_id, question_text, explanation, difficulty) " +
                "VALUES (1, 'JVM là gì?', 'Máy ảo Java', 'Easy')");

        Cursor c2 = db.rawQuery("SELECT last_insert_rowid()", null);
        c2.moveToFirst();
        int q2Id = c2.getInt(0);
        c2.close();

        db.execSQL("INSERT INTO answers VALUES (NULL, " + q2Id + ", 'Máy ảo Java', 1)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q2Id + ", 'Compiler', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q2Id + ", 'IDE', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q2Id + ", 'Framework', 0)");
    }
    public users loginUser(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, username, role FROM users WHERE username = ? AND password = ?",
                new String[]{username, password}
        );

        if(cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String user_name = cursor.getString(1);
            String role = cursor.getString(2);

            users user = new users();
            user.id = id;
            user.username = user_name;
            user.role = role;

            cursor.close();
            return user;
        }

        cursor.close();
        return null;
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

    public List<quizzes> getAllQuizByCateID(String id){
        List<quizzes> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM quizzes where category_id = ?", new String[] {id});

        if (cursor.moveToFirst()) {
            do {
                quizzes quiz = new quizzes();

                quiz.id = cursor.getInt(0);
                quiz.title = cursor.getString(1);
                quiz.description = cursor.getString(2);
                quiz.category_id = cursor.getInt(3);
                quiz.difficulty = cursor.getString(4);
                quiz.created_by = cursor.getInt(5);
                quiz.created_at = cursor.getString(6);

                list.add(quiz);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public List<questions> getAllQuestionByQuizzId(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        List<questions> list_question = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select * from questions where quiz_id = ?",
                new String[] {id}
        );

        if(cursor.moveToFirst()){
            do{
                questions ques = new questions();

                ques.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                ques.quiz_id = cursor.getInt(cursor.getColumnIndexOrThrow("quiz_id"));
                ques.question_text = cursor.getString(cursor.getColumnIndexOrThrow("question_text"));
                ques.explanation = cursor.getString(cursor.getColumnIndexOrThrow("explanation"));
                ques.difficulty = cursor.getString(cursor.getColumnIndexOrThrow("difficulty"));

                list_question.add(ques);
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        return list_question;
    }
    public List<answers> getAllAnswerByQuestionID(String id){
        SQLiteDatabase db = getReadableDatabase();
        List<answers> list_answer = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select * from answers where question_id = ?",
                new String[] {id}
        );
        if(cursor.moveToFirst()){
            do {
                answers ans = new answers();
                ans.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                ans.question_id = cursor.getInt(cursor.getColumnIndexOrThrow("question_id"));
                ans.answer_text = cursor.getString(cursor.getColumnIndexOrThrow("answer_text"));
                ans.is_correct = cursor.getInt(cursor.getColumnIndexOrThrow("is_correct"));
                list_answer.add(ans);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return list_answer;
    }
    public List<categories> getAllCategories(){
        List<categories> list_cate = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from categories",
                null
        );

        if(cursor.moveToFirst()){
            do {
                categories cate = new categories();

                cate.id = cursor.getInt(0);
                cate.name = cursor.getString(1);
                cate.description = cursor.getString(2);

                list_cate.add(cate);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return list_cate;
    }
    public List<answers> getTestAnswer(){
        SQLiteDatabase db = getReadableDatabase();
        List<answers> list_answer = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select * from answers",
                null
        );
        if(cursor.moveToFirst()){
            do {
                answers ans = new answers();
                ans.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                ans.question_id = cursor.getInt(cursor.getColumnIndexOrThrow("question_id"));
                ans.answer_text = cursor.getString(cursor.getColumnIndexOrThrow("answer_text"));
                ans.is_correct = cursor.getInt(cursor.getColumnIndexOrThrow("is_correct"));
                list_answer.add(ans);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return list_answer;
    }

    public boolean saveUserProcess(int user_id, int quiz_id, int score, int total_question){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", user_id);
        values.put("quiz_id", quiz_id);
        values.put("score", score);
        values.put("total_questions", total_question);

        // nếu có cột thời gian
        values.put("started_at", System.currentTimeMillis());
        values.put("finished_at", System.currentTimeMillis());

        long result = db.insert("quiz_attempts", null, values);

        return result != -1; // true nếu insert thành công
    }
    public List<quiz_attempts> getUserAttempts(int user_id) {
        List<quiz_attempts> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM quiz_attempts WHERE user_id = ?",
                new String[]{String.valueOf(user_id)});
        if (cursor.moveToFirst()) {
            do {
                quiz_attempts attempt = new quiz_attempts();
                attempt.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                attempt.user_id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                attempt.quiz_id = cursor.getInt(cursor.getColumnIndexOrThrow("quiz_id"));
                attempt.score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
                attempt.total_questions = cursor.getInt(cursor.getColumnIndexOrThrow("total_questions"));
                attempt.started_at = cursor.getString(cursor.getColumnIndexOrThrow("started_at"));
                attempt.finished_at = cursor.getString(cursor.getColumnIndexOrThrow("finished_at"));
                list.add(attempt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean removeAllUser() {
        SQLiteDatabase db = this.getWritableDatabase(); // phải là writable để xóa
        try {
            db.delete("users", null, null); // xóa tất cả dòng trong bảng users
            // Hoặc bạn có thể dùng execSQL: db.execSQL("DELETE FROM users");
            return true; // xóa thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false; // xóa thất bại
        } finally {
            db.close();
        }
    }

    public int getQuestionCountByQuizId(int quizId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM questions WHERE quiz_id = ?",
                new String[]{String.valueOf(quizId)});
        int count = 0;
        if(cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getStudentCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM users WHERE role = 'STUDENT'", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getCategoryCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM categories", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
    public long insertCategory(String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        return db.insert("categories", null, values);
    }

    public boolean deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Chú ý: Khi xóa Category, bạn nên xóa luôn các Quiz liên quan để tránh rác database
        db.delete("quizzes", "category_id = ?", new String[]{String.valueOf(id)});
        int result = db.delete("categories", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public long insertQuiz(int categoryId, String title, String description, String difficulty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("category_id", categoryId);
        values.put("title", title);
        values.put("description", description);
        values.put("difficulty", difficulty);
        return db.insert("quizzes", null, values);
    }

    public boolean deleteQuiz(int quizId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Xóa đáp án -> Xóa câu hỏi -> Xóa Quiz (đúng thứ tự khóa ngoại)
        db.execSQL("DELETE FROM answers WHERE question_id IN (SELECT id FROM questions WHERE quiz_id = ?)", new Object[]{quizId});
        db.delete("questions", "quiz_id = ?", new String[]{String.valueOf(quizId)});
        return db.delete("quizzes", "id = ?", new String[]{String.valueOf(quizId)}) > 0;
    }

    public boolean deleteQuestion(int questionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("answers", "question_id = ?", new String[]{String.valueOf(questionId)});
        return db.delete("questions", "id = ?", new String[]{String.valueOf(questionId)}) > 0;
    }

    public long insertQuestion(int quizId, String text, String explanation, String difficulty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quiz_id", quizId);
        values.put("question_text", text);
        values.put("explanation", explanation);
        values.put("difficulty", difficulty);
        return db.insert("questions", null, values);
    }

    public void insertAnswer(long questionId, String text, int isCorrect) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("question_id", questionId);
        values.put("answer_text", text);
        values.put("is_correct", isCorrect);
        db.insert("answers", null, values);
    }

    public List<quiz_attempts> getUserAttemptsWithTitle(int user_id) {
        List<quiz_attempts> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT qa.*, q.title FROM quiz_attempts qa " +
                        "LEFT JOIN quizzes q ON qa.quiz_id = q.id " +
                        "WHERE qa.user_id = ? ORDER BY qa.id DESC",
                new String[]{String.valueOf(user_id)}
        );
        if (cursor.moveToFirst()) {
            do {
                quiz_attempts attempt = new quiz_attempts();
                attempt.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                attempt.user_id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                attempt.quiz_id = cursor.getInt(cursor.getColumnIndexOrThrow("quiz_id"));
                attempt.score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
                attempt.total_questions = cursor.getInt(cursor.getColumnIndexOrThrow("total_questions"));
                attempt.started_at = cursor.getString(cursor.getColumnIndexOrThrow("started_at"));
                attempt.quizTitle = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                list.add(attempt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<RankingItem> getGlobalRanking() {
        List<RankingItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT u.username, SUM(qa.score) as total_score, COUNT(qa.id) as total_attempts " +
                        "FROM quiz_attempts qa LEFT JOIN users u ON qa.user_id = u.id " +
                        "GROUP BY qa.user_id ORDER BY total_score DESC LIMIT 20",
                null
        );
        if (cursor.moveToFirst()) {
            do {
                RankingItem item = new RankingItem();
                item.username = cursor.getString(0);
                item.totalScore = cursor.getInt(1);
                item.totalAttempts = cursor.getInt(2);
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public StatsData getUserStats(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        StatsData stats = new StatsData();

        Cursor c1 = db.rawQuery(
                "SELECT COUNT(*), SUM(score), SUM(total_questions) FROM quiz_attempts WHERE user_id = ?",
                new String[]{String.valueOf(user_id)}
        );
        if (c1.moveToFirst()) {
            stats.totalAttempts = c1.getInt(0);
            stats.totalScore = c1.getInt(1);
            stats.totalQuestions = c1.getInt(2);
        }
        c1.close();

        Cursor c2 = db.rawQuery(
                "SELECT q.title, qa.score, qa.total_questions FROM quiz_attempts qa " +
                        "LEFT JOIN quizzes q ON qa.quiz_id = q.id " +
                        "WHERE qa.user_id = ? ORDER BY qa.id DESC LIMIT 5",
                new String[]{String.valueOf(user_id)}
        );
        stats.recentAttempts = new ArrayList<>();
        if (c2.moveToFirst()) {
            do {
                quiz_attempts a = new quiz_attempts();
                a.quizTitle = c2.getString(0);
                a.score = c2.getInt(1);
                a.total_questions = c2.getInt(2);
                stats.recentAttempts.add(a);
            } while (c2.moveToNext());
        }
        c2.close();
        return stats;
    }

    public boolean updateUserName(int id, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", newName); // Tên cột phải khớp với DB của bạn

        int result = db.update("users", values, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}