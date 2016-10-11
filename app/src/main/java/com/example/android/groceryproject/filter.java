package com.example.android.groceryproject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Vedant on 18-07-2016.
 */
public class Filter  implements Serializable{
    private String FilterCategory;
    private ArrayList<subFilter> FilterSubCategory;
    private int count;

    public String getFilterCategory() {
        return FilterCategory;
    }

    public void setFilterCategory(String filterCategory) {
        FilterCategory = filterCategory;
    }

    public ArrayList<subFilter> getFilterSubCategory() {
        return FilterSubCategory;
    }

    public void setFilterSubCategory(ArrayList<subFilter> filterSubCategory) {
        FilterSubCategory = filterSubCategory;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
