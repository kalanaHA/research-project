package com.eshan.healthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.eshan.healthapp.Fragments.AnalyticsFragment;
import com.eshan.healthapp.Fragments.EmotionDetectionFragment;
import com.eshan.healthapp.Fragments.ExercisePlanFragment;
import com.eshan.healthapp.Fragments.HomeFragment;
import com.eshan.healthapp.Fragments.PlanFragment;
import com.eshan.healthapp.Fragments.ProfileFragment;
import com.eshan.healthapp.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        moveToFragment(new HomeFragment());

//        binding.dietPlan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                startActivity(new Intent(HomeActivity.this, DietPlanActivity.class));
//
//            }
//        });

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.home) {

                    moveToFragment(new HomeFragment());

                } else if (item.getItemId() == R.id.dietPlan) {

                    moveToFragment(new PlanFragment());

                } else if (item.getItemId() == R.id.exercisePlan) {

                    moveToFragment(new ExercisePlanFragment());

                } else if (item.getItemId() == R.id.analytics) {

                    moveToFragment(new AnalyticsFragment());

                } else {

                    moveToFragment(new EmotionDetectionFragment());
//                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
//                    intent.putExtra("status", "status");
//                    startActivity(intent);

                }

                return true;
            }
        });

    }

    private void moveToFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();

    }

}