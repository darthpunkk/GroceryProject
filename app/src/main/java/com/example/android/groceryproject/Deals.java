package com.example.android.groceryproject;

/**
 * Created by Vedant on 14-07-2016.
 */
public class Deals {
    private String Url;
    private String Name;
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
