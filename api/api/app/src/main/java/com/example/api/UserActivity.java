package com.example.api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager;
    private List<Akun> userList = null;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Log.i("autolog", "onCreate");

        Bundle myBundle4 = getIntent().getExtras();
        email = myBundle4.getString("email");
        getUserList();
    }

    private void getUserList() {
        Log.i("autolog", "getUserList");
        try {
            String url = "http://192.168.1.9:3000";
            Log.i("autolog", "http://192.168.1.9:3000");

            Retrofit retrofit = null;
            Log.i("autolog", "retrofit");

            if(retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Log.i("autolog", "build();");
            }

            RetrofitInterface service = retrofit.create(RetrofitInterface.class);
            Log.i("autolog", " RetrofitInterface service = retrofit.create(RetrofitInterface.class);");

            Call<List<Akun>> call = service.getUserData();
            Log.i("autolog", "Call<List<Akun>> call = service.getUserData();");

            call.enqueue(new Callback<List<Akun>>() {
                @Override
                public void onResponse(Call<List<Akun>> call, Response<List<Akun>> response) {
                    Log.i("autolog", "onResponse");

                    userList = response.body();
                    Log.i("autolog", "List<Akun> userList = response.body();");

                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    Log.i("autolog", "RecyclerView recyclerView = findViewById(R.id.recyclerView);");

                    layoutManager = new LinearLayoutManager(UserActivity.this);
                    Log.i("autolog", "layoutManager = new LinearLayoutManager(UserActivity.this);");
                    recyclerView.setLayoutManager(layoutManager);
                    Log.i("autolog", "recyclerView.setLayoutManager(layoutManager);");

                    SpacingItemDecorator itemDecorator = new SpacingItemDecorator(10);
                    recyclerView.addItemDecoration(itemDecorator);

                    RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), userList, email);
                    Log.i("autolog", "RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), userList);");
                    recyclerView.setAdapter(recyclerViewAdapter);
                    Log.i("autolog", "recyclerView.setAdapter(recyclerViewAdapter);");
                }

                @Override
                public void onFailure(Call<List<Akun>> call, Throwable t) {
                    Log.i("autolog", t.getMessage());
                }
            });
        }catch (Exception e) {
            Log.i("autolog", "Exception");
        }
    }
}