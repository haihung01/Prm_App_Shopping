package com.example.prm_app_shopping.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prm_app_shopping.R;
import com.example.prm_app_shopping.api.UsersApiService;
import com.example.prm_app_shopping.databinding.ActivityProductDetailBinding;
import com.example.prm_app_shopping.model.Cart;
import com.example.prm_app_shopping.model.Product;
import com.example.prm_app_shopping.model.Users;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding binding;
    Product product;
    Cart cart;
    ArrayList<Cart> carts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String json = getIntent().getStringExtra("product");
        Gson gson = new Gson();
        product = gson.fromJson(json, Product.class);
        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        String status = getIntent().getStringExtra("status");
        double price = getIntent().getDoubleExtra("price", 0);

        Glide.with(this)
                .load(image)
                .into(binding.productImage);


        getSupportActionBar().setTitle(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // display the product details
        TextView productName = findViewById(R.id.textViewProductDetail);
        TextView productPrice = findViewById(R.id.textViewProductDetail2);
        TextView productNote = findViewById(R.id.textViewProductDetail3);

        productName.setText(name);
        productPrice.setText(String.valueOf(price));
        productNote.setText(status);
    }


    public void clickAdd(View view) {
        //khai báo tạo cartList, cart mới với sản phẩm
        carts = new ArrayList<>();
        product.setDiscount(1);
        cart = new Cart(product.getId(), product.getPrice(), product);
        //khai báo bộ nhớ cache
        SharedPreferences sharedPreferences = getSharedPreferences("CACHE", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("cartsList", null);// truy cập vùng nhớ cartList
        Gson gson = new Gson();
        //kiểm tra xem có cartList không, nếu có tiến hành update số lượng sản phẩm trong cartList đã được lấy ra, không thì thêm mới sản phẩm
        if (json != null) {
            Type type = new TypeToken<ArrayList<Cart>>() {
            }.getType();
            carts = gson.fromJson(json, type);

            boolean find = false;
            for (Cart item : carts
            ) {
                if (item.getId() == cart.getId()) {
                    item.getProduct().setDiscount(item.getProduct().getDiscount() + 1);
                    item.setTotal(item.getProduct().getPrice()*item.getProduct().getDiscount());
                    find = true;
                }
            }
            if (!find) {
                carts.add(cart);
            }
        } else {
            carts.add(cart);
        }


        //cập nhật lại cartList trong bộ nhớ
        String cachedUsers = sharedPreferences.getString("USERS", null);
        Users customer = new Gson().fromJson(cachedUsers, Users.class);
        UsersApiService.UsersApiService.createProduct(Integer.parseInt(customer.getId()),product).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String json = gson.toJson(carts); // Chuyển đổi đối tượng thành chuỗi JSON
                editor.putString("cartsList", json);
                editor.apply();

                //chuyển trang
                Toast.makeText(ProductDetailActivity.this, "Order has been placed. ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProductDetailActivity.this, HomeActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Cannot connect server.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    // hàm gọi lại từ adapter để cập nhật tổng giá
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}