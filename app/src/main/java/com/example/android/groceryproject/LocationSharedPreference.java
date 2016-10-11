package com.example.android.groceryproject;

/**
 * Created by Vedant on 28-06-2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;


public class LocationSharedPreference {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LOCATION_PREFERENCE";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CHECKLOC = "locationAvailable";

    public LocationSharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setLocation(String address) {
        editor.putBoolean(KEY_CHECKLOC, true);
        editor.putString(KEY_ADDRESS, address);
        editor.commit();

    }

    public String getLocation() {
        String locationDetails;
        locationDetails=sharedPreferences.getString(KEY_ADDRESS, null);
        return locationDetails;

    }
    public boolean checkLocationAvailable(){
        return  sharedPreferences.getBoolean(KEY_CHECKLOC,false);
    }
}
