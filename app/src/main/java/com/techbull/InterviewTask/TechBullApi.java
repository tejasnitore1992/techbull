package com.techbull.InterviewTask;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TechBullApi {

    @POST("apikey.aspx")
    Call<JsonElement> getApiKey();

    @POST("?s={searchText}&apikey={apikey}")
    Call<JsonElement> searchMovieShowName(@Path("searchText") String searchText, @Path("apikey") String apikey);
}
