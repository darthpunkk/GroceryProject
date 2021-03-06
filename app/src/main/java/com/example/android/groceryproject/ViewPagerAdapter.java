package com.example.android.groceryproject;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.android.groceryproject.R;
import com.example.android.groceryproject.SingletonRequestQueue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Vedant on 18-06-2016.
 */
public class ViewPagerAdapter extends PagerAdapter{

    ArrayList<String> ImageArray;
    private Context mContext;
    ImageView ImageView;

    public ViewPagerAdapter(ArrayList<String> imageArray,Context mContext) {
        this.mContext = mContext;
        ImageArray = imageArray;
    }

    @Override
    public int getCount() {
        return (ImageArray.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.imageview,container,false);
        ImageView = (ImageView)viewGroup.findViewById(R.id.image);
        Picasso.with(mContext).load(ImageArray.get(position)).into(ImageView);
        container.addView(viewGroup);
        return viewGroup;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
