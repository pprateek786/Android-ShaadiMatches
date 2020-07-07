package com.shaadi.shaadimatches.communication;

import com.shaadi.shaadimatches.models.ShaadiMatchesResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIEndPointInterface
{
    @GET("api/")
    Call<ShaadiMatchesResult> getUserList(@Query("results") String result);
}
