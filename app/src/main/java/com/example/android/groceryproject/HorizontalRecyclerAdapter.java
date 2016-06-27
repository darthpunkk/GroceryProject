package com.example.android.groceryproject;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by Vedant on 6/2/2016.
 */
public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<HorizontalRecyclerAdapter.ProductViewHolder> {

    List<Product> products;
    private Context mContext;
    ImageLoader imageLoader;
    private static clickListenerInterface listener;


    HorizontalRecyclerAdapter(List<Product> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;

    }
    public interface clickListenerInterface{
        void onItemClick(View v,int position);

    }
    public void setClickListenerInterface(clickListenerInterface listener) {
        this.listener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {

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
        holder.sellerLogo.setImageResource(products.get(position).getProductSellerId());

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        NetworkImageView itemImage;
        NetworkImageView sellerLogo;
        TextView itemName;
        TextView itemPrice;
        ProgressBar progressBar;


        public ProductViewHolder(View itemView) {
            super(itemView);
            itemImage = (NetworkImageView) itemView.findViewById(R.id.product_image);
            sellerLogo = (NetworkImageView) itemView.findViewById(R.id.product_seller);
            itemName = (TextView) itemView.findViewById(R.id.product_name);
            itemPrice = (TextView) itemView.findViewById(R.id.product_price);
            progressBar = (ProgressBar)itemView.findViewById(R.id.network_image_progressbar);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v,getLayoutPosition());
        }
    }
}