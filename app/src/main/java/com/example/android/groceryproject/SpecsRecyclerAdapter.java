package com.example.android.groceryproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

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


    }

    @Override
    public int getItemCount() {
        return SpecsName.size();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        TextView SpecsName;
       ExpandableTextView SpecsDesc;


        public SimpleViewHolder(View itemView) {
            super(itemView);

            SpecsName = (TextView) itemView.findViewById(R.id.specs_name);
            SpecsDesc = (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);



        }
    }
}