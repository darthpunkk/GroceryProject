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
public class FilterCategoryAdapter extends BaseAdapter {

    List<String> List;
    private Context mContext;
    int selectPosition ;

    public FilterCategoryAdapter(List<String> List, Context mContext,int selectPosition){
        this.List = List;
        this.mContext = mContext;
        this.selectPosition = selectPosition;


    }
    public  void setSelectPosition(int selectPosition){
        this.selectPosition = selectPosition;

    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int position) {
        return List.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_textview,parent,false);
            holder.categoryName = (TextView) convertView.findViewById(R.id.custom_text);
            convertView.setTag(holder);
        }
        else
            holder = (GridViewHolder) convertView.getTag();

        holder.categoryName.setText(List.get(position));
        if(position==selectPosition){
            convertView.setBackgroundResource(R.color.white);
        }
        else {
            convertView.setBackgroundResource(R.color.grey);
        }

        return convertView;
    }
    private static class GridViewHolder{

        //ImageView categoryIcon;
        TextView categoryName;
        //TextView price;

    }

}
