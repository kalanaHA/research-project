package com.eshan.healthapp.FaceAPI;

import com.eshan.healthapp.ResponseModels.EmotionDetectionResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RequestDataFaceAPI {

    @Multipart
    @POST("/predict")
    Call<EmotionDetectionResponse>getResults(@Part MultipartBody.Part image);

}
