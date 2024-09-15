package com.example.prm_app_shopping.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.prm_app_shopping.R;
import com.example.prm_app_shopping.api.UsersApiService;
import com.example.prm_app_shopping.model.Users;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {
    AppCompatButton login, register;
    EditText email, password;
    Users customer;
    ArrayList<Users> listUsers;
    String cachedUsers;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        listUsers = new ArrayList<>();
        UsersApiService.UsersApiService.allUsers().enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                List<Users> allUsers = response.body();
                listUsers.addAll(allUsers);

                SharedPreferences sharedPreferences = getSharedPreferences("CACHE", MODE_PRIVATE);
                cachedUsers = sharedPreferences.getString("USERS", null);
                if (cachedUsers != null) {
                    customer = new Gson().fromJson(cachedUsers, Users.class);
                    for (Users u : listUsers
                    ) {
                        if (customer.getEmail().equals(u.getEmail())) {
                            if (customer.getPassword().equals(u.getPassword())) {
                                Intent intent = new Intent(Login.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                showWrongPasswordAlert("Đa xảy ra sự cố, vui lòng đăng nhập lại.");
                                // Xóa cachedUsers khỏi bộ nhớ
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("USERS");
                                editor.apply();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                showWrongPasswordAlert("Đã xảy ra sự cố.");
            }
        });


        email = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        login = (AppCompatButton) findViewById(R.id.ButtonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean usersfound = false;
                for (Users user : listUsers) {
                    if (user.getEmail().equals(String.valueOf(email.getText())) || user.getPhone().equals(String.valueOf(email.getText()))) {
                        if (!user.getPassword().equals(String.valueOf(password.getText()))) {
                            showWrongPasswordAlert("mật khẩu không đúng");
                        } else {
                            usersfound = true;
                            customer = user;
                            Intent intent = new Intent(Login.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                }
                if (!usersfound) {
                    if (listUsers.size() == 0) {
                        showWrongPasswordAlert("không thể kết nối với server, vui lòng thử lại");
                    } else {
                        showWrongPasswordAlert("không tìm thấy thông tin người dùng");
                    }
                }
                //lưu thông tin đăng nhập cho lần tiếp
                if (usersfound && cachedUsers == null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("CACHE", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String customerJson = new Gson().toJson(customer);
                    editor.putString("USERS", customerJson);
                    editor.apply();
                }
            }
        });

        register = (AppCompatButton) findViewById(R.id.buttomRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });


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
