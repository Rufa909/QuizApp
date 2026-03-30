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
    public void onCreate(SQLiteDatabase db) {

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
        // Đã sửa lỗi merge: Giữ lại bản có cột role ADMIN
        db.execSQL("INSERT INTO users (id, username, password, role) VALUES (1, 'admin', '123', 'ADMIN')");

        db.execSQL("INSERT INTO categories (name, description) VALUES ('Java', 'Lập trình Java')");
        db.execSQL("INSERT INTO categories (name, description) VALUES ('Android', 'Lập trình Android')");

        db.execSQL("INSERT INTO quizzes (title, description, category_id, difficulty, created_by) " +
                "VALUES ('Java cơ bản', 'Quiz Java', 1, 'Easy', 1)");

        db.execSQL("INSERT INTO quizzes (title, description, category_id, difficulty, created_by) " +
                "VALUES ('Android UI', 'Quiz Android', 2, 'Medium', 1)");

        // Java Questions
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

        // Android Questions
        db.execSQL("INSERT INTO questions (quiz_id, question_text, explanation, difficulty) " +
                "VALUES (2, 'Intend resolution là gì?', 'Quá trình tìm kiếm một hoạt động phù hợp với một Intent', 'Easy')");

        Cursor c3 = db.rawQuery("SELECT last_insert_rowid()", null);
        c3.moveToFirst();
        int q3Id = c3.getInt(0);
        c3.close();

        db.execSQL("INSERT INTO answers VALUES (NULL, " + q3Id + ", 'Quá trình thực hiện một tác vụ hệ thống', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q3Id + ", 'Quá trình tìm kiếm một hoạt động phù hợp với một Intent', 1)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q3Id + ", 'Quá trình khởi chạy một hoạt động', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q3Id + ", 'Quá trình chia sẻ dữ liệu', 0)");

        db.execSQL("INSERT INTO questions (quiz_id, question_text, explanation, difficulty) " +
                "VALUES (2, 'Trong Android, để tạo một thanh tiêu đề tùy chỉnh cho một hoạt động (activity), thường sử dụng lớp nào?', 'Toolbar', 'Easy')");

        Cursor c4 = db.rawQuery("SELECT last_insert_rowid()", null);
        c4.moveToFirst();
        int q4Id = c4.getInt(0);
        c4.close();

        db.execSQL("INSERT INTO answers VALUES (NULL, " + q4Id + ", 'ActionBar', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q4Id + ", 'MenuBar', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q4Id + ", 'Toolbar', 1)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q4Id + ", 'TitleBar', 0)");

        db.execSQL("INSERT INTO questions (quiz_id, question_text, explanation, difficulty) " +
                "VALUES (2, 'Trong Android, để lấy vị trí GPS của thiết bị, cần yêu cầu quyền (permission) nào trong tệp AndroidManifest.xml?', 'ACCESS_FINE_LOCATION', 'Easy')");

        Cursor c5 = db.rawQuery("SELECT last_insert_rowid()", null);
        c5.moveToFirst();
        int q5Id = c5.getInt(0);
        c5.close();

        db.execSQL("INSERT INTO answers VALUES (NULL, " + q5Id + ", 'ACCESS_WIFI_STATE', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q5Id + ", 'ACCESS_FINE_LOCATION', 1)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q5Id + ", 'WRITE_EXTERNAL_STORAGE', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q5Id + ", 'INTERNET', 0)");

        db.execSQL("INSERT INTO questions (quiz_id, question_text, explanation, difficulty) " +
                "VALUES (2, 'Để lưu trữ một chuỗi \"username\" trong SharedPreferences, đoạn mã nào sau đây là đúng?', 'Lưu trữ \"username\" với giá trị \"JohnDoe\" trong SharedPreferences', 'Medium')");

        Cursor c6 = db.rawQuery("SELECT last_insert_rowid()", null);
        c6.moveToFirst();
        int q6Id = c6.getInt(0);
        c6.close();

        db.execSQL("INSERT INTO answers VALUES (NULL, " + q6Id + ", 'Xóa \"username\" khỏi SharedPreferences', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q6Id + ", 'Tạo mới SharedPreferences nhưng không lưu gì', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q6Id + ", 'Đọc giá trị \"username\" từ SharedPreferences', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q6Id + ", 'Lưu trữ \"username\" với giá trị \"JohnDoe\" trong SharedPreferences', 1)");

        db.execSQL("INSERT INTO questions (quiz_id, question_text, explanation, difficulty) " +
                "VALUES (2, 'Làm thế nào để cập nhật giao diện người dùng từ luồng phụ khi doHeavyWork() hoàn thành?', 'Tất cả đều đúng', 'Medium')");

        Cursor c7 = db.rawQuery("SELECT last_insert_rowid()", null);
        c7.moveToFirst();
        int q7Id = c7.getInt(0);
        c7.close();

        db.execSQL("INSERT INTO answers VALUES (NULL, " + q7Id + ", 'Thêm runOnUiThread() vào phần cập nhật giao diện', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q7Id + ", 'Tất cả đều đúng', 1)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q7Id + ", 'Dùng AsyncTask thay vì Thread', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q7Id + ", 'Thêm new Handler(Looper.getMainLooper())', 0)");

        db.execSQL("INSERT INTO questions (quiz_id, question_text, explanation, difficulty) " +
                "VALUES (2, 'Về phía activity nhận, nếu cần trả về kết quả thành công cho activity trước, thì dùng phương thức gì?', 'setResult(RESULT_OK, x);', 'Easy')");

        Cursor c8 = db.rawQuery("SELECT last_insert_rowid()", null);
        c8.moveToFirst();
        int q8Id = c8.getInt(0);
        c8.close();

        db.execSQL("INSERT INTO answers VALUES (NULL, " + q8Id + ", 'setRespond(RESULT_PASS, x);', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q8Id + ", 'setRespond(RESULT_OK, x);', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q8Id + ", 'setResult(RESULT_PASS, x);', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q8Id + ", 'setResult(RESULT_OK, x);', 1)");

        db.execSQL("INSERT INTO questions (quiz_id, question_text, explanation, difficulty) " +
                "VALUES (2, 'Trong lập trình Android, để tạo một cơ sở dữ liệu SQLite mới, nên thực hiện bước nào sau đây?', 'Sử dụng lớp SQLiteDatabase để tạo cơ sở dữ liệu.', 'Easy')");

        Cursor c9 = db.rawQuery("SELECT last_insert_rowid()", null);
        c9.moveToFirst();
        int q9Id = c9.getInt(0);
        c9.close();

        db.execSQL("INSERT INTO answers VALUES (NULL, " + q9Id + ", 'Sử dụng lớp SQLiteDatabase để tạo cơ sở dữ liệu.', 1)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q9Id + ", 'Sử dụng lệnh \"CREATE TABLE\" để tạo bảng', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q9Id + ", 'Cài đặt thư viện \"android.database.sqlite\"', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q9Id + ", 'Sử dụng Context.openOrCreateDatabase', 0)");

        db.execSQL("INSERT INTO questions (quiz_id, question_text, explanation, difficulty) " +
                "VALUES (2, 'Trong Android, để thực hiện truy vấn SQLite SELECT, cần sử dụng phương thức nào của lớp SQLiteDatabase?', 'query()', 'Easy')");

        Cursor c10 = db.rawQuery("SELECT last_insert_rowid()", null);
        c10.moveToFirst();
        int q10Id = c10.getInt(0);
        c10.close();

        db.execSQL("INSERT INTO answers VALUES (NULL, " + q10Id + ", 'select()', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q10Id + ", 'query()', 1)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q10Id + ", 'getData()', 0)");
        db.execSQL("INSERT INTO answers VALUES (NULL, " + q10Id + ", 'executeQuery()', 0)");
    }

    // --- Các phương thức bên dưới giữ nguyên không thay đổi ---

    public users loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, username, role FROM users WHERE username = ? AND password = ?",
                new String[]{username, password}
        );
        if (cursor.moveToFirst()) {
            users user = new users();
            user.id = cursor.getInt(0);
            user.username = cursor.getString(1);
            user.role = cursor.getString(2);
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id from users where username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        cursor.close();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = db.insert("users", null, values);
        return result != -1;
    }

    public List<quizzes> getAllQuizByCateID(String id) {
        List<quizzes> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM quizzes where category_id = ?", new String[]{id});
        if (cursor.moveToFirst()) {
            do {
                quizzes quiz = new quizzes();
                quiz.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                quiz.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                quiz.description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                quiz.category_id = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
                quiz.difficulty = cursor.getString(cursor.getColumnIndexOrThrow("difficulty"));
                quiz.created_by = cursor.getInt(cursor.getColumnIndexOrThrow("created_by"));
                quiz.created_at = cursor.getString(cursor.getColumnIndexOrThrow("created_at"));
                list.add(quiz);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<questions> getAllQuestionByQuizzId(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<questions> list_question = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from questions where quiz_id = ?", new String[]{id});
        if (cursor.moveToFirst()) {
            do {
                questions ques = new questions();
                ques.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                ques.quiz_id = cursor.getInt(cursor.getColumnIndexOrThrow("quiz_id"));
                ques.question_text = cursor.getString(cursor.getColumnIndexOrThrow("question_text"));
                ques.explanation = cursor.getString(cursor.getColumnIndexOrThrow("explanation"));
                ques.difficulty = cursor.getString(cursor.getColumnIndexOrThrow("difficulty"));
                list_question.add(ques);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list_question;
    }

    public List<answers> getAllAnswerByQuestionID(String id) {
        SQLiteDatabase db = getReadableDatabase();
        List<answers> list_answer = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from answers where question_id = ?", new String[]{id});
        if (cursor.moveToFirst()) {
            do {
                answers ans = new answers();
                ans.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                ans.question_id = cursor.getInt(cursor.getColumnIndexOrThrow("question_id"));
                ans.answer_text = cursor.getString(cursor.getColumnIndexOrThrow("answer_text"));
                ans.is_correct = cursor.getInt(cursor.getColumnIndexOrThrow("is_correct"));
                list_answer.add(ans);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list_answer;
    }

    public List<categories> getAllCategories() {
        List<categories> list_cate = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from categories", null);
        if (cursor.moveToFirst()) {
            do {
                categories cate = new categories();
                cate.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                cate.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                cate.description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                list_cate.add(cate);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list_cate;
    }

    public List<answers> getTestAnswer() {
        SQLiteDatabase db = getReadableDatabase();
        List<answers> list_answer = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from answers", null);
        if (cursor.moveToFirst()) {
            do {
                answers ans = new answers();
                ans.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                ans.question_id = cursor.getInt(cursor.getColumnIndexOrThrow("question_id"));
                ans.answer_text = cursor.getString(cursor.getColumnIndexOrThrow("answer_text"));
                ans.is_correct = cursor.getInt(cursor.getColumnIndexOrThrow("is_correct"));
                list_answer.add(ans);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list_answer;
    }

    public boolean saveUserProcess(int user_id, int quiz_id, int score, int total_question) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", user_id);
        values.put("quiz_id", quiz_id);
        values.put("score", score);
        values.put("total_questions", total_question);
        values.put("started_at", String.valueOf(System.currentTimeMillis()));
        values.put("finished_at", String.valueOf(System.currentTimeMillis()));
        long result = db.insert("quiz_attempts", null, values);
        return result != -1;
    }

    public List<quiz_attempts> getUserAttempts(int user_id) {
        List<quiz_attempts> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM quiz_attempts WHERE user_id = ?", new String[]{String.valueOf(user_id)});
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
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete("users", null, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getQuestionCountByQuizId(int quizId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM questions WHERE quiz_id = ?", new String[]{String.valueOf(quizId)});
        int count = 0;
        if(cursor.moveToFirst()){ count = cursor.getInt(0); }
        cursor.close();
        return count;
    }

    public int getStudentCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM users WHERE role = 'STUDENT'", null);
        int count = 0;
        if (cursor.moveToFirst()) { count = cursor.getInt(0); }
        cursor.close();
        return count;
    }

    public int getCategoryCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM categories", null);
        int count = 0;
        if (cursor.moveToFirst()) { count = cursor.getInt(0); }
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
        values.put("username", newName);
        int result = db.update("users", values, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}