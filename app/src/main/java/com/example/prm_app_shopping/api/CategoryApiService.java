package com.example.prm_app_shopping.api;

import com.example.prm_app_shopping.model.Category;
import com.example.prm_app_shopping.model.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    //ham khoi tao
    CategoryApiService CategoryApiService = new Retrofit.Builder()
            .baseUrl("https://64085ddf8ee73db92e3eafad.mockapi.io/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CategoryApiService.class);

    @GET("/categories")
    Call<List<Category>> getCategory();

    @GET("/categories/{id}/products")
    Call<List<Product>> getProductsByCategoryId(@Path("id") int categoryId);
}
