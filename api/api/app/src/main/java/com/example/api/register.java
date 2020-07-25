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

public class register extends AppCompatActivity {

    Button btnRegister, btnLoginAct;
    EditText etName, etEmail, etPassword, etPassword2;
    TextView tvToggle, tvToggle2;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.168.1.9:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        btnRegister = findViewById(R.id.btnRegister);
        btnLoginAct = findViewById(R.id.btnLoginAct);
        etName = findViewById(R.id.textView8);
        etEmail = findViewById(R.id.textView10);
        etPassword = findViewById(R.id.textView12);
        etPassword2 = findViewById(R.id.textView14);
        tvToggle = findViewById(R.id.textView20);
        tvToggle2 = findViewById(R.id.textView21);

        tvToggle.setVisibility(View.GONE);
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        tvToggle2.setVisibility(View.GONE);
        etPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

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

        etPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etPassword2.getText().length() > 0) {
                    tvToggle2.setVisibility(View.VISIBLE);
                } else {
                    tvToggle2.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvToggle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvToggle2.getText() == "SHOW") {
                    tvToggle2.setText("HIDE");
                    etPassword2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etPassword2.setSelection(etPassword2.length());
                } else {
                    tvToggle2.setText("SHOW");
                    etPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPassword2.setSelection(etPassword2.length());
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String password2 = etPassword2.getText().toString().trim();
                String name = etName.getText().toString().trim();

                if (name.isEmpty()) {
                    etName.setError("Nama harus diisi.");
                    etName.setFocusable(true);
                }else if (email.isEmpty()){
                    etEmail.setError("Email harus diisi.");
                    etEmail.setFocusable(true);
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.setError("Format email salah.");
                    etEmail.setFocusable(true);
                }else if (password.isEmpty()){
                    etPassword.setError("Kolom harus diisi");
                    etPassword.setFocusable(true);
                }else if (password.length()<6){
                    etPassword.setError("Masukan setidaknya 6 karakter");
                    etPassword.setFocusable(true);
                }else if (password2.isEmpty()){
                    etPassword2.setError("Kolom harus diisi");
                    etPassword2.setFocusable(true);
                }else if (!password2.equals(password)){
                    etPassword2.setError("Password tidak sama");
                    etPassword2.setFocusable(true);
                }else {
                    handleRegister();
                }
            }
        });

        btnLoginAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this, MainActivity.class));
            }
        });
    }

    private void handleRegister() {

        HashMap<String, String> map = new HashMap<>();

        map.put("name", etName.getText().toString());
        map.put("email", etEmail.getText().toString());
        map.put("password", etPassword.getText().toString());
        map.put("password2", etPassword2.getText().toString());

        Call<Void> call = retrofitInterface.executeRegister(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200) {
                    Toast.makeText(register.this, "Registrasi Berhasil", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(register.this, MainActivity.class));
                } else if(response.code() == 400) {
                    Toast.makeText(register.this, "Akun Sudah terdaftar", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(register.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}