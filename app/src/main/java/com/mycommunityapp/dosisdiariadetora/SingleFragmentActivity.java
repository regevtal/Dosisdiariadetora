package com.mycommunityapp.dosisdiariadetora;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();
    public static final String TAG = "SingleFragmentActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ///Log.i(TAG,"DESTROY");



    }

    @Override
    public void onPause(){
        super.onPause();


        //Log.i(TAG,"PAUSE");



    }
}

// class that take a fragment and drop it in fragmentContainer