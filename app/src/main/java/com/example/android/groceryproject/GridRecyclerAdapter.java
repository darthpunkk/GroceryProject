package com.example.android.groceryproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vedant on 6/2/2016.
 */
public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.CategoryViewHolder> {

    List<Category> categories;
    private Context mContext;
    private int MaxItems=6;
    private static clickListenerInterface listener;

    GridRecyclerAdapter(List<Category> categories, Context mContext) {
        this.categories =categories;
        this.mContext = mContext;

    }

    public interface clickListenerInterface{
        void onItemClick(View v,int position);

    }
    public void setClickListenerInterface(clickListenerInterface listener) {
        this.listener = listener;
    }

    public void setMaxItems(int flag){

        if(flag==0){
            MaxItems = categories.size();
        }
        else
        {
            MaxItems = 6;
        }

    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {
        Picasso.with(mContext).load(categories.get(position).getUrl()).into(holder.categoryIcon);
        holder.categoryName.setText(categories.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return MaxItems;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

       ImageView categoryIcon;
        TextView categoryName;


        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryIcon = (ImageView) itemView.findViewById(R.id.category_icon);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v,getLayoutPosition());
        }
    }

}