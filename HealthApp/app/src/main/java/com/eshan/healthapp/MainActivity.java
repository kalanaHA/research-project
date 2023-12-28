package com.eshan.healthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.eshan.healthapp.Database.LocaleDatabase;
import com.eshan.healthapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private LocaleDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = new LocaleDatabase(this);
        //System.out.println("User Name"+database.getAllUsers().get(0).getName());

        if (!database.getAllUsers().isEmpty()) {

            binding.getStartedButton.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //HomeActivity
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finishAffinity();

                }
            }, 3000);
        }

        binding.getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (database.getAllUsers().isEmpty()) {

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));

                }

            }
        });

    }
}