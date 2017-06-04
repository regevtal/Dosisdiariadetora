package com.mycommunityapp.dosisdiariadetora;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.claucookie.miniequalizerlibrary.EqualizerView;

public class MainScreenFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static final String TAG = "MainScreenFragment";
    public static final String PREFS_NAME = "appStillAlive";


    private Toolbar mToolbar;
    private AppCompatActivity activity;
    private List<Playlist> listFromSC;
    private int progressStatus = 0;
    private Handler mHandler = new Handler();
    private TextView mTrackTitle, mTrackDateTxv,donatTxv;
    private EqualizerView mEqualizerView;
    private ImageView mPlayRecent, mImg2017, mImg2016, mImg2015;
    private MediaPlayer mp;
    private RecyclerView mRecyclerViewPlayList;
    private SCPlaylistAdapter mAdapterPlayList;
    private SeekBar trackProgressBar;
    private TextView trackCurrentDurationLabel;
    private TextView trackTotalDurationLabel;
    private Utilities utils;
    private SharedPreferences prefs;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(false);
        setHasOptionsMenu(false);
        utils = new Utilities();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        listFromSC = PlayListLab.get(getActivity()).getListPlayList();
        //new FetchItemsTask().execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main_screen, parent, false);


        mToolbar = (Toolbar) v.findViewById(R.id.toolbar_maim_screen);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mTrackTitle = (TextView) v.findViewById(R.id.lecture_title_recent);
        mTrackDateTxv = (TextView) v.findViewById(R.id.track_date_recent);
        mPlayRecent = (ImageView) v.findViewById(R.id.play_recent);
        mPlayRecent.setOnClickListener(this);
        mImg2017 = (ImageView) v.findViewById(R.id.image2017);
        mImg2017.setOnClickListener(this);
        mImg2016 = (ImageView) v.findViewById(R.id.image2016);
        mImg2016.setOnClickListener(this);
        mImg2015 = (ImageView) v.findViewById(R.id.image2015);
        mImg2015.setOnClickListener(this);
        donatTxv = (TextView) v.findViewById(R.id.textDonatBtn);
        donatTxv.setOnClickListener(this);
        //getDownloadPlaylist(v);
        trackProgressBar = (SeekBar) v.findViewById(R.id.songProgressBar_main_screen);
        trackCurrentDurationLabel = (TextView) v.findViewById(R.id.songCurrentDurationLabel_main_screen);
        trackTotalDurationLabel = (TextView) v.findViewById(R.id.songTotalDurationLabel_main_screen);
        trackProgressBar.setOnSeekBarChangeListener(this);

        mEqualizerView = (EqualizerView) v.findViewById(R.id.equalizer_view);


        mTrackTitle.setText(listFromSC.get(0).getTracks().get(0).getTitle());
        String substrDate = listFromSC.get(0).getTracks().get(0).getCreated_at().substring(0, 16);
        mTrackDateTxv.setText(substrDate);




        return v;
    }


    public List<Playlist> getData() {


        return listFromSC;

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.play_recent:
                mPlayRecent.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_pause_black_24dp));
                if (mp == null) {
                    mPlayRecent.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_pause_black_24dp));
                    mp = new MediaPlayer();
                    playTrack(listFromSC.get(0).getTracks().get(0).getStream_url());
                    //mp.start();
                }

                if (!mp.isPlaying()) {
                    mPlayRecent.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_pause_black_24dp));
                    mEqualizerView.animateBars();
                    mp.start();
                    updateProgressBar();


                } else {
                    mp.pause();
                    mPlayRecent.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_play_arrow_black_24dp));
                    mEqualizerView.stopBars();

                }
                break;
            case R.id.image2017:
                selectedPlaylist(0);
                break;
            case R.id.image2016:
                selectedPlaylist(1);
                break;
            case R.id.image2015:
                selectedPlaylist(2);
                break;
            case R.id.textDonatBtn:
                String url = "http://www.dosisdiariadetora.com/dona-una-dosis";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;


        }


    }

    public void selectedPlaylist(int pos) {
        if (mp != null) {

            mHandler.removeCallbacks(mUpdateTimeTask);
            mp.stop();
            mp.release();
            mp = null;

        }
        Playlist p = listFromSC.get(pos);

        // Instantiate a new fragment.

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment newFragment = TracksListFragment.newInstance(p.getId());
        ft.replace(R.id.fragmentContainer, newFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void playTrack(String strUrl) {

        try {

            //Toast.makeText(getActivity(), "Play " + strUrl, Toast.LENGTH_SHORT).show();
            mp.reset();
            mp.setDataSource(strUrl + "?client_id=" + Config.CLIENT_ID);
            mp.prepare();

            //mp.start();
            // Setup listener so next song starts automatically
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mediaPlayer) {
                    mPlayRecent.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_play_arrow_black_24dp));
                    mEqualizerView.stopBars();
                }

            });
        } catch (Exception e) {
            Log.e(TAG, "ERROR ", e);

        }
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREFS_NAME, true);
        // Commit the edits!
        editor.apply();
        //Log.i(TAG,"PAUSE");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //getActivity().stopService(playIntent);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        // Commit the edits!
        editor.apply();
        // Log.i(TAG,"DESTROY");


    }

    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if (mp != null) {

            mHandler.removeCallbacks(mUpdateTimeTask);
            int totalDuration = mp.getDuration();
            int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

            mp.seekTo(currentPosition);

            updateProgressBar();
        }


    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            // set Progress bar values
            mHandler.postDelayed(new Runnable() {

                public void run() {

                    if (mp != null) {
                        if (mp.isPlaying()) {
                            try {


                                trackProgressBar.setProgress(0);
                                trackProgressBar.setMax(100);


                            } finally {
                                if (mp != null) {


                                    long totalDuration = mp.getDuration();
                                    long currentDuration = mp.getCurrentPosition();
                                    // Displaying Total Duration time
                                    trackTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
                                    // Displaying time completed playing
                                    trackCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));
                                    // Updating progress bar
                                    int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                                    //Log.d("Progress", ""+progress);
                                    trackProgressBar.setProgress(progress);
                                    mHandler.postDelayed(this, 100);

                                }


                            }
                        }

                    }


                }
            }, 200);

        }
    };


}
