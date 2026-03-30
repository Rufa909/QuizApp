package com.example.quizapp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

public class BottomNavHelper {

    public static void setup(Activity activity, View navView, String activeTab) {
        LinearLayout llHistory = navView.findViewById(R.id.llHistory);
        LinearLayout llRanking = navView.findViewById(R.id.llRanking);
        LinearLayout llStats   = navView.findViewById(R.id.llStats);

        llHistory.setOnClickListener(v -> {
            if (!activeTab.equals("history")) {
                Intent intent = new Intent(activity, HistoryActivity.class);
                // Flag này giúp quay lại Activity cũ nếu nó đã mở, thay vì tạo mới
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
                // KHÔNG gọi activity.finish() ở đây
            }
        });

        llRanking.setOnClickListener(v -> {
            if (!activeTab.equals("ranking")) {
                Intent intent = new Intent(activity, RankingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
            }
        });

        llStats.setOnClickListener(v -> {
            if (!activeTab.equals("stats")) {
                Intent intent = new Intent(activity, StatsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
            }
        });
    }
}