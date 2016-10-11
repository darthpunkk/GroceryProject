package com.example.android.groceryproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vedant on 6/9/2016.
 */
public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.CartListViewHolder> {

    ArrayList<Product> products;
    private Context mContext;
    private final String ADD_TO_CART_URL = Constants.getInstance().ip + "addToCart.php";

    ResultCodeInterface codeInterface;

    public void setCodeInterface(ResultCodeInterface codeInterface) {
        this.codeInterface = codeInterface;
    }


    interface ResultCodeInterface {
        void setResultCode();
    }

    CartRecyclerAdapter(ArrayList<Product> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;

    }

    @Override
    public CartListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_list, parent, false);

        return new CartListViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final CartListViewHolder holder, final int position) {


        Picasso.with(mContext).load(products.get(position).getProductImageUrl()).into(holder.cartItemImage);
        holder.cartItemName.setText(products.get(position).getProductName());
        holder.cartItemPrice.setText(mContext.getString(R.string.currency) + " " + products.get(position).getProductPrice());
        holder.cartItemSeller.setText(products.get(position).getSellerName());
        holder.itemCount.setText(String.valueOf(products.get(position).getItemCount()));
        holder.cartItemQuantity.setText(products.get(position).getItemQuantity());


        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeVolleyRequest(holder, products.get(position).getItemCount(), holder.flag = 0, position);
            }

        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeVolleyRequest(holder, products.get(position).getItemCount(), holder.flag = 1, position);
            }

        });

    }

    private void makeVolleyRequest(final CartListViewHolder holder, final int itemCount,
                                   final int flag, final int pos) {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_TO_CART_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pDialog.dismiss();

                        Log.i("tagsconverter", "*" + response + "*");

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                codeInterface.setResultCode();
                                //((AddToCartActivity)mContext).setResultCode();
                                if (flag == 0) {
                                    Log.i("Add", "success");
                                    products.get(pos).setItemCount(itemCount + 1);
                                    holder.itemCount.setText(String.valueOf(products.get(pos).getItemCount()));

                                } else {
                                    Log.i("remove", "success");
                                    products.get(pos).setItemCount(itemCount - 1);
                                    if (products.get(pos).getItemCount() != 0) {
                                        holder.itemCount.setText(String.valueOf(products.get(pos).getItemCount()));

                                    } else {
                                        int posi = holder.getAdapterPosition();
                                        products.remove(posi);
                                        notifyItemRemoved(posi);
                                        notifyItemRangeChanged(posi, products.size());
                                        if (products.size() == 0) {
                                            AddToCartActivity.emptyCart.setVisibility(View.VISIBLE);
                                            AddToCartActivity.recyclerView.setVisibility(View.GONE);
                                            AddToCartActivity.checkOut.setVisibility(View.GONE);
                                        }

                                    }

                                }

                            } else
                                Toast.makeText(mContext, "Sorry, Some error occurred", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, null) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("productName", products.get(pos).getProductName());
                params.put("flag", String.valueOf(holder.flag));
                params.put("quantity", products.get(pos).getItemQuantity());
                params.put("price", String.valueOf(products.get(pos).getProductPrice()));
                params.put("sellerName", products.get(pos).getSellerName());
                params.put("count", String.valueOf(itemCount));
                return params;
            }
        };

        SingletonRequestQueue.getInstance(mContext).addToRequestQueue(stringRequest);


    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView cartItemImage;
        ImageView removeRow;
        TextView cartItemSeller, itemCount;
        TextView cartItemName;
        TextView cartItemPrice;
        TextView cartItemQuantity;
        ImageButton addButton, removeButton;
        int flag;

        public CartListViewHolder(View itemView) {
            super(itemView);
            cartItemImage = (ImageView) itemView.findViewById(R.id.cart_item_list_image);
            removeRow = (ImageView) itemView.findViewById(R.id.cart_item_delete_button);
            cartItemSeller = (TextView) itemView.findViewById(R.id.cart_item_list_seller);
            cartItemName = (TextView) itemView.findViewById(R.id.cart_item_list_name);
            cartItemQuantity = (TextView) itemView.findViewById(R.id.cart_item_list_quantity);
            cartItemPrice = (TextView) itemView.findViewById(R.id.cart_item_list_price);
            addButton = (ImageButton) itemView.findViewById(R.id.cart_list_add_button);
            removeButton = (ImageButton) itemView.findViewById(R.id.cart_list_remove_button);
            itemCount = (TextView) itemView.findViewById(R.id.cart_list_item_count);
            removeRow.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            Log.i("removebotton", getAdapterPosition() + "");
            products.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(), products.size());

        }
    }


}
