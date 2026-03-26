package com.example.quizapp.xulydulieu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizapp.QuizActivity;
import com.example.quizapp.R;
import com.example.quizapp.class_package.categories;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    List<categories> list;
    public CategoryAdapter(Context context, List<categories> list){
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView cate_name, cate_des;

        // bo sung o home
        public ViewHolder(View itemView){
            super(itemView);
            cate_name = itemView.findViewById(R.id.categories_name);
            cate_des = itemView.findViewById(R.id.categories_description);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_categories, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        categories cate = list.get(position);

        holder.cate_name.setText(cate.name);
        holder.cate_des.setText(cate.description);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuizActivity.class);
            intent.putExtra("category_id", cate.id);
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount(){
        return list.size();
    }

}
