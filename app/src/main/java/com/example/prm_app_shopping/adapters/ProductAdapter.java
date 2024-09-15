package com.example.prm_app_shopping.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm_app_shopping.R;
import com.example.prm_app_shopping.activity.ProductDetailActivity;
import com.example.prm_app_shopping.databinding.ItemProductBinding;
import com.example.prm_app_shopping.model.Product;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {

    private List<Product> mListProdcut;
    private List<Product> mListProdcutOld;


    public ProductAdapter(List<Product> mListProdcut) {
        this.mListProdcut = mListProdcut;
        this.mListProdcutOld = mListProdcut;
    }


    Context context;
    ArrayList<Product> products;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
         Product product = products.get(position);
         Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.image);
        holder.binding.label.setText(product.getName());
        holder.binding.price.setText( "Price: " + product.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("name", product.getName());
                intent.putExtra("image", product.getImage());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("status", product.getStatus());
                Gson gson = new Gson();
                String json = gson.toJson(product);
                intent.putExtra("product", json);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (mListProdcut != null) {
            return mListProdcut.size();

        }
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView label, price;

        ItemProductBinding binding;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);

            img = itemView.findViewById(R.id.image);
            label = itemView.findViewById(R.id.label);
            price = itemView.findViewById(R.id.price);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()){
                    mListProdcut = mListProdcutOld;
                }else {
                    List<Product> list = new ArrayList<>();
                    for (Product products : mListProdcutOld) {
                        if (products.getName().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(products);
                        }
                    }

                    mListProdcutOld = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListProdcut;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mListProdcut = (List<Product>) results.values;
                notifyDataSetChanged();

            }
        };
    }
}
