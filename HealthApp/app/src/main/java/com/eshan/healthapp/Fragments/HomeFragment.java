package com.eshan.healthapp.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.eshan.healthapp.API.RequestData;
import com.eshan.healthapp.API.RetroServer;
import com.eshan.healthapp.Database.LocaleDatabase;
import com.eshan.healthapp.DementiaActivity;
import com.eshan.healthapp.Models.User;
import com.eshan.healthapp.R;
import com.eshan.healthapp.ResponseModels.EmergencyResponse;
import com.eshan.healthapp.ResponseModels.LatestReadingsResponse;
import com.eshan.healthapp.databinding.FragmentHomeBinding;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.sql.Date;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private String startDate;
    private String endDate;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.bind(view);
        binding.toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedTitleAppearance);

        toolBarCustomization();
        getDataFromLocalDB();

        binding.cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                try {
//                    Intent intent = requireContext().getPackageManager().getLaunchIntentForPackage("com.relax.app");
//
//                    if (intent == null) {
//
//                        Toast.makeText(requireContext(), "This app is not installed on your device.", Toast.LENGTH_SHORT).show();
//
//                    } else {
//
//                        startActivity(intent);
//
//                    }
//
//                } catch (Exception e) {
//
//                    Toast.makeText(requireContext(), "This app is not installed on your device.", Toast.LENGTH_SHORT).show();
//
//                }

                //startActivity(new Intent(requireContext(), DementiaActivity.class));

            }
        });

        binding.selectStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectStartDate();

            }
        });

        binding.selectEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectEndDate();

            }
        });

        binding.showDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDataButtonProcess();

            }
        });

        binding.emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.emergencyButton.setEnabled(false);
                emergencyButtonProcess();

            }
        });

        return view;
    }

    private void emergencyButtonProcess() {

        RequestData requestData = RetroServer.connectRetrofit().create(RequestData.class);
        Call<EmergencyResponse>responseCall = requestData.callEmergency();
        responseCall.enqueue(new Callback<EmergencyResponse>() {
            @Override
            public void onResponse(@NonNull Call<EmergencyResponse> call, @NonNull Response<EmergencyResponse> response) {
                
                if (response.isSuccessful()) {
                    
                    if (response.body() != null) {
                        
                        if (response.body().isOk()) {

                            Toast.makeText(requireContext(), "Your alert has been sent successfully!", Toast.LENGTH_LONG).show();
                            
                        } else {

                            Toast.makeText(requireContext(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();

                        }
                        
                    } else {

                        Toast.makeText(requireContext(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                        
                    }
                    
                } else {

                    Toast.makeText(requireContext(), "Error - "+response.code(), Toast.LENGTH_SHORT).show();
                    
                }

                binding.emergencyButton.setEnabled(true);
                
            }

            @Override
            public void onFailure(@NonNull Call<EmergencyResponse> call, @NonNull Throwable t) {

                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getLocalizedMessage());
                binding.emergencyButton.setEnabled(true);

            }
        });

    }

    private void showDataButtonProcess() {

        if (startDate == null) {

            Toast.makeText(requireContext(), "Select Start Date", Toast.LENGTH_SHORT).show();

        } else if (endDate == null) {

            Toast.makeText(requireContext(), "Select End Date", Toast.LENGTH_SHORT).show();

        } else {

            binding.showDataButton.setEnabled(false);
            binding.progressBar.setVisibility(View.VISIBLE);
            getDataFromAPI();

        }

    }

    private JsonObject getDatesAsJson() {

        JSONObject jsonObjectMain = new JSONObject();

        try {

            jsonObjectMain.put("start_date", startDate);
            jsonObjectMain.put("end_date", endDate);

        } catch (Exception e) {

            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonObjectMain.toString());

        System.out.println(jsonObject.toString());

        return jsonObject;

    }

    //get data from latest reading api
    private void getDataFromAPI() {

        RequestData requestData = RetroServer.connectRetrofit().create(RequestData.class);
        Call<List<LatestReadingsResponse>> call = requestData.getLatestReadings(getDatesAsJson());
        call.enqueue(new Callback<List<LatestReadingsResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<LatestReadingsResponse>> call, @NonNull Response<List<LatestReadingsResponse>> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null ) {

                        if (!response.body().isEmpty()) {

                            customizeData(response.body());
                            binding.dataContainer.setVisibility(View.VISIBLE);

                        } else {

                            Toast.makeText(requireContext(), "Sorry, no data available at the moment!", Toast.LENGTH_LONG).show();

                        }

                    } else {

                        Toast.makeText(requireContext(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(requireContext(), "Error - "+response.code(), Toast.LENGTH_SHORT).show();

                }

                binding.showDataButton.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                binding.emergencyButton.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(@NonNull Call<List<LatestReadingsResponse>> call, @NonNull Throwable t) {

                binding.showDataButton.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getLocalizedMessage());

            }
        });

    }

    private void customizeData(List<LatestReadingsResponse> body) {

        List<Integer>bloodOxygenLevels = new ArrayList<>();
        List<Integer>heartRateLineValues = new ArrayList<>();
        List<Float>bodyTemperatureValues = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            bloodOxygenLevels.add(body.get(i).getBloodOxygenLevel());
            heartRateLineValues.add(body.get(i).getHeartRate());
            bodyTemperatureValues.add((float) body.get(i).getBodyTemperature());

        }

        bloodOxygenLevelChart(bloodOxygenLevels);
        heartRateLevelChart(heartRateLineValues);

        List<Integer> intList = new ArrayList<>();
        for (float floatValue : bodyTemperatureValues) {
            int intValue = (int) floatValue; // Truncate the decimal part
            intList.add(intValue);
        }

        bodyTemperatureLevelChart(intList);

    }

    private void selectEndDate() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // Adjust the month value to start from 1 (January)
                month = month + 1;

                // Create a SimpleDateFormat object to format the date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                // Create the startDate string using the formatted date
                endDate = dateFormat.format(new Date(year - 1900, month - 1, dayOfMonth));
                System.out.println("End Date : "+endDate);

            }
        }, year, month, day);

        datePickerDialog.show();

    }

    private void selectStartDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Adjust the month value to start from 1 (January)
                month = month + 1;

                // Create a SimpleDateFormat object to format the date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                // Create the startDate string using the formatted date
                startDate = dateFormat.format(new Date(year - 1900, month - 1, dayOfMonth));

                System.out.println("Start Date: " + startDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void bloodOxygenLevelChart(List<Integer>values) {

        // Customization
        binding.bloodOxygenLevelLineChart.getDescription().setEnabled(false);
        binding.bloodOxygenLevelLineChart.setTouchEnabled(true);
        binding.bloodOxygenLevelLineChart.setDragEnabled(true);
        binding.bloodOxygenLevelLineChart.setScaleEnabled(true);
        binding.bloodOxygenLevelLineChart.setPinchZoom(true);
        binding.bloodOxygenLevelLineChart.setDrawGridBackground(true);

        XAxis xAxis = binding.bloodOxygenLevelLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        YAxis leftAxis = binding.bloodOxygenLevelLineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(112f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {

            entries.add(new Entry(i, values.get(i)));

        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Blood Oxygen Level");
        lineDataSet.setColor(Color.parseColor("#3390FF"));
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleColor(Color.parseColor("#3390FF"));
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawValues(true);

        LineData lineData = new LineData(lineDataSet);

        binding.bloodOxygenLevelLineChart.setData(lineData);
        binding.bloodOxygenLevelLineChart.invalidate(); // Refresh the chart to display the data

    }

    private void heartRateLevelChart(List<Integer>values) {

        // Customization
        binding.heartRateLineChart.getDescription().setEnabled(false);
        binding.heartRateLineChart.setTouchEnabled(true);
        binding.heartRateLineChart.setDragEnabled(true);
        binding.heartRateLineChart.setScaleEnabled(true);
        binding.heartRateLineChart.setPinchZoom(true);
        binding.heartRateLineChart.setDrawGridBackground(true);

        XAxis xAxis = binding.heartRateLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        YAxis leftAxis = binding.heartRateLineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(100f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {

            entries.add(new Entry(i, values.get(i)));

        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Heart Rate");
        lineDataSet.setColor(Color.parseColor("#3390FF"));
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleColor(Color.parseColor("#3390FF"));
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawValues(true);

        LineData lineData = new LineData(lineDataSet);

        binding.heartRateLineChart.setData(lineData);
        binding.heartRateLineChart.invalidate(); // Refresh the chart to display the data

    }

    private void bodyTemperatureLevelChart(List<Integer>values) {

        // Customization
        binding.bodyTemperatureLineChart.getDescription().setEnabled(false);
        binding.bodyTemperatureLineChart.setTouchEnabled(true);
        binding.bodyTemperatureLineChart.setDragEnabled(true);
        binding.bodyTemperatureLineChart.setScaleEnabled(true);
        binding.bodyTemperatureLineChart.setPinchZoom(true);
        binding.bodyTemperatureLineChart.setDrawGridBackground(true);

        XAxis xAxis = binding.bodyTemperatureLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        YAxis leftAxis = binding.bodyTemperatureLineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(100f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {

            entries.add(new Entry(i, values.get(i)));

        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Body Temperature Level");
        lineDataSet.setColor(Color.parseColor("#3390FF"));
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleColor(Color.parseColor("#3390FF"));
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawValues(true);

        LineData lineData = new LineData(lineDataSet);

        binding.bodyTemperatureLineChart.setData(lineData);
        binding.bodyTemperatureLineChart.invalidate(); // Refresh the chart to display the data

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
                    binding.toolbarLayout.setTitle(getResources().getString(R.string.home));
                    isShow = true;
                } else if(isShow) {
                    binding.toolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

    }


}