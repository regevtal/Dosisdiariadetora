package com.mycommunityapp.dosisdiariadetora;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SCPlaylistAdapter extends
        RecyclerView.Adapter<SCPlaylistAdapter.MyViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    public static final String TAG = "SCPlaylistAdapter";
    private List<Playlist> mPlayListData = new ArrayList<Playlist>();

    int pos = 0;


    public SCPlaylistAdapter(Context context, List<Playlist> data) {

        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPlayListData = data;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.play_list_item, parent, false);

        // passing the view to the adapter
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mPlayListData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given
    // type to represent an item
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Playlist playlist = mPlayListData.get(position);

       // holder.title.setText(playlist.getTitle());
        // loading album cover using Glide library


        if (pos == 0) {
            holder.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.im_ano_2017));
            pos++;
        } else if (pos == 1) {
            holder.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.im_ano_2016));

            pos++;
        } else if (pos == 2) {
            holder.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.im_ano_2015));

            pos++;
        }


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView thumbnail;

        // getting the view and setting the data
        public MyViewHolder(final View itemView) {
            super(itemView);


            title = (TextView) itemView.findViewById(R.id.title_play_list);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail_play_list);


        }


    }


    public void showPopupMenu(View view) {
        // inflate menu

        PopupMenu popupMenu = new PopupMenu(mContext, view);

        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popupMenu.show();
    }


    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {

                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }

            return true;
        }
    }


    private class FetchItemsTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {


            return null;
        }
    }


}
