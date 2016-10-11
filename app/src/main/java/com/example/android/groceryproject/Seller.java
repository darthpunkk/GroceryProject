package com.example.android.groceryproject;

import java.util.ArrayList;

/**
 * Created by Vedant on 17-06-2016.
 */
public class Seller {
    private String SellerName;
    private String SellerLogoUrl;
    private int PriceAtSeller;
    private String quantity;
    ArrayList<String> QuantityArrayList;
    ArrayList<Integer> PriceArrayList, CountArrayList;

    public ArrayList<String> getQuantityArrayList() {
        return QuantityArrayList;
    }

    public void setArrayLists(ArrayList<String> quantityArrayList, ArrayList<Integer> countArrayList
            , ArrayList<Integer> priceArrayList) {
        setQuantityArrayList(quantityArrayList);
        setPriceArrayList(priceArrayList);
        setCountArrayList(countArrayList);
    }

    public void setQuantityArrayList(ArrayList<String> quantityArrayList) {
        QuantityArrayList = quantityArrayList;
    }

    public ArrayList<Integer> getCountArrayList() {
        return CountArrayList;
    }

    public void setCountArrayList(ArrayList<Integer> countArrayList) {
        CountArrayList = countArrayList;
    }

    public ArrayList<Integer> getPriceArrayList() {
        return PriceArrayList;
    }

    public void setPriceArrayList(ArrayList<Integer> priceArrayList) {
        PriceArrayList = priceArrayList;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public int getPriceAtSeller() {
        return PriceAtSeller;
    }

    public void setPriceAtSeller(int priceAtSeller) {
        PriceAtSeller = priceAtSeller;
    }

    public String getSellerLogoUrl() {
        return SellerLogoUrl;
    }

    public void setSellerLogoUrl(String sellerLogoUrl) {
        SellerLogoUrl = sellerLogoUrl;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }


}

