package com.example.prm_app_shopping.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm_app_shopping.R;
import com.example.prm_app_shopping.activity.History;
import com.example.prm_app_shopping.databinding.CartBinding;
import com.example.prm_app_shopping.databinding.ItemHistoryBinding;
import com.example.prm_app_shopping.model.Cart;
import com.example.prm_app_shopping.model.Orders;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    Context context;
    ArrayList<Orders> orders;

    public HistoryAdapter(Context context, ArrayList<Orders> orderList) {
        this.context = context;
        this.orders= orderList;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryAdapter.HistoryViewHolder((LayoutInflater.from(context).inflate(R.layout.item_history, parent, false )));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        Orders o = orders.get(position);
        holder.binding.textView9.setText(String.valueOf(o.getOrderDate()));
        List<Cart> carts = o.getOdersItem();

        SubAdapter subAdapter = new SubAdapter(carts);
        GridLayoutManager layoutManager = new GridLayoutManager(holder.itemView.getContext(), 1);
        holder.mSubRecyclerView.setLayoutManager(layoutManager);
        holder.mSubRecyclerView.setAdapter(subAdapter);

        double total = 0;
        for (Cart item : o.getOdersItem()
             ) {
            total = item.getTotal()+total;
        }
        holder.binding.totalPrice3.setText("Tổng tiền:"+total);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        private ItemHistoryBinding binding;
        private TextView totalPrice, dateOrder;
        public RecyclerView mSubRecyclerView;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemHistoryBinding.bind(itemView);
            mSubRecyclerView = itemView.findViewById(R.id.item_cart_history);
        }
    }
    public class SubAdapter extends RecyclerView.Adapter<SubAdapter.SubViewHolder> {
        private List<Cart> carts;

        public SubAdapter(List<Cart> subItemList) {
            this.carts = subItemList;
        }

        @NonNull
        @Override
        public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart, parent, false);
            return new SubViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull SubViewHolder holder, int position) {
            Cart cart = carts.get(position);
            Glide.with(context)
                    .load(cart.getProduct().getImage())
                    .into(holder.binding.image);
            holder.binding.title.setText(cart.getProduct().getName());
            holder.binding.year.setText("Year model:" + cart.getProduct().getModel_year());
            holder.binding.price.setText("$" + cart.getProduct().getPrice());
            holder.binding.quantity.setText(String.valueOf((int) cart.getProduct().getDiscount()));
        }

        @Override
        public int getItemCount() {
            return carts.size();
        }

        public class SubViewHolder extends RecyclerView.ViewHolder {
            private ImageView img,delete;
            private TextView title, year, quantity, price;
            CartBinding binding;
            public SubViewHolder(@NonNull View itemView) {
                super(itemView);

                binding = CartBinding.bind(itemView);
                img = itemView.findViewById(R.id.image);
                title = itemView.findViewById(R.id.title);
                year = itemView.findViewById(R.id.year);
                quantity = itemView.findViewById(R.id.quantity);
                price = itemView.findViewById(R.id.price);
                delete = itemView.findViewById(R.id.delete);
            }
        }
    }
}
