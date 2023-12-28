package com.eshan.healthapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eshan.healthapp.API.RequestData;
import com.eshan.healthapp.API.RetroServer;
import com.eshan.healthapp.Adapters.ExercisePlanAdapter;
import com.eshan.healthapp.Database.LocaleDatabase;
import com.eshan.healthapp.Models.ExercisePlan;
import com.eshan.healthapp.Models.User;
import com.eshan.healthapp.R;
import com.eshan.healthapp.ResponseModels.DietPlanResponse;
import com.eshan.healthapp.ResponseModels.ExercisePlanResponse;
import com.eshan.healthapp.databinding.FragmentExercisePlanBinding;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExercisePlanFragment extends Fragment {

    private FragmentExercisePlanBinding binding;
    private List<ExercisePlanResponse>exercisePlanList;
    private ExercisePlanAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exercise_plan, container, false);
        binding = FragmentExercisePlanBinding.bind(v);

        binding.toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedTitleAppearance);
        toolBarCustomization();
        getDataFromLocalDB();
        recyclerViewConfig();
        getDataFromAPI();

        return v;
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

    private void recyclerViewConfig() {

        exercisePlanList = new ArrayList<>();
        adapter = new ExercisePlanAdapter(requireContext(), exercisePlanList);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);

    }

    //get data from exercise-plan API
    private void getDataFromAPI() {

        RequestData requestData = RetroServer.connectRetrofit().create(RequestData.class);
        Call<List<ExercisePlanResponse>>responseCall = requestData.getExercisePlan();
        responseCall.enqueue(new Callback<List<ExercisePlanResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ExercisePlanResponse>> call, @NonNull Response<List<ExercisePlanResponse>> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        if (!response.body().isEmpty()) {

                            exercisePlanList.addAll(response.body());
                            adapter.notifyDataSetChanged();

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
            public void onFailure(@NonNull Call<List<ExercisePlanResponse>> call, @NonNull Throwable t) {

                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

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