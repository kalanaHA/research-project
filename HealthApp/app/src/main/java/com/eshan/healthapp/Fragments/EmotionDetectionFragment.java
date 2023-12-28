package com.eshan.healthapp.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eshan.healthapp.Database.LocaleDatabase;
import com.eshan.healthapp.FaceAPI.FaceRetroServer;
import com.eshan.healthapp.FaceAPI.RequestDataFaceAPI;
import com.eshan.healthapp.Models.User;
import com.eshan.healthapp.R;
import com.eshan.healthapp.ResponseModels.EmotionDetectionResponse;
import com.eshan.healthapp.databinding.FragmentEmotionDetectionBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmotionDetectionFragment extends Fragment {

    private FragmentEmotionDetectionBinding binding;
    private File userImageFile;
    private static final int START_CAMERA_CODE = 2;
    private String videoLink;

    private User user;
    private ActivityResultLauncher<String[]> requestMultiplePermissionsLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> results) {
                    // Check if all permissions are granted
                    boolean allPermissionsGranted = true;
                    for (Map.Entry<String, Boolean> entry : results.entrySet()) {
                        if (!entry.getValue()) {
                            allPermissionsGranted = false;
                            break;
                        }
                    }

                    if (allPermissionsGranted) {
                        // All permissions are granted. Proceed with your code.
                        proceedWithOpenCamera();
                    } else {
                        // Permission request was denied. Handle it accordingly, e.g., show a message.
                        Toast.makeText(requireContext(), "Permissions denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emotion_detection, container, false);
        binding = FragmentEmotionDetectionBinding.bind(view);

        getDataFromLocalDB();
        
        binding.detectEmotionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assert userImageFile != null;
//                if ( userImageFile.exists()) {
//
//                   getResults(userImageFile);
//
//                }
                if (userImageFile != null && userImageFile.exists()) {
                    getResults(userImageFile);
                }
                
            }
        });

        binding.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                detectEmotionsProcess();

            }
        });

        binding.enjoyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Uri uri = Uri.parse(videoLink);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                } catch (Exception e) {

                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });

        return view;
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

    private void getResults(File userImageFile) {

        binding.detectEmotionsButton.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        try {

            RequestBody request = RequestBody.create(MediaType.parse("multipart/form-data"), userImageFile);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", userImageFile.getName(), request);

            RequestDataFaceAPI requestData = FaceRetroServer.connectRetrofit().create(RequestDataFaceAPI.class);
            Call<EmotionDetectionResponse>call = requestData.getResults(filePart);
            call.enqueue(new Callback<EmotionDetectionResponse>() {
                @Override
                public void onResponse(@NonNull Call<EmotionDetectionResponse> call, @NonNull Response<EmotionDetectionResponse> response) {

                    if (response.isSuccessful()) {

                        if (response.body() != null) {

                            System.out.println("Class : "+response.body().getClassValue());
                            System.out.println("emotion : "+response.body().getEmotion());
                            System.out.println("music_link : "+response.body().getMusicLink());

                            videoLink = response.body().getMusicLink();
                            String emotion = "Emotion : "+response.body().getEmotion();
                            binding.emotionText.setText(emotion);

                            binding.resultLayout.setVisibility(View.VISIBLE);

                        } else {

                            Toast.makeText(requireContext(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();

                        }

                    } else {

                        Toast.makeText(requireContext(), "Error - "+response.code(), Toast.LENGTH_SHORT).show();

                    }

                    binding.detectEmotionsButton.setEnabled(true);
                    binding.progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NonNull Call<EmotionDetectionResponse> call, @NonNull Throwable t) {

                    binding.detectEmotionsButton.setEnabled(true);
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {

            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    private void detectEmotionsProcess() {

        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if ((ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {

            // Request permissions that are not granted
            requestMultiplePermissionsLauncher.launch(permissions);

        } else {

            proceedWithOpenCamera();

        }

    }

    private File createPhotoFile() {

        File photoFileDir = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "SIGN_IMAGES");

        if (!photoFileDir.exists()) {

            photoFileDir.mkdirs();

        }

        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+".jpg";
        return new File(photoFileDir.getPath()+File.separator+name);

    }

    private void proceedWithOpenCamera() {

        userImageFile = createPhotoFile();

        try {

            Uri fileURI = FileProvider.getUriForFile(requireContext(), "com.eshan.file_provider", userImageFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileURI);
            startActivityForResult(intent, START_CAMERA_CODE);

        } catch (Exception e) {

            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == START_CAMERA_CODE) {

                System.out.println("callback camera");
                System.out.println("Image Path : "+userImageFile.getAbsolutePath());

                try {

                    Bitmap bitmap = BitmapFactory.decodeFile(userImageFile.getAbsolutePath());
                    binding.userImage.setImageBitmap(bitmap);
                    //checkWithAPI(new File(signImageFile.getAbsolutePath()));

                } catch (Exception e) {

                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }

        } else {

            Toast.makeText(requireContext(), "Something Went wrong!", Toast.LENGTH_SHORT).show();

        }

    }

}