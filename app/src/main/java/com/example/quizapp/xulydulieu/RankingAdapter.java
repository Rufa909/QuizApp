package com.example.quizapp.xulydulieu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizapp.R;
import com.example.quizapp.class_package.RankingItem;
import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.VH> {

    private final List<RankingItem> list;

    public RankingAdapter(List<RankingItem> list) { this.list = list; }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        RankingItem item = list.get(pos);
        String medal = pos == 0 ? "🥇" : pos == 1 ? "🥈" : pos == 2 ? "🥉" : String.valueOf(pos + 1);
        h.tvRank.setText(medal);
        h.tvUsername.setText(item.username);
        h.tvTotalScore.setText(item.totalScore + " điểm");
        h.tvAttempts.setText(item.totalAttempts + " lần làm");
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvRank, tvUsername, tvTotalScore, tvAttempts;
        VH(View v) {
            super(v);
            tvRank = v.findViewById(R.id.tvRank);
            tvUsername = v.findViewById(R.id.tvUsername);
            tvTotalScore = v.findViewById(R.id.tvTotalScore);
            tvAttempts = v.findViewById(R.id.tvAttempts);
        }
    }
}