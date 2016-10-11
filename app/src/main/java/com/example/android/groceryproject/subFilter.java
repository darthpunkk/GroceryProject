package com.example.android.groceryproject;

import java.io.Serializable;

/**
 * Created by Vedant on 18-07-2016.
 */
public class subFilter implements Serializable{

    private String subFilterName;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getSubFilterName() {
        return subFilterName;
    }

    public void setSubFilterName(String subFilterName) {
        this.subFilterName = subFilterName;
    }
}
