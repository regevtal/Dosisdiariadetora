package com.mycommunityapp.dosisdiariadetora;

import android.app.Fragment;



public class MainScreenActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new MainScreenFragment();
    }
}
