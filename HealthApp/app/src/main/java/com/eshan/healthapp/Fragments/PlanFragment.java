package com.eshan.healthapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eshan.healthapp.API.RequestData;
import com.eshan.healthapp.API.RetroServer;
import com.eshan.healthapp.Database.LocaleDatabase;
import com.eshan.healthapp.Models.User;
import com.eshan.healthapp.R;
import com.eshan.healthapp.ResponseModels.DietPlanResponse;
import com.eshan.healthapp.databinding.FragmentPlanBinding;
import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanFragment extends Fragment {

    private FragmentPlanBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        binding = FragmentPlanBinding.bind(view);
        binding.toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedTitleAppearance);
        toolBarCustomization();
        getDataFromLocalDB();
        getDataFromAPI();

        return view;
    }

    private void getDataFromLocalDB() {

        User user = null;

        try (LocaleDatabase database = new LocaleDatabase(requireContext())) {
            user = database.getUserById(1);
        }

        if (user != null) {

            binding.userNameText.setText(user.getName());

        } else {

            String userName = "ERROR";
            binding.userNameText.setText(userName);

        }

    }

    // get data from diet-plan API
    private void getDataFromAPI() {

        RequestData requestData = RetroServer.connectRetrofit().create(RequestData.class);
        Call<List<DietPlanResponse>>responseCall = requestData.getDietPlan();
        responseCall.enqueue(new Callback<List<DietPlanResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<DietPlanResponse>> call, @NonNull Response<List<DietPlanResponse>> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        if (!response.body().isEmpty()) {

                            DietPlanResponse planResponse = response.body().get(0);
                            binding.breakfastContent.setText(planResponse.getDiet_plan().getBreakfast());
                            binding.lunchContent.setText(planResponse.getDiet_plan().getLunch());
                            binding.dinnerContent.setText(planResponse.getDiet_plan().getDinner());
                            binding.planContainer.setVisibility(View.VISIBLE);

                        } else {

                            Toast.makeText(requireContext(), "Something Went Wrong, Sorry!", Toast.LENGTH_SHORT).show();

                        }

                    } else {

                        Toast.makeText(requireContext(), "Something Went Wrong, Sorry!", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(requireContext(), "Error - "+response.code(), Toast.LENGTH_SHORT).show();

                }

                binding.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NonNull Call<List<DietPlanResponse>> call, @NonNull Throwable t) {

                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getLocalizedMessage());

            }
        });

    }

    private void toolBarCustomization() {

        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.toolbarLayout.setTitle(getResources().getString(R.string.analytics));
                    isShow = true;
                } else if(isShow) {
                    binding.toolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

    }

}