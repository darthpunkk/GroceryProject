package com.example.android.groceryproject;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vedant on 6/9/2016.
 */
public class VerticalRecyclerAdapter extends RecyclerView.Adapter<VerticalRecyclerAdapter.ProductListViewHolder>{

    ArrayList<Product> products;
    private Context mContext;
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


        Picasso.with(mContext).load(products.get(position).getProductImageUrl()).into(holder.itemImage);
        holder.itemName.setText(products.get(position).getProductName());
        //
        holder.itemSellerCount.setText(products.get(position).getSellerCount());
        holder.favoriteButton.setFavorite(products.get(position).isitFavorite(),false);
        final ArrayList<String> arrayList = new ArrayList<>(products.get(position).getProductQuantity().values());
        final ArrayList<Integer> arrayList1 = new ArrayList<>(products.get(position).getProductQuantity().keySet());

        if(arrayList.size()>1) {

            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,R.layout.quantity_textview, arrayList);
            products.get(position).setProductPrice(arrayList1.get(0));
            holder.itemPrice.setText("₹ " + arrayList1.get(0));
            holder.PriceQuantitySpinner.setAdapter(adapter);
            holder.PriceQuantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    products.get(position).setProductPrice(arrayList1.get(pos));
                    holder.itemPrice.setText("₹ " + arrayList1.get(pos));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else {
            holder.relativeLayout.setVisibility(View.INVISIBLE);
            holder.itemQuantity.setVisibility(View.VISIBLE);
            holder.itemQuantity.setText(arrayList.get(0));
            products.get(position).setProductPrice(arrayList1.get(0));
            holder.itemPrice.setText("₹ "+arrayList1.get(0));
        }

    }



    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView itemImage;
        TextView itemSellerCount;
        TextView itemName;
        TextView itemPrice,itemQuantity;
        MaterialFavoriteButton favoriteButton;
        Spinner PriceQuantitySpinner;
        RelativeLayout relativeLayout;

        public ProductListViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.product_list_image);
            itemSellerCount = (TextView) itemView.findViewById(R.id.seller_count);
            itemName = (TextView) itemView.findViewById(R.id.product_list_name);
            PriceQuantitySpinner = (Spinner) itemView.findViewById(R.id.price_quantity_spinner);
            itemQuantity = (TextView) itemView.findViewById(R.id.product_quantity);
            itemPrice = (TextView) itemView.findViewById(R.id.product_list_price);
            favoriteButton = (MaterialFavoriteButton)itemView.findViewById(R.id.custom_favorite_button);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.spinner_layout);
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
