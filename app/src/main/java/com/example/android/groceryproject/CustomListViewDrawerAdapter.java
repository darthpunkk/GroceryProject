package com.example.android.groceryproject;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.groceryproject.R;

/**
 * Created by Vedant on 6/1/2016.
 */
public class CustomListViewDrawerAdapter extends ArrayAdapter<String>{
    private String[] itemName;
    private TypedArray iconArray;
    private Context context;
    private LayoutInflater layoutInflater;


    public CustomListViewDrawerAdapter(Context context,String[] itemName,TypedArray iconArray) {
        super(context, R.layout.others_list_items, itemName);
        this.itemName = itemName;
        this.context = context;
        this.iconArray = iconArray;
    }
    public View getView(int position, View view, ViewGroup parent) {
        if(layoutInflater==null)
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view==null)
        view=layoutInflater.inflate(R.layout.others_list_items, null,true);

        TextView textView = (TextView) view.findViewById(R.id.others_list_textview);
        ImageView imageView = (ImageView) view.findViewById(R.id.others_list_icon);

        textView.setText(itemName[position]);
        imageView.setImageResource(iconArray.getResourceId(position,0));

        return view;

    };

}
