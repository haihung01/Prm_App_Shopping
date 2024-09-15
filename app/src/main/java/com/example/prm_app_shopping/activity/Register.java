package com.example.prm_app_shopping.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_app_shopping.R;
import com.example.prm_app_shopping.api.UsersApiService;
import com.example.prm_app_shopping.model.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register extends AppCompatActivity {
    private EditText firstname, lastname, phone, email, password, confPassword,Date;
    AppCompatButton submit, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstname = findViewById(R.id.username);
        lastname = findViewById(R.id.fullname);
        Date = findViewById(R.id.date);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confPassword = findViewById(R.id.confirmPassword);
        submit = findViewById(R.id.submit);
        cancel = findViewById(R.id.cancel);

        Users customer = new Users("", String.valueOf(password.getText()), String.valueOf(phone.getText()), String.valueOf(email.getText()), "", "", "", "", String.valueOf(firstname.getText()), String.valueOf(lastname.getText()));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!String.valueOf(password.getText()).equals(String.valueOf(confPassword.getText()))) {
                    showWrongPasswordAlert("Password phải giống với confirmPassword");
                } else {
                    createUsers(customer);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createUsers(Users user) {
        ArrayList<Users> listUsers = new ArrayList<>();

        UsersApiService.UsersApiService.allUsers().enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                List<Users> allUsers = response.body();
                listUsers.addAll(allUsers);
                for (Users use : listUsers
                ) {
                    if (user.getEmail().equals(use.getEmail())) {
                        showWrongPasswordAlert("Email đã được sử dụng");
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                showWrongPasswordAlert("không thể kiểm tra được thông tin người dùng, vui lòng kiểm tra các kết nối");
            }
        });
        user.setId(String.valueOf(listUsers.size() + 1));

        Call<Users> call = UsersApiService.UsersApiService.createUsers(user);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                // Xử lý kết quả thành công
                Users user = response.body();
                // ...
//                showWrongPasswordAlert("Đăng ký thành công.");
//                Log.d("log-note","register success");
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                // Xử lý lỗi
                showWrongPasswordAlert("Đã có lỗi xảy ra");
//                Log.d("log-note","ERRoR");
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
    private void checkDataEntered() {
        String inputUserName = firstname.getText().toString();
        String inputPassword = password.getText().toString();
        String confirmPw = confPassword.getText().toString();
        String inputPhone = phone.getText().toString();
        String inputFullName = lastname.getText().toString();
        String birthdate = Date.getText().toString();

        if (inputUserName.isEmpty() || !inputUserName.matches("^[a-zA-Z0-9]*$")) {
            showError(firstname, "Username not only contain digits and letter");
        }
        else if (birthdate.isEmpty()) {
            showError(Date, "Birthdate is required");
        }
        else if (inputFullName.isEmpty()) {
            showError(lastname, "FullName is required");
        }else if (inputPassword.isEmpty() || inputPassword.length() < 8) {
            showError(password, "password must at least 7 characters");
        } else if (!confirmPw.equals(inputPassword)) {
            showError(confPassword, "password not match");
        } else if (inputPhone.length() != 10) {
            showError(phone, "Invalid Phone, must be 10 number");
        } else if (!isEmail(email)) {
            showError(email, "Invalid email");
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date date = new Date();
            try {
                date = sdf.parse(birthdate);
            } catch (ParseException e) {
                e.printStackTrace();
                showError(Date,"invalid");
            }
            Calendar currentDate = Calendar.getInstance();
            Calendar birthDate = Calendar.getInstance();
            birthDate.setTime(date);
            int age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
            if (currentDate.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            if (age < 18) {
                showError(Date, "Age must be over 18");
            } else {
                Toast t = Toast.makeText(this, "Register successfully !!!", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
    private void datePicker() {
        Calendar dob = Calendar.getInstance();
        int day = dob.get(Calendar.DATE);
        int month = dob.get(Calendar.MONTH);
        int year = dob.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year) {
                dob.set(day, month, year);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                simpleDateFormat.setLenient(false);
                Date.setText(simpleDateFormat.format(dob.getTime()));
            }
        }, day, month, year);
        datePickerDialog.show();
    }
}