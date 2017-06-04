package com.mycommunityapp.dosisdiariadetora;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SoundCloud {


    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(Config.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final SCService SERVICE = RETROFIT.create(SCService.class);
    private static final SCServicePlaylists SERVICE_PLAYLISTS = RETROFIT.create(SCServicePlaylists.class);


    public static SCService getService() {
        return SERVICE;
    }

    public static SCServicePlaylists getServicePlaylists() {
        return SERVICE_PLAYLISTS;
    }
}
