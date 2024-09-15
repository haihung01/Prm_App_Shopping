package com.example.prm_app_shopping.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm_app_shopping.R;
import com.example.prm_app_shopping.activity.CartActivity;
import com.example.prm_app_shopping.activity.ProductDetailActivity;
import com.example.prm_app_shopping.databinding.CartBinding;
import com.example.prm_app_shopping.model.Cart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    ArrayList<Cart> carts;
    CartActivity cartActivity = new CartActivity();


    public CartAdapter(Context context, ArrayList<Cart> carts) {
        this.context = context;
        this.carts = carts;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = carts.get(position);
        updateData(total());
        Glide.with(context)
                .load(cart.getProduct().getImage())
                .into(holder.binding.image);
        holder.binding.title.setText(cart.getProduct().getName());
        holder.binding.year.setText("Year model:" + cart.getProduct().getModel_year());
        holder.binding.price.setText("$" + cart.getProduct().getPrice());
        holder.binding.quantity.setText(String.valueOf((int) cart.getProduct().getDiscount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chuyển đến trang detail của sản phẩm
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("name", cart.getProduct().getName());
                intent.putExtra("image", cart.getProduct().getImage());
                intent.putExtra("price", cart.getProduct().getPrice());
                intent.putExtra("status", cart.getProduct().getStatus());
                Gson gson = new Gson();
                String json = gson.toJson(cart.getProduct());
                intent.putExtra("product", json);
                context.startActivity(intent);
            }
        });
        holder.binding.ButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = (int) cart.getProduct().getDiscount();
                //kiểm tra số lượng sản phẩm tối thiểu mỗi cart là 1, tính
                if (quantity > 1) {
                    holder.binding.quantity.setText(String.valueOf(quantity - 1));
                    cart.getProduct().setDiscount(quantity - 1);
                    double total=0;

                    SharedPreferences sharedPreferences = context.getSharedPreferences("CACHE", Context.MODE_PRIVATE);
                    String json = sharedPreferences.getString("cartsList", null);
                    Gson gson = new Gson();
                    if (json != null) {
                        Type type = new TypeToken<ArrayList<Cart>>() {
                        }.getType();
                        carts = gson.fromJson(json, type);
                        for (Cart item : carts
                        ) {
                            if (item.getId() == cart.getId()) {
                                item.getProduct().setDiscount(item.getProduct().getDiscount() - 1);
                                item.setTotal(item.getProduct().getPrice()*item.getProduct().getDiscount());
                            }
                            total = total+(item.getProduct().getPrice()*item.getProduct().getDiscount());
                        }
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    json = gson.toJson(carts); // Chuyển đổi đối tượng thành chuỗi JSON
                    editor.putString("cartsList", json);
                    editor.apply();

                    cart.setTotal(total);
                    updateData(total);
                }
            }
        });
        holder.binding.ButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = (int) cart.getProduct().getDiscount();
                holder.binding.quantity.setText(String.valueOf(quantity + 1));
                cart.getProduct().setDiscount(quantity + 1);
                double total=0;

                SharedPreferences sharedPreferences = context.getSharedPreferences("CACHE", Context.MODE_PRIVATE);
                String json = sharedPreferences.getString("cartsList", null);
                Gson gson = new Gson();
                if (json != null) {
                    Type type = new TypeToken<ArrayList<Cart>>() {
                    }.getType();
                    carts = gson.fromJson(json, type);
                    for (Cart item : carts
                    ) {
                        if (item.getId() == cart.getId()) {
                            item.getProduct().setDiscount(item.getProduct().getDiscount() + 1);
                            item.setTotal(item.getProduct().getPrice()*item.getProduct().getDiscount());
                        }
                        total = total+(item.getProduct().getPrice()*item.getProduct().getDiscount());
                    }
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                json = gson.toJson(carts); // Chuyển đổi đối tượng thành chuỗi JSON
                editor.putString("cartsList", json);
                editor.apply();

                cart.setTotal(total);
                updateData(total);
            }
        });
        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Xóa sản phẩm")
                        .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                carts.remove(position);
                                notifyDataSetChanged();

                                updateData(total());

                                SharedPreferences sharedPreferences = context.getSharedPreferences("CACHE", Context.MODE_PRIVATE);
                                String json = sharedPreferences.getString("cartsList", null);
                                Gson gson = new Gson();
                                if (json != null) {
                                    Type type = new TypeToken<ArrayList<Cart>>() {
                                    }.getType();
                                    carts = gson.fromJson(json, type);
                                    for (Cart item : carts
                                    ) {
                                        if (item.getId() == cart.getId()) {
                                            carts.remove(item);
                                        }
                                    }
                                }

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                json = gson.toJson(carts); // Chuyển đổi đối tượng thành chuỗi JSON
                                editor.putString("cartsList", json);
                                editor.apply();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Không làm gì cả
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return carts != null ? carts.size() : 0;
    }

    //tạo interface để activity gọi
    public interface OnDataChangeListener {
        void onDataChanged(double data);
    }
    private OnDataChangeListener mListener;
    public void setOnDataChangeListener(OnDataChangeListener listener) {
        mListener = listener;
    }
    public void updateData(double newData) {
        // cập nhật số liệu biến
        double mData = newData;

        // gọi phương thức callback
        if (mListener != null) {
            mListener.onDataChanged(mData);
        }
    }
    private double total(){
        double total=0;
        for (Cart item :
                carts) {
            total = total+ item.getTotal();
        }
        return total;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView img,delete;
        private TextView title, year, quantity, price;

        CartBinding binding;


        public CartViewHolder(@NonNull View itemView) {
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

    //kai báo các hàm cần thiết để tương tác với bộ nhớ cache
    public class MySharedPreferences {
        private SharedPreferences sharedPreferences;

        public MySharedPreferences(Context context) {
            sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        }

        public void saveData(String key, String value) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }

        public String getData(String key) {
            return sharedPreferences.getString(key, null);
        }
    }


}
