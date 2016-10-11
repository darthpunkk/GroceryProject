package com.example.android.groceryproject;

import java.util.LinkedHashMap;

/**
 * Created by Vedant on 6/2/2016.
 */
public class Product {

    private String productName;
    private int productPrice;
    private String productImageUrl;

    private String sellerCount;
    private boolean favorite;
    private String SellerName;
    private String  itemQuantity;

    private int itemCount;

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    private LinkedHashMap<Integer, String> productQuantity;

    public Product() {}


    public LinkedHashMap<Integer, String> getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(LinkedHashMap<Integer, String> productQuantity) {
        this.productQuantity = productQuantity;
    }


    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }


    public boolean isitFavorite() {
        return favorite;
    }

    public void setitFavorite(boolean favorite) {
        this.favorite = favorite;
    }


    public String getSellerCount() {
        return sellerCount;
    }

    public void setSellerCount(String sellerCount) {
        this.sellerCount = sellerCount;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }


    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
}


