package com.example.android.groceryproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Vedant on 6/2/2016.
 */
public class RecyclerOnItemClickListener implements RecyclerView.OnItemTouchListener {

    public interface onItemClickListener {

        void onItemClick(View view, int position);
    }

    GestureDetector gestureDetector;
    private onItemClickListener listener;

    public RecyclerOnItemClickListener(Context context, onItemClickListener listener) {
        this.listener = listener;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View ChildView = rv.findChildViewUnder(e.getX(), e.getY());
        if (ChildView != null && listener != null && gestureDetector.onTouchEvent(e)) {
            listener.onItemClick(ChildView, rv.getChildAdapterPosition(ChildView));

            return false;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
}
