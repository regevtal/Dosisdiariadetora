package com.mycommunityapp.dosisdiariadetora;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SCTrackAdapter extends
        RecyclerView.Adapter<SCTrackAdapter.MyViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    public static final String TAG = "SCTrackAdapter";

    //List<MenuDrawerData> mDataList = Collections.emptyList();
    private List<Playlist.TracksBean> lectureListData = new ArrayList<Playlist.TracksBean>();
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    //private List<Audio> filteredLectureList;
// Start with first item selected
    private int selectedPos = -1;

    public SCTrackAdapter(Context context, List<Playlist.TracksBean> data) {

        mContext = context;
        // LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lectureListData = data;
        // filteredLectureList = data;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.track_item, parent, false);

        // passing the view to the adapter
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return lectureListData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given
    // type to represent an item
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.itemView.setSelected(selectedPos == position);

        Playlist.TracksBean lecture = lectureListData.get(position);
        holder.titleTextView.setText(lecture.getTitle());

//        String substrDate=lecture.getCreated_at().substring(0,16);
//        holder.createdAtSubTitleView.setText(substrDate);

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(mContext).load(R.drawable.lg_logo_track_list).into(holder.trackImageView);


    }

    public Playlist.TracksBean getItem(int i) {
        return lectureListData.get(i);
    }

    public List<Playlist.TracksBean> getList() {

        return lectureListData;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, createdAtSubTitleView;
        ImageView trackImageView;
        RelativeLayout rl;

        // getting the view and setting the data
        public MyViewHolder(final View itemView) {
            super(itemView);


            rl = (RelativeLayout) itemView.findViewById(R.id.item_relativeLayout);
            titleTextView = (TextView) itemView
                    .findViewById(R.id.lecture_title);

            createdAtSubTitleView = (TextView) itemView.findViewById(R.id.lecture_Added);

            trackImageView = (ImageView) itemView
                    .findViewById(R.id.lecture_photo);


        }


    }


    public String formatDate(String dateString) {




        String date = dateString;
        SimpleDateFormat input = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
        SimpleDateFormat output = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.US);

        try {
            Date newDate =  input.parse(date);

            date = output.format(newDate);


        } catch (ParseException e) {
        Log.i(TAG, "ParseException - dateFormat "+ e.getMessage());
    }
        return date;

    }

}