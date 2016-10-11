package com.example.android.groceryproject;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Vedant on 06-07-2016.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view)%2==0) {
            outRect.left = space;
            outRect.right = 0;
        } else {
            outRect.left =0;
            outRect.right = space;
        }
    }
}