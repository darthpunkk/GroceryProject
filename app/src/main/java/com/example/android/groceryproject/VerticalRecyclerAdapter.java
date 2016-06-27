package com.example.android.groceryproject;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vedant on 6/9/2016.
 */
public class VerticalRecyclerAdapter extends RecyclerView.Adapter<VerticalRecyclerAdapter.ProductListViewHolder>{

    ArrayList<Product> products;
    private Context mContext;
    ImageLoader imageLoader;
    private clickListenerInterface listener;



    VerticalRecyclerAdapter(ArrayList<Product> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;

    }

    public interface clickListenerInterface{
        void clickView(View view,int position);
        void clickFavourite(MaterialFavoriteButton button,int position,List<Product> products);



    }

    public void setClickListenerInterface(clickListenerInterface listener){
        this.listener = listener;
    }

    public void setProductsList(ArrayList<Product> products){
        this.products = products;
    }

    @Override
    public ProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list, parent, false);

        return new ProductListViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ProductListViewHolder holder, final int position) {


        imageLoader = SingletonRequestQueue.getInstance(mContext).getImageLoader();
        String URL = products.get(position).getProductImageUrl();
        holder.progressBar.setVisibility(View.VISIBLE);
        /* to hide the progress bar after image response */

        imageLoader.get(URL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response != null) {
                    Bitmap bitmap = response.getBitmap();
                    if (bitmap != null) {
                        holder.progressBar.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        holder.itemImage.setImageUrl(URL, imageLoader);
        holder.itemName.setText(products.get(position).getProductName());
        holder.itemPrice.setText("₹ "+products.get(position).getProductPrice());
        holder.itemSellerCount.setText(products.get(position).getSellerCount());
        holder.favoriteButton.setFavorite(products.get(position).isitFavorite(),false);

    }



    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        NetworkImageView itemImage;
        TextView itemSellerCount;
        TextView itemName;
        TextView itemPrice;
        ProgressBar progressBar;
        MaterialFavoriteButton favoriteButton;


        public ProductListViewHolder(View itemView) {
            super(itemView);
            itemImage = (NetworkImageView) itemView.findViewById(R.id.product_list_image);
            itemSellerCount = (TextView) itemView.findViewById(R.id.seller_count);
            itemName = (TextView) itemView.findViewById(R.id.product_list_name);
            itemPrice = (TextView) itemView.findViewById(R.id.product_list_price);
            progressBar = (ProgressBar)itemView.findViewById(R.id.network_list_image_progressbar);
            favoriteButton = (MaterialFavoriteButton)itemView.findViewById(R.id.custom_favorite_button);
            favoriteButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(v instanceof MaterialFavoriteButton){
                listener.clickFavourite((MaterialFavoriteButton)v,getLayoutPosition(),products);
            }
            else {
                listener.clickView(v,getLayoutPosition());
            }


        }


    }


}
