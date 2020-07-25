package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnRegisAct;
    EditText etEmail, etPassword;
    TextView tvToggle;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.168.1.9:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegisAct = findViewById(R.id.btnRegisterAct);
        etEmail = findViewById(R.id.textView3);
        etPassword = findViewById(R.id.textView5);
        tvToggle = findViewById(R.id.textView19);

        tvToggle.setVisibility(View.GONE);
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etPassword.getText().length() > 0) {
                    tvToggle.setVisibility(View.VISIBLE);
                } else {
                    tvToggle.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvToggle.getText() == "SHOW") {
                    tvToggle.setText("HIDE");
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etPassword.setSelection(etPassword.length());
                } else {
                    tvToggle.setText("SHOW");
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPassword.setSelection(etPassword.length());
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty()){
                    etEmail.setError("Email harus diisi.");
                    etEmail.setFocusable(true);
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.setError("Format email salah.");
                    etEmail.setFocusable(true);
                }else if (password.isEmpty()){
                    etPassword.setError("Kolom harus diisi");
                    etPassword.setFocusable(true);
                }else {
                    handleLogin();
                }
            }
        });
        
        btnRegisAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, register.class));
            }
        });
    }

    private void handleLogin() {

        HashMap<String, String> map = new HashMap<>();

        map.put("email", etEmail.getText().toString());
        map.put("password", etPassword.getText().toString());

        Call<LoginResults> call = retrofitInterface.executeLogin(map);

        call.enqueue(new Callback<LoginResults>() {
            @Override
            public void onResponse(Call<LoginResults> call, Response<LoginResults> response) {
                if(response.code() == 200) {
                    LoginResults result = response.body();
                    Intent intentBeranda = new Intent(MainActivity.this, beranda.class);
                    Bundle myBundle = new Bundle();
                    String name = result.getName();
                    String email = result.getEmail();
                    myBundle.putString("name", name);
                    myBundle.putString("email", email);
                    intentBeranda.putExtras(myBundle);
                    Toast.makeText(MainActivity.this, "Selamat Datang " +name, Toast.LENGTH_LONG).show();
                    startActivity(intentBeranda);
                } else if(response.code() == 404) {
                    Toast.makeText(MainActivity.this, "Email atau Password salah", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResults> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}