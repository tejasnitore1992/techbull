package com.techbull.InterviewTask;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TechBullConfig {

    public static final String APIKEY = "af16ecf0";
    private static Retrofit retrofit = null;
    public static Retrofit getClient(Context context) {

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URLS.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
