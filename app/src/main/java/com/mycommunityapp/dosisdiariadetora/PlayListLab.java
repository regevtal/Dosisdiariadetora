package com.mycommunityapp.dosisdiariadetora;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class PlayListLab {

    private static final String TAG = " PlayListLab";

    private List<Playlist> mListPlayList;

    private static PlayListLab sPlayListLab;
    private Context mAppContext;

    private PlayListLab(Context appContext) {
        mAppContext = appContext;

        mListPlayList = new ArrayList<Playlist>();

    }

    public static PlayListLab get(Context c) {
        if (sPlayListLab == null) {
            sPlayListLab = new PlayListLab(c.getApplicationContext());
        }
        return sPlayListLab;
    }

    public List<Playlist> getListPlayList() {
        return mListPlayList;
    }


    public Playlist getPlayList(int id) {
        for (Playlist p : mListPlayList) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public void addPlayList(List<Playlist> p) {

        mListPlayList.addAll(p);
    }

}
