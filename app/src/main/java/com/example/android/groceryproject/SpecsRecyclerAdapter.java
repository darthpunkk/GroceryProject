package com.example.android.groceryproject;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by Vedant on 6/2/2016.
 */
public class SpecsRecyclerAdapter extends RecyclerView.Adapter<SpecsRecyclerAdapter.SimpleViewHolder> {

    ArrayList<String>  SpecsName,SpecsDesc;
    private Context mContext;


    SpecsRecyclerAdapter(ArrayList<String> SpecsName, ArrayList<String> SpecsDesc, Context mContext) {
        this.SpecsName = SpecsName;
        this.SpecsDesc = SpecsDesc;
        this.mContext = mContext;

    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_specs, parent, false);
        return new SimpleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {

        holder.SpecsName.setText(SpecsName.get(position));
        holder.SpecsDesc.setText(SpecsDesc.get(position));
        holder.SpecsDesc.post(new Runnable() {
            @Override
            public void run() {
                int lineCount =  holder.SpecsDesc.getLayout().getLineCount();
                int ellipseCount = holder.SpecsDesc.getLayout().getEllipsisCount(lineCount);
                Log.v("Line count: ", lineCount+" "+ellipseCount);
                if(ellipseCount>0){
                    holder.SpecsDesc.setOnClickListener(holder);
                    holder.ExandedIcon.setOnClickListener(holder);
                    holder.ExandedIcon.setBackgroundResource(R.drawable.ic_expand_more);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return SpecsName.size();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView SpecsName;
        TextView SpecsDesc;
        ImageView ExandedIcon;
        boolean isExpanded=false;



        public SimpleViewHolder(View itemView) {
            super(itemView);
            ExandedIcon = (ImageView) itemView.findViewById(R.id.expand_collapse);
            SpecsName = (TextView) itemView.findViewById(R.id.specs_name);
            SpecsDesc = (TextView) itemView.findViewById(R.id.expand_text_view);




        }

        @Override
        public void onClick(View v) {
            isExpanded=!isExpanded;
            if(isExpanded) {
                SpecsDesc.setSingleLine(false);
                SpecsDesc.setEllipsize(null);
                ExandedIcon.setBackgroundResource(R.drawable.ic_expand_less);

            }
            else {
                SpecsDesc.setSingleLine(true);
                SpecsDesc.setEllipsize(TextUtils.TruncateAt.END);
                ExandedIcon.setBackgroundResource(R.drawable.ic_expand_more);
            }

        }
    }
}