package com.mycommunityapp.dosisdiariadetora;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SCService {


    @GET("/tracks?filter=public&limit=3&client_id=" + Config.CLIENT_ID)
    Call<List<Audio>> getUserTracks(@Query("user_id") String date);
}
