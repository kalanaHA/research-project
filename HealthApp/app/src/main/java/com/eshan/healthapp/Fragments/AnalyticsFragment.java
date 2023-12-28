package com.eshan.healthapp.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eshan.healthapp.API.RequestData;
import com.eshan.healthapp.API.RetroServer;
import com.eshan.healthapp.Adapters.HealthStatusAdapter;
import com.eshan.healthapp.Database.LocaleDatabase;
import com.eshan.healthapp.DementiaActivity;
import com.eshan.healthapp.Models.RiskStatus;
import com.eshan.healthapp.Models.User;
import com.eshan.healthapp.R;
import com.eshan.healthapp.databinding.FragmentAnalyticsBinding;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticsFragment extends Fragment {

    private FragmentAnalyticsBinding binding;
    private List<RiskStatus>riskStatusList;
    private HealthStatusAdapter adapter;
    private User user;

    private List<PieEntry>pieChartDataList;
    private PieDataSet pieDataSet;
    private PieData pieData;
    private int normalValue;
    private int manageableValue;
    private int normalHealthy;
    private int riskyValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        binding = FragmentAnalyticsBinding.bind(view);
        binding.toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedTitleAppearance);

        toolBarCustomization();
        getDataFromLocalDB();
        pieChartConfig();
        setDataToRecyclerView();

        binding.getDementiaPredictionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), DementiaActivity.class));
            }
        });

        binding.getReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("https://5029-2402-d000-8130-4007-996b-fe38-7791-6a5c.ngrok-free.app/api/report"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        return view;
    }


    private void pieChartConfig() {

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN); // Green for "Manageable"
        colors.add(ColorTemplate.COLORFUL_COLORS[0]); // First color for "Normal"
        colors.add(ColorTemplate.COLORFUL_COLORS[1]); // Second color for "Normal Healthy"
        colors.add(Color.RED); // Third color for "Risky"

        pieChartDataList = new ArrayList<>();
        pieDataSet = new PieDataSet(pieChartDataList, getResources().getString(R.string.health_status));
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        pieDataSet.setLabel(getResources().getString(R.string.health_status));
        pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(12f);
        binding.pieChart.setData(pieData);
        binding.pieChart.invalidate();

    }

    private void getDataFromLocalDB() {

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

    private void setDataToRecyclerView() {

        riskStatusList = new ArrayList<>();
        adapter = new HealthStatusAdapter(requireContext(), riskStatusList, user);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

        getDataFromAPI();

    }


    // get data from isk-history API
    private void getDataFromAPI() {

        RequestData requestData = RetroServer.connectRetrofit().create(RequestData.class);
        Call<List<RiskStatus>>call = requestData.getRiskStatus();
        call.enqueue(new Callback<List<RiskStatus>>() {
            @Override
            public void onResponse(@NonNull Call<List<RiskStatus>> call, @NonNull Response<List<RiskStatus>> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        if (!response.body().isEmpty()) {

                            riskStatusList.addAll(response.body());
                            setDataToPieChart(response.body());
                            adapter.notifyDataSetChanged();

                        } else {

                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();

                        }

                    } else {

                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(requireContext(), "Error - "+response.code(), Toast.LENGTH_SHORT).show();

                }

                binding.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NonNull Call<List<RiskStatus>> call, @NonNull Throwable t) {

                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getLocalizedMessage());
                binding.progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void setDataToPieChart(List<RiskStatus> riskStatusList) {

        for (RiskStatus riskStatus : riskStatusList) {

            switch (riskStatus.getRisk_level()) {
                case "Manageable":

                    manageableValue++;

                    break;
                case "Normal":

                    normalValue++;

                    break;
                case "Normal Healthy":

                    normalHealthy++;

                    break;
                default:

                    riskyValue++;

                    break;
            }

        }

        pieChartDataList.add(new PieEntry((float) manageableValue, "Manageable"));
        pieChartDataList.add(new PieEntry((float) normalValue, "Normal"));
        pieChartDataList.add(new PieEntry((float) normalHealthy, "Normal Healthy"));
        pieChartDataList.add(new PieEntry((float) riskyValue, "Risky"));

        pieDataSet.notifyDataSetChanged();
        PieData data = new PieData(pieDataSet);
        binding.pieChart.setData(data);
        binding.pieChart.invalidate();

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