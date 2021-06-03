package com.techbull.InterviewTask;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TechBullApi {


    @GET(".")
    Call<JsonElement> searchMovieShowName(@Query("s") String searchText, @Query("apikey") String apikey, @Query("page") String pageNo);
}
