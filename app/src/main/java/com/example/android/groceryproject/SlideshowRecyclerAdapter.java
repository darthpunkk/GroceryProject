package com.example.android.groceryproject;

/**
 * Created by Vedant on 22-06-2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vedant on 6/2/2016.
 */
public class SlideshowRecyclerAdapter extends RecyclerView.Adapter<SlideshowRecyclerAdapter.ImageViewHolder> {

    List<String> Images;
    private Context mContext;
    ImageLoader imageLoader;
    String URL;

    public SlideshowRecyclerAdapter(List<String> images, Context mContext) {
        Images = images;
        this.mContext = mContext;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.slideshow, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        Picasso.with(mContext).load(Images.get(position)).into(holder.itemImage);
        //imageLoader = SingletonRequestQueue.getInstance(mContext).getImageLoader();
        //String URL =Images.get(position);
        //holder.itemImage.setImageUrl(URL, imageLoader);

    }

    @Override
    public int getItemCount() {
        return Images.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         ImageView itemImage;


        public ImageViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.slideshow_image);
            //itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
        }
    }
}
