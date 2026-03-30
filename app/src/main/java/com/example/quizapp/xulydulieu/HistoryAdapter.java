package com.example.quizapp.xulydulieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizapp.R;
import com.example.quizapp.class_package.quiz_attempts;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VH> {

    private final List<quiz_attempts> list;

    public HistoryAdapter(Context ctx, List<quiz_attempts> list) {
        this.list = list;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        quiz_attempts a = list.get(pos);
        h.tvTitle.setText(a.quizTitle != null ? a.quizTitle : "Quiz #" + a.quiz_id);
        h.tvScore.setText(a.score + "/" + a.total_questions);
        h.tvDate.setText(a.started_at != null ? a.started_at : "");
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvScore, tvDate;
        VH(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvQuizTitle);
            tvScore = v.findViewById(R.id.tvScore);
            tvDate = v.findViewById(R.id.tvDate);
        }
    }
}