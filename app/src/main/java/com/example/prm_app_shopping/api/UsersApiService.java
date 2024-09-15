package com.example.prm_app_shopping.api;

import com.example.prm_app_shopping.model.Product;
import com.example.prm_app_shopping.model.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersApiService {
    //    https://64085ddf8ee73db92e3eafad.mockapi.io/api/products

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    //ham khoi tao
    UsersApiService UsersApiService = new Retrofit.Builder()
            .baseUrl("https://64085ddf8ee73db92e3eafad.mockapi.io/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UsersApiService.class);

    //ham call api
    @GET("customers")
    Call<List<Users>> allUsers();

    @POST("customers")
    Call<Users> createUsers(@Body Users user);

    @POST("customers/{id}/products")
    Call<Users> createProduct(@Path("id") int categoryId,@Body Product product);

    @GET("customers/{id}/products")
    Call<Product> getProduct(@Path("id") int Id);
}
