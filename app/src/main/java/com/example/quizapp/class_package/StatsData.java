package com.example.quizapp.class_package;

import java.util.List;

public class StatsData {
    public int totalAttempts;
    public int totalScore;
    public int totalQuestions;
    public List<quiz_attempts> recentAttempts;

    public int getAccuracyPercent() {
        if (totalQuestions == 0) return 0;
        return (int) ((totalScore * 100.0) / totalQuestions);
    }
}