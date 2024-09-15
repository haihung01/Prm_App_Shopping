package com.example.prm_app_shopping.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.prm_app_shopping.adapters.HistoryAdapter;
import com.example.prm_app_shopping.api.OrderAPIService;
import com.example.prm_app_shopping.databinding.HistoryBinding;
import com.example.prm_app_shopping.model.Orders;
import com.example.prm_app_shopping.model.Users;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class History extends AppCompatActivity {
    ArrayList<Orders> orderList;
    HistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("CACHE", MODE_PRIVATE);
        String cachedUsers = sharedPreferences.getString("USERS", null);
        Users customer = new Gson().fromJson(cachedUsers, Users.class);

        OrderAPIService.OrderApiService.getHistory(Integer.parseInt(customer.getId())).enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                orderList = new ArrayList<>();
                List<Orders> orders = response.body();
                orderList.addAll(orders);
                HistoryAdapter historyAdapter = new HistoryAdapter(History.this, orderList);
                GridLayoutManager layoutManager = new GridLayoutManager(History.this, 1);
                binding.historyItem.setLayoutManager(layoutManager);
                binding.historyItem.setAdapter(historyAdapter);
            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {

            }
        });
        orderList = new ArrayList<>();
        HistoryAdapter historyAdapter = new HistoryAdapter(this, orderList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.historyItem.setLayoutManager(layoutManager);
        binding.historyItem.setAdapter(historyAdapter);

        binding.imageView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(History.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}