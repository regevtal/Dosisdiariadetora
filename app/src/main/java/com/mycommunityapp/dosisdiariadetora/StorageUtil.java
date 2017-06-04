package com.mycommunityapp.dosisdiariadetora;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StorageUtil {

    public static final String TAG = "StorageUtil";

    private final String STORAGE = "com.mycommunityapp.dosisdiariadetora.STORAGE";
    private SharedPreferences preferences;
    private Context context;

    public StorageUtil(Context context) {

        this.context = context;

    }

    public void storeAudio(List<Playlist.TracksBean> audioList) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Log.i(TAG, "Audio list: " + audioList.size());

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(audioList);
        editor.putString("audioList", json);
        editor.apply();
       // Log.i(TAG, "STORE list: " + json);


    }

    public void removeAll(){

        preferences.edit().remove(STORAGE).apply();
    }



    public List<Playlist.TracksBean> loadAudio() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("audioList", null);
       // Log.i(TAG, "loadAudio Audio list: " + json);

        Type type = new TypeToken<List<Playlist.TracksBean>>() {
        }.getType();

        return gson.fromJson(json, type);
    }

    public void storeAudioIndex(int index) {
        Log.i(TAG, "store Audio index: " + index);

        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("audioIndex", index);
        editor.apply();

    }

    public int loadAudioIndex() {

        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
       // Log.i(TAG, "load Audio index: " + preferences.getInt("AudioIndex", -1));

        return preferences.getInt("audioIndex", -1);//return -1 if no data

    }

    public void clearCachedAudioPlaylist() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}


