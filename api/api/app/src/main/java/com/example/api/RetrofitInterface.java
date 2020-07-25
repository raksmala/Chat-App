package com.example.api;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResults> executeLogin(@Body HashMap<String, String> map);

    @POST("/register")
    Call<Void> executeRegister(@Body HashMap<String, String> map);

    @GET("/listUser")
    Call<List<Akun>> getUserData();

    @POST("/listChat")
    Call<List<MessageResults>> getMessage(@Body HashMap<String, String> map);
}
