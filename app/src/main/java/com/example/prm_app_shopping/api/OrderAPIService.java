package com.example.prm_app_shopping.api;

import com.example.prm_app_shopping.model.Orders;
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

public interface OrderAPIService {
    //    https://64085ddf8ee73db92e3eafad.mockapi.io/api/products

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    //ham khoi tao
    OrderAPIService OrderApiService = new Retrofit.Builder()
            .baseUrl("https://64085ddf8ee73db92e3eafad.mockapi.io/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OrderAPIService.class);

    @GET("customers/{id}/orders")
    Call<List<Orders>> getHistory(@Path("id") int UserID);

    @POST("/customers/{id}/orders")
    Call<Orders> addAOrder(@Body Orders order,@Path("id") String UID);
}
