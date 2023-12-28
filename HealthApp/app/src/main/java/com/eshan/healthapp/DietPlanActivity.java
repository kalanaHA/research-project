package com.eshan.healthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.eshan.healthapp.API.RequestData;
import com.eshan.healthapp.API.RetroServer;
import com.eshan.healthapp.Adapters.DietPlanAdapter;
import com.eshan.healthapp.Models.DietPlan;
import com.eshan.healthapp.ResponseModels.LoginResponse;
import com.eshan.healthapp.databinding.ActivityDietPlanBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietPlanActivity extends AppCompatActivity {
    
    private ActivityDietPlanBinding binding;
//    private List<DietPlan>dietPlanList;
//    private DietPlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDietPlanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
//        getDataFromAPI();
//
//        setRecyclerView();
        
    }

//    private void setRecyclerView() {
//
//        dietPlanList = new ArrayList<>();
//        adapter = new DietPlanAdapter(this, dietPlanList);
//        binding.dietPlanRecyclerView.setHasFixedSize(true);
//        binding.dietPlanRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        binding.dietPlanRecyclerView.setAdapter(adapter);
//
//    }
//
//    private void getDataFromAPI() {
//
//        binding.progressBar.setVisibility(View.VISIBLE);
//        RequestData requestData = FaceRetroServer.connectRetrofit().create(RequestData.class);
//        Call<List<DietPlan>> call = requestData.getDietPlan();
//        call.enqueue(new Callback<List<DietPlan>>() {
//            @Override
//            public void onResponse(@NonNull Call<List<DietPlan>> call, @NonNull Response<List<DietPlan>> response) {
//
//                if (response.isSuccessful()) {
//
//                    if (response.body() != null) {
//
//                        if (!response.body().isEmpty()) {
//
//                            dietPlanList.addAll(response.body());
//                            adapter.notifyDataSetChanged();
//
//
//                        } else {
//
//                            Toast.makeText(DietPlanActivity.this, "Something Went Wrong, Try Again!", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    } else {
//
//                        Toast.makeText(DietPlanActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
//
//                    }
//
//                } else {
//
//                    Toast.makeText(DietPlanActivity.this, "Error - "+response.code(), Toast.LENGTH_SHORT).show();
//
//                }
//
//                binding.progressBar.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<List<DietPlan>> call, @NonNull Throwable t) {
//
//                Toast.makeText(DietPlanActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                System.out.println(t.getLocalizedMessage());
//                binding.progressBar.setVisibility(View.GONE);
//
//            }
//        });
//
//    }
}