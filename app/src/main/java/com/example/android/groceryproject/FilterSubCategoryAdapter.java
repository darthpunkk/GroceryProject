package com.example.android.groceryproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.Filter;

import com.example.android.groceryproject.Category;
import com.example.android.groceryproject.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vedant on 14-07-2016.
 */
public class FilterSubCategoryAdapter extends BaseAdapter implements Filterable {



   List<subFilter> FilterList;
    List<subFilter> OriginalList;

    private Context mContext;
    private Filter itemFilter ;

    public FilterSubCategoryAdapter(List<subFilter> FilterList, Context mContext){
        this.FilterList = FilterList;
        this.OriginalList = FilterList;
        this.mContext = mContext;


    }

    @Override
    public int getCount() {
        return FilterList.size();
    }

    @Override
    public Object getItem(int position) {
        return FilterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final GridViewHolder holder;

        if(convertView == null){
            holder = new GridViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.checkedtextview,parent,false);
            holder.subCategoryName = (CheckedTextView) convertView.findViewById(R.id.checked_text);
            convertView.setTag(holder);
        }
        else
            holder = (GridViewHolder) convertView.getTag();

        holder.subCategoryName.setText(FilterList.get(position).getSubFilterName());
        holder.subCategoryName.setChecked(FilterList.get(position).isChecked());

        holder.subCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.subCategoryName.isChecked()==true)
                {
                    holder.subCategoryName.setChecked(false);
                    FilterList.get(position).setChecked(false);
                }
                else{
                    holder.subCategoryName.setChecked(true);
                    FilterList.get(position).setChecked(true);
                }


            }
        });

        return convertView;
    }

    public void resetData(){
        FilterList = OriginalList;
    }
    @Override
    public Filter getFilter() {
        if(itemFilter == null)
            itemFilter = new ItemFilter();

        return itemFilter;
    }

    private static class GridViewHolder{

        CheckedTextView subCategoryName;


    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();


            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = OriginalList;
                results.count = OriginalList.size();
            }
            else {
                // We perform filtering operation
                List<subFilter> nList = new ArrayList<>();

                for (subFilter sFilter : FilterList) {
                    if (sFilter.getSubFilterName().toUpperCase().startsWith(constraint.toString().toUpperCase())){
                        nList.add(sFilter);
                    }

                }

                results.values = nList;
                results.count = nList.size();

            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                FilterList = (ArrayList<subFilter>) results.values;
                notifyDataSetChanged();
            }
        }
    }

}
