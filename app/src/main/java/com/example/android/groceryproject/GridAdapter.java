package com.example.android.groceryproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.groceryproject.Category;
import com.example.android.groceryproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vedant on 14-07-2016.
 */
public class GridAdapter extends BaseAdapter {

    List<Deals> dealsList;
    private Context mContext;

    public GridAdapter(List<Deals> dealsList, Context mContext){
        this.dealsList = dealsList;
        this.mContext = mContext;

    }
    @Override
    public int getCount() {
        return dealsList.size();
    }

    @Override
    public Object getItem(int position) {
        return dealsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GridViewHolder holder;

        if(convertView == null){
            holder = new GridViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.deals,parent,false);
            holder.categoryIcon = (ImageView) convertView.findViewById(R.id.deals_icon);
            holder.categoryName = (TextView) convertView.findViewById(R.id.deals_name);
            convertView.setTag(holder);
        }
        else
       holder = (GridViewHolder) convertView.getTag();

        Picasso.with(mContext).load(dealsList.get(position).getUrl()).into(holder.categoryIcon);
        holder.categoryName.setText(dealsList.get(position).getName());


        return convertView;
    }
    private static class GridViewHolder{

        ImageView categoryIcon;
        TextView categoryName;
        TextView price;

    }
}
