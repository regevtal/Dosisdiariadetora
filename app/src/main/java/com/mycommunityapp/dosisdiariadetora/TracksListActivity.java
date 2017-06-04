package com.mycommunityapp.dosisdiariadetora;

import android.app.Fragment;

public class TracksListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new TracksListFragment();
    }


}
