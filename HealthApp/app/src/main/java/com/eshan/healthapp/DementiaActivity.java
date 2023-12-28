package com.eshan.healthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.eshan.healthapp.DementiaAPI.DementiaRequestData;
import com.eshan.healthapp.DementiaAPI.DementiaRetroServer;
import com.eshan.healthapp.ResponseModels.DementiaResponse;
import com.eshan.healthapp.databinding.ActivityDementiaBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DementiaActivity extends AppCompatActivity {

    private ActivityDementiaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDementiaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.getPredictionsButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String alcoholLevel = binding.alcoholLevelText.getText().toString();
                String weight = binding.weightText.getText().toString();
                String mriDelay = binding.mriDelayText.getText().toString();

                if (alcoholLevel.isEmpty()) {

                    Toast.makeText(DementiaActivity.this, "Please Input Your Alcohol Level", Toast.LENGTH_SHORT).show();

                } else if (weight.isEmpty()) {

                    Toast.makeText(DementiaActivity.this, "Please Input Your Weight", Toast.LENGTH_SHORT).show();

                } else if (mriDelay.isEmpty()) {

                    Toast.makeText(DementiaActivity.this, "Please Input Your MRI Delay", Toast.LENGTH_SHORT).show();

                } else {

                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.getPredictionsButtons.setEnabled(false);
                    getPredictionsButtonProcess(alcoholLevel, weight, mriDelay);
                    binding.predictionsText.setVisibility(View.GONE);

                }
            }
        });

    }

    private void getPredictionsButtonProcess(String alcoholLevel, String weight, String mriDelay) {

        DementiaRequestData requestData = DementiaRetroServer.connectRetrofit().create(DementiaRequestData.class);
        Call<DementiaResponse>call = requestData.getPrediction(getSelectedDiabeticType(), Float.parseFloat(alcoholLevel), Float.parseFloat(weight), Float.parseFloat(mriDelay));
        call.enqueue(new Callback<DementiaResponse>() {
            @Override
            public void onResponse(@NonNull Call<DementiaResponse> call, @NonNull Response<DementiaResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        binding.predictionsText.setText(response.body().getResult().getMessage());
                        binding.predictionsText.setVisibility(View.VISIBLE);

                    } else {

                        Toast.makeText(DementiaActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(DementiaActivity.this, "Error - "+response.code(), Toast.LENGTH_SHORT).show();
                    
                }

                binding.progressBar.setVisibility(View.GONE);
                binding.getPredictionsButtons.setEnabled(true);

            }

            @Override
            public void onFailure(@NonNull Call<DementiaResponse> call, @NonNull Throwable t) {

                binding.progressBar.setVisibility(View.GONE);
                binding.getPredictionsButtons.setEnabled(true);
                Toast.makeText(DementiaActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getLocalizedMessage());

            }
        });

    }

    private int getSelectedDiabeticType() {

        RadioButton selectedButton = findViewById(binding.radioGroup.getCheckedRadioButtonId());
        String status = selectedButton.getText().toString();

        if (status.equals("Yes")) {

            return 1;

        } else {

            return 0;

        }

    }

}