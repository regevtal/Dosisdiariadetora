package com.mycommunityapp.dosisdiariadetora;


import com.google.gson.annotations.SerializedName;

public class Audio {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private int mID;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;


    public String getTitle() {
        return mTitle;
    }


    public int getID() {
        return mID;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public String getCreatedAt() {
        return createdAt;
    }


    public String getArtworkURL() {
        return mArtworkURL;
    }







    }
