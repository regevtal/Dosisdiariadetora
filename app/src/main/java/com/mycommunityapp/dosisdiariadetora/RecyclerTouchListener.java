package com.mycommunityapp.dosisdiariadetora;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Regev on 5/31/2016.
 */
public class RecyclerTouchListener implements
        RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private ClickListener clickListener;

    public RecyclerTouchListener(Context context,
                                 final RecyclerView recyclerView,
                                 final ClickListener clickListener) {

        this.clickListener = clickListener;

        // Detects various gestures and events using the supplied Motion
        // Events.
        gestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {

                    // Notified when a tap occurs with the up MotionEvent
                    // that triggered it.
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {

                        return true;

                    }

                    @Override
                    public void onLongPress(MotionEvent e) {

                        View child = recyclerView.findChildViewUnder(
                                e.getX(), e.getY());

                        if (child != null && clickListener != null) {

                            clickListener.onLongClick(child, recyclerView
                                    .getChildAdapterPosition(child));

                        }

                        super.onLongPress(e);
                    }

                });
    }




    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
        // Log.i(TAG, "onInterceptTouchEvent "  + gestureDetector.onTouchEvent(event) + " " + event);

        View child = rv.findChildViewUnder(event.getX(), event.getY());

        if (child != null && clickListener != null
                && gestureDetector.onTouchEvent(event)) {

            // clickListener.onLongClick(child,
            // rv.getChildAdapterPosition(child));
            clickListener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent event) {
        //Log.i(TAG, "onTouchEvent " + event);

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    // create interface for long and inClick
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

}


