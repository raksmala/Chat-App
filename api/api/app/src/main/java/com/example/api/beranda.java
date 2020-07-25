package com.example.api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

public class beranda extends AppCompatActivity {

    TextView tvResultName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_beranda);

        tvResultName = findViewById(R.id.textView17);

        Bundle myBundle2 = getIntent().getExtras();
        String name = myBundle2.getString("name");
        String email = myBundle2.getString("email");
        tvResultName.setText("Halo " +name);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle myBundle3 = new Bundle();
                myBundle3.putString("email", email);
                Intent intentUser = new Intent(beranda.this, UserActivity.class);
                intentUser.putExtras(myBundle3);
                startActivity(intentUser);
                finish();
            }
        }, 3000L);
    }

}