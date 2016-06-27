package com.example.android.groceryproject;

import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Vedant on 6/2/2016.
 */
public class Product {
    private String productName;
    private int productPrice;
    private int productImageId;
    private String productImageUrl;
    private String productSellerUrl;
    private int productSellerId;
    private String sellerCount;
    private boolean favorite;
    private ArrayList<String> ProductImageUrlArray;
    private String SellerName;

    public ArrayList<String> getProductImageUrlArray() {
        return ProductImageUrlArray;
    }

    public void setProductImageUrlArray(ArrayList<String> productImageUrlArray) {
        ProductImageUrlArray = productImageUrlArray;
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



    public Product(){} //default constructor

    /*public Product(String productName, String productPrice, int productImageId,
                   int productSellerId ,String productImageUrl , String productSellerUrl) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageId = productImageId;
        this.productSellerId = productSellerId;
        this.productImageUrl = productImageUrl;
        this.productSellerUrl = productSellerUrl;
    }*/

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

    public int getProductImageId() {
        return productImageId;
    }

    public void setProductImageId(int productImageId) {
        this.productImageId = productImageId;
    }

    public int getProductSellerId() {
        return productSellerId;
    }

    public void setProductSellerId(int productSellerId) { this.productSellerId = productSellerId; }

    public String getProductImageUrl(){ return  productImageUrl; }

    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public String getProductSellerUrl(){ return  productSellerUrl; }

    public void setProductSellerUrl(String productSellerUrl){ this.productSellerUrl = productSellerUrl; }



}
