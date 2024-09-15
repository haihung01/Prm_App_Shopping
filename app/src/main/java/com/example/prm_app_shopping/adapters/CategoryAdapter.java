package com.example.prm_app_shopping.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm_app_shopping.R;
import com.example.prm_app_shopping.activity.CategoryActivity;
import com.example.prm_app_shopping.activity.Login;
import com.example.prm_app_shopping.activity.ProductDetailActivity;
import com.example.prm_app_shopping.databinding.ItemCategoriesBinding;
import com.example.prm_app_shopping.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<Category> categories;
    public  CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder((LayoutInflater.from(context).inflate(R.layout.item_categories, parent, false )));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
         Category category = categories.get(position);
         holder.binding.label.setText(category.getName());
         Glide.with(context)
                .load(category.getIcon())
                .into(holder.binding.image);



         holder.binding.image.setBackgroundColor(Color.parseColor(category.getColor()));

        //--------------
        Glide.with(context)
                .load(category.getIcon())
                .into(holder.binding.image);

        // set on click listener for the whole item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category", category.getId());
                intent.putExtra("product_id", category.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ItemCategoriesBinding binding;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoriesBinding.bind(itemView);
        }
    }

}
