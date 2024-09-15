package com.example.prm_app_shopping.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prm_app_shopping.adapters.CartAdapter;
import com.example.prm_app_shopping.api.OrderAPIService;
import com.example.prm_app_shopping.databinding.ActivityDeliveryDetailsBinding;
import com.example.prm_app_shopping.model.Cart;
import com.example.prm_app_shopping.model.Orders;
import com.example.prm_app_shopping.model.Users;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryDetails extends AppCompatActivity {

    EditText contact,zipCode,address;
    ArrayList<Cart> carts;
    CartAdapter CartAdapter;
    ActivityDeliveryDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("CACHE", MODE_PRIVATE);
        String cachedUsers = sharedPreferences.getString("USERS", null);
        Users customer = new Gson().fromJson(cachedUsers, Users.class);

        carts = new ArrayList<>();
        String json = sharedPreferences.getString("cartsList", null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Cart>>() {
            }.getType();
            carts = gson.fromJson(json, type);
        }

        binding.email.getEditText().setText(customer.getPhone());
        binding.zipCode.getEditText().setText(customer.getZip_code());
        binding.addrees.getEditText().setText(customer.getState()+"/"+customer.getCity()+"/"+customer.getStreet());
        CartAdapter = new CartAdapter(this, carts);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.orderItems.setLayoutManager(layoutManager);
        binding.orderItems.setAdapter(CartAdapter);

        binding.finalcheckoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Orders oder = new Orders(generateRandomString(8),String.valueOf(customer.getId()),carts,0, new Date(),null,null);
                OrderAPIService.OrderApiService.addAOrder(oder,customer.getId()).enqueue(new Callback<Orders>() {
                    @Override
                    public void onResponse(Call<Orders> call, Response<Orders> response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("cartsList");
                        editor.apply();
                        Toast.makeText(DeliveryDetails.this, "Order Success.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DeliveryDetails.this, HomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Orders> call, Throwable t) {
                        showWrongPasswordAlert("Order Not Success.");
                    }
                });
            }
        });
    }
        private static String generateRandomString(int length) {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            StringBuilder sb = new StringBuilder(length);
            Random random = new Random();

            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(characters.length());
                char randomChar = characters.charAt(randomIndex);
                sb.append(randomChar);
            }

            return sb.toString();
        }
    private void showWrongPasswordAlert(String mess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage(mess);
        builder.setPositiveButton("Close", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}