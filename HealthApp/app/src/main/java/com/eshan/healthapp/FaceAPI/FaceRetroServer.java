package com.eshan.healthapp.FaceAPI;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FaceRetroServer {

    private static final String BASE_URL = "https://85de-175-157-30-23.ngrok-free.app";
    private static Retrofit retrofit;

    public static Retrofit connectRetrofit() {

        if (retrofit == null) {

            // Create an OkHttp client with logging interceptor
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            // Set a 5-minute (300,000 milliseconds) connection and read timeout
            httpClient.connectTimeout(300000, TimeUnit.MILLISECONDS);
            httpClient.readTimeout(300000, TimeUnit.MILLISECONDS);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Set log level to BODY for full logging
            httpClient.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofit;

    }

}
