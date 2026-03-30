package com.example.quizapp.xulydulieu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.AdminQuizActivity;
import com.example.quizapp.R;
import com.example.quizapp.class_package.categories;
import com.example.quizapp.database.DatabaseHelper;

import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.ViewHolder> {
    Context context;
    List<categories> list;
    DatabaseHelper db;

    public AdminCategoryAdapter(Context context, List<categories> list) {
        this.context = context;
        this.list = list;
        this.db = new DatabaseHelper(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cate_name, cate_des;

        public ViewHolder(View itemView) {
            super(itemView);
            cate_name = itemView.findViewById(R.id.categories_name);
            cate_des = itemView.findViewById(R.id.categories_description);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        categories cate = list.get(position);

        holder.cate_name.setText(cate.name);
        holder.cate_des.setText(cate.description);

        // Click bình thường: Chuyển đến trang quản lý Quiz của Category đó
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminQuizActivity.class);
            intent.putExtra("category_id", cate.id);
            intent.putExtra("category_name", cate.name);
            context.startActivity(intent);
        });

        // Nhấn giữ: Để xóa Category
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa danh mục")
                    .setMessage("Bạn có chắc chắn muốn xóa '" + cate.name + "' và tất cả dữ liệu bên trong?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (db.deleteCategory(cate.id)) {
                            list.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}