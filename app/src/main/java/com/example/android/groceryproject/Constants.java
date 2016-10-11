package com.example.android.groceryproject;

/**
 * Created by Vedant on 15-07-2016.
 */
public class Constants {
    private static Constants mInstance = null;

    public static final String ip = "http://122.162.158.31:8080/";
    //public String ip ="http://192.168.54.130:8080/";

    public Constants() {
    }

    public static synchronized Constants getInstance() {
        if(mInstance == null)
        mInstance = new Constants();

        return mInstance;
    }
}
