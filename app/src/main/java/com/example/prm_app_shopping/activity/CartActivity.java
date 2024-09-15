package com.example.prm_app_shopping.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.prm_app_shopping.adapters.CartAdapter;
import com.example.prm_app_shopping.api.UsersApiService;
import com.example.prm_app_shopping.databinding.ActivityCardBinding;
import com.example.prm_app_shopping.model.Cart;
import com.example.prm_app_shopping.model.Users;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnDataChangeListener {
    ListView listview;
    TextView total;
    Button button;
    ArrayList<Cart> carts;
    Cart cart;
    CartAdapter CartAdapter;
    ActivityCardBinding binding;
    Users customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("CACHE", MODE_PRIVATE);
        String cachedUsers = sharedPreferences.getString("USERS", null);
        customer = new Gson().fromJson(cachedUsers, Users.class);
        UsersApiService.UsersApiService.getProduct(Integer.parseInt(customer.getId()));

        init();

    }


    private void init() {
        carts = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("CACHE", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("cartsList", null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Cart>>() {
            }.getType();
            carts = gson.fromJson(json, type);
        }

        CartAdapter = new CartAdapter(this, carts);
        CartAdapter.setOnDataChangeListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.setAdapter(CartAdapter);

        binding.checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiểm tra người dùng có tồn tại trong bộ nhớ được cấp không
                Intent intent;
                if (customer == null) {
                    intent = new Intent(CartActivity.this, Login.class);
                } else {
                    intent = new Intent(CartActivity.this, DeliveryDetails.class);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDataChanged(double data) {
        binding.totalPrice.setText("Thanh toan:" + data);
    }
}