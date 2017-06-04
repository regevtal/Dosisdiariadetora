package com.mycommunityapp.dosisdiariadetora;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "SplashScreen";
    /**
     * Id to identify a camera permission request.
     */
    private static final int REQUEST_READ_PHONE_STATE = 0;

    Thread splashTread;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler mHandler = new Handler();
    private int progress;
    private int countList = 0;
    private View mLayout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isAlive = settings.getBoolean(MainScreenFragment.PREFS_NAME, false);
        //Log.i(TAG, "IS Alive " + isAlive);


        progressBar = (ProgressBar) findViewById(R.id.progress_bar_list);

        StartAnimations();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void StartAnimations() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);


        if (!isNetworkAvailable()) {

            Toast.makeText(SplashScreen.this, "No Connection!!!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            getDownloadPlaylist();


        }


    }

    public void getDownloadPlaylist() {

        progress = 0;
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_list);
        progressBar.setMax(500);

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {

                    progressStatus = doSomeWork();


                    mHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                }

                mHandler.post(new Runnable() {
                    public void run() {
                        // ---0 - VISIBLE; 4 - INVISIBLE; 8 - GONE---
                        progressBar.setVisibility(View.GONE);
                        Intent i = new Intent(SplashScreen.this, MainScreenActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }

            private int doSomeWork() {
                try {
                    // ---simulate doing some work---
                    Thread.sleep(50);

                    if (countList == 0) {
                        FetchItemsTask();
                        countList = 1;
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return ++progress;
            }
        }).start();

    }


    private void FetchItemsTask() {

        SCServicePlaylists scService = SoundCloud.getServicePlaylists();
        scService.getPlaylist("user-590009887").enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    //sizeData =response.body().size()
                    PlayListLab.get(SplashScreen.this).addPlayList(response.body());
                    //showMessage(" Code " + response.message());

                } else {

                    showMessage("Error Code " + response.message());

                }

            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                showMessage("Network Error: " + t.getMessage());

            }
        });


    }

    private void showMessage(String message) {
        Toast.makeText(SplashScreen.this, message, Toast.LENGTH_LONG).show();
    }

    //Request Updates at startup
    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SplashScreen Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Requests the Camera permission.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestPhonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Snackbar.make(mLayout, "Msg need here",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ActivityCompat.requestPermissions(getParent(),
                                    new String[]{Manifest.permission.READ_CONTACTS},
                                    REQUEST_READ_PHONE_STATE);
                        }
                    })
                    .show();
        } else {

            // PHONE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    StartAnimations();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
