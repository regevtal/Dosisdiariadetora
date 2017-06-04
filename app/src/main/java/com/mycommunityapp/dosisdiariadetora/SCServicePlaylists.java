package com.mycommunityapp.dosisdiariadetora;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SCServicePlaylists {

    @GET("/playlists?filter=public&limit=3&client_id=" + Config.CLIENT_ID)
    Call<List<Playlist>> getPlaylist(@Query("user_id") String date);

}
