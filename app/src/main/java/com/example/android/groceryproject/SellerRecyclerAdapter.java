package com.example.android.groceryproject;

/**
 * Created by Vedant on 19-06-2016.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vedant on 6/9/2016.
 */
public class SellerRecyclerAdapter extends RecyclerView.Adapter<SellerRecyclerAdapter.SellerListViewHolder> {

    List<Seller> sellers;
    private Context mContext;
    ImageLoader imageLoader;
    TextView textView;





    SellerRecyclerAdapter(List<Seller> sellers, Context mContext) {
        this.sellers = sellers;
        this.mContext = mContext;
        ;

    }

    @Override
    public SellerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_sellers, parent, false);

        return new SellerListViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final SellerListViewHolder holder, final int position) {


        imageLoader = SingletonRequestQueue.getInstance(mContext).getImageLoader();
        String URL = sellers.get(position).getSellerLogoUrl();
        holder.sellerLogo.setImageUrl(URL, imageLoader);

            holder.itemCount.setVisibility(View.INVISIBLE);
            holder.removeButton.setVisibility(View.INVISIBLE);


        //holder.itemCount.setText(String.valueOf(count.get(position)));
        //holder.sellerName.setText(sellers.get(position).getSellerName());
        holder.sellerPrice.setText("₹ " + sellers.get(position).getPriceAtSeller());
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    holder.count++;
                    holder.itemCount.setText(String.valueOf(holder.count));
                    holder.itemCount.setVisibility(View.VISIBLE);
                    holder.removeButton.setVisibility(View.VISIBLE);
                    ProductDetailActivity.counter++;
                    Log.i("count","adapter"+ProductDetailActivity.counter);
                    ActivityCompat.invalidateOptionsMenu((ProductDetailActivity)mContext);



            }
        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailActivity.counter--;
                Log.i("count","adapter"+ProductDetailActivity.counter);
                ActivityCompat.invalidateOptionsMenu((ProductDetailActivity)mContext);
                    holder.count--;
                    holder.itemCount.setText(String.valueOf(holder.count));
                    if (holder.count == 0) {
                       holder.itemCount.setVisibility(View.INVISIBLE);
                        holder.removeButton.setVisibility(View.INVISIBLE);}
            }
        });

    }


    @Override
    public int getItemCount() {
        return sellers.size();
    }

    public class SellerListViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView sellerLogo;
        //TextView sellerName;
        TextView sellerPrice;
        TextView itemCount;
        ImageButton addButton, removeButton;
       int count = 0;

        public SellerListViewHolder(View itemView) {
            super(itemView);
            sellerLogo = (NetworkImageView) itemView.findViewById(R.id.seller_logo);
            //sellerName = (TextView) itemView.findViewById(R.id.);
            sellerPrice = (TextView) itemView.findViewById(R.id.price_at_seller);
            addButton = (ImageButton) itemView.findViewById(R.id.add_button);
            removeButton = (ImageButton) itemView.findViewById(R.id.remove_button);
            itemCount = (TextView) itemView.findViewById(R.id.item_count);

        }
    }


}

