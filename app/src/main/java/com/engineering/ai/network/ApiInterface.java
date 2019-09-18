package com.engineering.ai.network;

import com.engineering.ai.model.ResponseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("users")
    Call<ResponseData> getUsers(@Query("offset") Integer offset,
                                @Query("limit") Integer limit);


}
