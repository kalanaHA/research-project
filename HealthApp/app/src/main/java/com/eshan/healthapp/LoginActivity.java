package com.eshan.healthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.eshan.healthapp.API.RequestData;
import com.eshan.healthapp.API.RetroServer;
import com.eshan.healthapp.Database.LocaleDatabase;
import com.eshan.healthapp.Database.MobileNoDatabase;
import com.eshan.healthapp.Models.MobileNumber;
import com.eshan.healthapp.Models.User;
import com.eshan.healthapp.ResponseModels.LoginResponse;
import com.eshan.healthapp.databinding.ActivityLoginBinding;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private String date;
    private String bloodType;
    private String healthStatus;
    private LocaleDatabase database;

    private String status;
    private int returnValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);

        status = getIntent().getStringExtra("status");
        if (status != null) {

            binding.stepText.setVisibility(View.GONE);
            binding.registrationText.setText(R.string.profile_update);
            binding.bloodTypeLayout.setVisibility(View.GONE);
            binding.healthStatusLayout.setVisibility(View.GONE);
            binding.healthStatusSpinner.setVisibility(View.GONE);
            binding.filledExposed.setVisibility(View.GONE);
            binding.selectDateButton.setVisibility(View.GONE);
            final String update = "Update";
            binding.loginButton.setText(update);

            getProfileFromDB();

        }

        final String [] bloodTypes = {"A Positive (A+)", "A Negative (A-)", "B Positive (B+)", "B Negative (B-), " +
                "AB Positive (AB+)", "AB Negative (AB-)", "O Positive (O+)", "O Negative (O-)"};
        final String [] healthStatusList = {"Heart Patient", "Diabetic", "Asthmatic", "Hypertensive", "Allergic",
                "Migraine Sufferer", "Arthritic"};

        database = new LocaleDatabase(LoginActivity.this);

        final ArrayAdapter<String>bloodTypesAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item, bloodTypes);
        binding.filledExposed.setAdapter(bloodTypesAdapter);
        binding.filledExposed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(LoginActivity.this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                bloodType = adapterView.getItemAtPosition(i).toString();
            }
        });

        final ArrayAdapter<String>healthStatusAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item, healthStatusList);
        binding.healthStatusSpinner.setAdapter(healthStatusAdapter);
        binding.healthStatusSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(LoginActivity.this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                healthStatus = adapterView.getItemAtPosition(i).toString();
            }
        });

        binding.selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectDateButtonProcess();

            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = binding.nameText.getText().toString();
                String address = binding.addressText.getText().toString();
                String phoneNumber = binding.phoneNumberText.getText().toString();

                if (status != null) {

                    updateProfile(name, address, phoneNumber);

                } else {

                    loginButtonProcess(name, address, bloodType, healthStatus, phoneNumber);

                }

            }
        });

    }

    private void getProfileFromDB() {

        User user = null;

        try (LocaleDatabase database = new LocaleDatabase(this)) {
            user = database.getUserById(1);
        }

        if (user != null) {

            binding.nameText.setText(user.getName());
            binding.addressText.setText(user.getAddress());

        }

        MobileNumber mobileNumber = null;
        try (MobileNoDatabase db = new MobileNoDatabase(this)) {
            mobileNumber = db.getMobileNoByID(1);
        }

        if (mobileNumber != null) {

            binding.phoneNumberText.setText(mobileNumber.getMobileNumber());

        }

    }

    private void updateProfile(String name, String address, String phoneNumber) {

        if (name.isEmpty()) {

            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();

        } else if (address.isEmpty()) {

            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();

        } else if (phoneNumber.isEmpty()) {

            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();

        } else {

            profileUpdateProcess(name, address, phoneNumber);

        }

    }

    private void profileUpdateProcess(String name, String address, String phoneNumber) {

        int updateStatus = database.updateUser(1, new User(name, address));

        if (updateStatus > 0) {

            // Update successful, user data was updated
            try (MobileNoDatabase db = new MobileNoDatabase(this)) {

                returnValue = db.updateMobileNumber(new MobileNumber(1, phoneNumber));

            }

            if (returnValue > 0) {
                // Update successful, user data was updated
                Toast.makeText(this, "Profile successfully Updated!", Toast.LENGTH_SHORT).show();
            } else if (returnValue == 0) {
                // No rows were updated, user with the given ID was not found
                Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            } else {
                // Update operation encountered an error
                Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            }

        } else if (updateStatus == 0) {

            // No rows were updated, user with the given ID was not found
            Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

        } else {

            // Update operation encountered an error
            Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
        }

    }

    private void loginButtonProcess(String name, String address, String bloodType, String healthStatus, String phoneNumber) {

        if (name.isEmpty()) {

            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();

        } else if (address.isEmpty()) {

            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();

        } else if (bloodType == null) {

            Toast.makeText(this, "Select Blood Type", Toast.LENGTH_SHORT).show();

        } else if (healthStatus == null) {

            Toast.makeText(this, "Select Health Status", Toast.LENGTH_SHORT).show();

        } else if (date == null) {

            Toast.makeText(this, "Select Date", Toast.LENGTH_SHORT).show();

        } else if (phoneNumber.isEmpty()) {

            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();

        } else {

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.loginButton.setEnabled(false);
            loginUser(new User(name, address, bloodType, healthStatus, phoneNumber));

        }

    }

    //user login process with API
    private void loginUser(User user) {

        JSONObject jsonObjectMain = new JSONObject();

        try {

            jsonObjectMain.put("name", user.getName());
            jsonObjectMain.put("dob", date);
            jsonObjectMain.put("blood_group", user.getBloodType());
            jsonObjectMain.put("health_status", user.getHealthStatus());
            jsonObjectMain.put("address", user.getAddress());

        } catch (Exception e) {

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonObjectMain.toString());

        System.out.println(jsonObject.toString());

        RequestData requestData = RetroServer.connectRetrofit().create(RequestData.class);
        Call<LoginResponse>call = requestData.getLoginResponse(jsonObject);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {

                // TODO : Implement loading layout part here - Done (2023/07/18 - 2:07PM)

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        if (response.body().getMsg().equals("success")) {

                            //System.out.println("Msg : "+response.body().getMsg());

                            // TODO : implement local database part here - Done (2023/07/29 - 9:25PM)
                            long status = database.addUser(new User(user.getName(), user.getAddress(), user.getBloodType(), user.getHealthStatus()));

                            if (status != -1) {

                                try (MobileNoDatabase mobileNoDB = new MobileNoDatabase(getApplicationContext())) {

                                    mobileNoDB.addMobileNumber(new MobileNumber(user.getPhoneNumber()));

                                }

                                Toast.makeText(LoginActivity.this, "Successfully Logged In!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {

                                Toast.makeText(LoginActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

                            }

                        } else {

                            Toast.makeText(LoginActivity.this, "Something Went Wrong, Try Again!", Toast.LENGTH_SHORT).show();

                        }

                    } else {

                        Toast.makeText(LoginActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(LoginActivity.this, "Error - "+response.code(), Toast.LENGTH_SHORT).show();

                }

                binding.progressBar.setVisibility(View.GONE);
                binding.loginButton.setEnabled(true);

            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {

                Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
                binding.loginButton.setEnabled(true);

            }
        });

    }

    private void selectDateButtonProcess() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // get selected date
                date = day+" - "+new DateFormatSymbols().getMonths()[month]+" - "+year;
                System.out.println(date);

            }
        }, year, month, day);

        datePickerDialog.show();

    }

}