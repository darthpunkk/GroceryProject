package com.example.android.groceryproject;

/**
 * Created by Vedant on 17-06-2016.
 */
public class Seller {

    private String SellerName;
    private String SellerLogoUrl;
    private int PriceAtSeller;

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
