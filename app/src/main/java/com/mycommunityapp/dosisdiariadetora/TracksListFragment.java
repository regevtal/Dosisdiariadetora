package com.mycommunityapp.dosisdiariadetora;


import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TracksListFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    public static final String Broadcast_PLAY_NEW_AUDIO = "com.mycommunityapp.dosisdiariadetora.PlayNewAudio";
    public static final String TAG = "TracksListFragment";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerViewLecture;
    private SCTrackAdapter mSCTrackAdapter;
    private MediaPlayerService playerService;
    boolean serviceBound = false;
    private LinearLayout mediaCont;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    private Utilities utils;
    private ImageView btnPlay, mCollapsingTrackImage, mSelectedTrackImage;
    private TextView mSelectedTrackTitle;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    private List<Playlist.TracksBean> filteredList;
    private AppCompatActivity activity;
    private Playlist mPlayList;
    private List<Playlist.TracksBean> tracks = new ArrayList<Playlist.TracksBean>();
    private List<Playlist> listFromSC = new ArrayList<Playlist>();
    int mNum;


    public static TracksListFragment newInstance(int numd) {
        TracksListFragment myFragment = new TracksListFragment();

        Bundle args = new Bundle();
        args.putInt("num", numd);
        myFragment.setArguments(args);

        return myFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        utils = new Utilities();
        setRetainInstance(false);
        setHasOptionsMenu(true);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        mPlayList = PlayListLab.get(getActivity()).getPlayList(mNum);
        tracks = mPlayList.getTracks();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tracks_list, parent, false);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar_list);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();

                try {
                    getActivity().unbindService(serviceConnection);
                    //service is active
                    playerService.stopSelf();
                    playerService = null;
                } catch (IllegalArgumentException e) {

                    //showMessage(e.getMessage());

                    //Log.i(TAG, e.getMessage());
                }


            }
        });

        btnPlay = (ImageView) v.findViewById(R.id.player_control);
        // btnPlay.setImageResource(R.drawable.ic_pause_white_24dp);


        //mSelectedTrackTitle = (TextView) v.findViewById(R.id.selected_track_title);

        mSelectedTrackTitle = (TextView) v.findViewById(R.id.selected_track_title);
        mediaCont = (LinearLayout) v.findViewById(R.id.media_control_id);
        songProgressBar = (SeekBar) v.findViewById(R.id.songProgressBar);
        songCurrentDurationLabel = (TextView) v.findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) v.findViewById(R.id.songTotalDurationLabel);


        songProgressBar.setOnSeekBarChangeListener(this);


        View rec = v.findViewById(R.id.recyclerview_track);

        if (!serviceBound) {
            mediaCont.setVisibility(View.GONE);


        }


        mRecyclerViewLecture = (RecyclerView) rec.findViewById(R.id.recyclerview_id);
        mRecyclerViewLecture.setLayoutManager(new LinearLayoutManager(
                getActivity()));
        mRecyclerViewLecture.setHasFixedSize(true);


        mRecyclerViewLecture.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                mRecyclerViewLecture, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                //Playlist.TracksBean tr = tracks.get(position);


                // playAudio(position);
                handelListItemClick(position);
                updateProgressBar();
            }

            @Override
            public void onLongClick(View view, int position) {
                // Toast.makeText(getActivity(), "on Long Click", Toast.LENGTH_LONG).show();

            }
        }));


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePlayPause();
                //playAudio();
                // Toast.makeText(getActivity(), "btnPlay Click", Toast.LENGTH_LONG).show();

            }
        });

        loadList(tracks);
        return v;

    }//end view


    public void loadList(List<Playlist.TracksBean> t) {


        mSCTrackAdapter = new SCTrackAdapter(getActivity(), t);
        mRecyclerViewLecture.setAdapter(mSCTrackAdapter);
    }





    private void handelListItemClick(int position) {


        mediaCont.setVisibility(View.VISIBLE);
        Playlist.TracksBean track = tracks.get(position);
        btnPlay.setImageResource(R.drawable.ic_pause_white_24dp);


        mSelectedTrackTitle.setText(track.getTitle());


        playAudio(position);


        // getActivity().overridePendingTransition(R.anim.cla, R.anim.stay_in);
    }

    private void togglePlayPause() {


        if (playerService.isPlaying()) {
            playerService.pauseMedia();

            // playerService.buildNotification(PlaybackStatus.PAUSED);

            btnPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        } else {
            playerService.playMedia();
            //playerService.buildNotification(PlaybackStatus.PLAYING);
            btnPlay.setImageResource(R.drawable.ic_pause_white_24dp);
        }


    }


    private void playAudio(int audioIndex) {


        //Check is service is active
        if (!serviceBound) {
            StorageUtil storage = new StorageUtil(getActivity());
            //Store Serializable audioList to SharedPreferences
            storage.storeAudio(tracks);
            storage.storeAudioIndex(audioIndex);
            Intent playerIntent = new Intent(getActivity(), MediaPlayerService.class);
            //playerIntent.putExtra("media", lectureList.get(audioIndex).getStreamURL());
           // Log.i( TAG, playerIntent.getExtras());
            getActivity().startService(playerIntent);
            getActivity().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Service is active
            //Send media with BroadCastReciver
            StorageUtil storage = new StorageUtil(getActivity());
            storage.storeAudioIndex(audioIndex);
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            getActivity().sendBroadcast(broadcastIntent);


        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);




    }



    public void updateBtnPlay() {
        if (!playerService.isPlaying()) {
            btnPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);


        } else {

            btnPlay.setImageResource(R.drawable.ic_pause_white_24dp);

        }

    }





    /**
     * When user starts moving the progress handler
     */

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);

    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {


        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = playerService.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        playerService.seekTo(currentPosition);

        updateProgressBar();

    }


    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    //Update Media when pause
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {


    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            // set Progress bar values
            mHandler.postDelayed(new Runnable() {

                public void run() {

                    try {

                        if (serviceBound) {
                            songProgressBar.setProgress(0);
                            songProgressBar.setMax(100);

                        }

                    } finally {


                        if (playerService != null) {

                            long totalDuration = playerService.getDuration();
                            long currentDuration = playerService.getCurrentPosition();
                            // Displaying Total Duration time
                            songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
                            // Displaying time completed playing
                            songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));
                            // Updating progress bar
                            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                            //Log.d("Progress", ""+progress);
                            songProgressBar.setProgress(progress);
                            mHandler.postDelayed(this,100);
                        }


                    }


                }
            }, 200);

        }
    };



    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            //We've bound to LocalService , cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            playerService = binder.getService();
            serviceBound = true;

            //Toast.makeText(getActivity(), "Service Bound", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            serviceBound = false;
        }
    };


    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();


        if (serviceBound) {
            Playlist.TracksBean a = playerService.getAudio();
            if (playerService.isPlaying()) {

                mSelectedTrackTitle.setText(a.getTitle());

                btnPlay.setImageResource(R.drawable.ic_pause_white_24dp);
            } else {

                mSelectedTrackTitle.setText(a.getTitle());
                btnPlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);

            }
            updateBtnPlay();
            updateProgressBar();

            //playerService.removeNotification();
        }


    }


    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mUpdateTimeTask);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            getActivity().unbindService(serviceConnection);
            //service is active
            playerService.stopSelf();
            playerService = null;
        } catch (IllegalArgumentException e) {

            //showMessage(e.getMessage());

            //Log.i(TAG, e.getMessage());
        }


    }


}
