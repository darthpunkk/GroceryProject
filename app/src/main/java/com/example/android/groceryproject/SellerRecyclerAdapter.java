package com.example.android.groceryproject;

/**
 * Created by Vedant on 19-06-2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Vedant on 6/9/2016.
 */
public class SellerRecyclerAdapter extends RecyclerView.Adapter<SellerRecyclerAdapter.SellerListViewHolder> {

    List<Seller> sellers;
    private String productName;
    private Context mContext;

    private final String ADD_TO_CART_URL = Constants.getInstance().ip + "addToCart.php";


    SellerRecyclerAdapter(List<Seller> sellers, Context mContext, String productName) {
        this.sellers = sellers;
        this.mContext = mContext;
        this.productName = productName;

    }

    @Override
    public SellerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_sellers, parent, false);

        return new SellerListViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final SellerListViewHolder holder, final int position) {


        Picasso.with(mContext).load(sellers.get(position).getSellerLogoUrl()).into(holder.sellerLogo);


        final ArrayList<String> quantityList = new ArrayList<>(sellers.get(position).getQuantityArrayList());
        final ArrayList<Integer> priceList = new ArrayList<>(sellers.get(position).getPriceArrayList());
        final ArrayList<Integer> countList = new ArrayList<>(sellers.get(position).getCountArrayList());

        sellers.get(position).setPriceAtSeller(priceList.get(0));
        holder.sellerPrice.setText("₹ " + priceList.get(0));

        sellers.get(position).setQuantity(quantityList.get(0));
        holder.itemQuantity.setText(quantityList.get(0));

        holder.itemCount.setText(String.valueOf(countList.get(0)));

        if (countList.get(0) == 0) {
            holder.itemCount.setVisibility(View.INVISIBLE);
            holder.removeButton.setVisibility(View.INVISIBLE);
        }


        if (quantityList.size() > 1) {

            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.quantity_textview, quantityList);

            holder.PriceQuantitySpinner.setAdapter(adapter);
            holder.PriceQuantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if (countList.get(pos) == 0) {
                        holder.itemCount.setVisibility(View.INVISIBLE);
                        holder.removeButton.setVisibility(View.INVISIBLE);
                    } else {
                        holder.itemCount.setVisibility(View.VISIBLE);
                        holder.removeButton.setVisibility(View.VISIBLE);

                    }

                    sellers.get(position).setPriceAtSeller(priceList.get(pos));
                    holder.sellerPrice.setText("₹ " + priceList.get(pos));
                    holder.itemCount.setText(String.valueOf(countList.get(pos)));
                    sellers.get(position).setQuantity(quantityList.get(pos));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            holder.relativeLayout.setVisibility(View.INVISIBLE);
            holder.itemQuantity.setVisibility(View.VISIBLE);
        }


        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionFlag = 0;
                if (holder.relativeLayout.getVisibility() == View.VISIBLE)
                    positionFlag = holder.PriceQuantitySpinner.getSelectedItemPosition();

                Log.i("positionFlag",""+positionFlag);
                Log.i("positionFlag",""+countList.get(positionFlag));
                makeVolleyRequest(holder, countList,holder.flag = 0, position, positionFlag);
            }

        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionFlag = 0;
                if (holder.relativeLayout.getVisibility() == View.VISIBLE)
                    positionFlag = holder.PriceQuantitySpinner.getSelectedItemPosition();

                Log.i("positionFlag",""+positionFlag);
                Log.i("positionFlag",""+countList.get(positionFlag));
                makeVolleyRequest(holder, countList,holder.flag = 1, position, positionFlag);
            }

        });

    }

    private void makeVolleyRequest(final SellerListViewHolder holder,
                                   final ArrayList<Integer> countList,
                                   final int flag, final int pos,
                                   final int positionFlag) {
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

                        if (flag==0) {
                            Log.i("Add","success");
                            countList.set(positionFlag, countList.get(positionFlag) + 1);
                            holder.itemCount.setText(String.valueOf(countList.get(positionFlag)));
                            holder.removeButton.setVisibility(View.VISIBLE);
                            holder.itemCount.setVisibility(View.VISIBLE);

                        } else {
                            Log.i("remove","success");
                            countList.set(positionFlag, countList.get(positionFlag) - 1);
                            holder.itemCount.setText(String.valueOf(countList.get(positionFlag)));

                            if (countList.get(positionFlag) == 0) {
                                holder.removeButton.setVisibility(View.INVISIBLE);
                                holder.itemCount.setVisibility(View.INVISIBLE);
                            }

                        }
                        //ProductDetailActivity.cartItemCounter++;
                        //Log.i("count", "adapter" + ProductDetailActivity.cartItemCounter);
                        ActivityCompat.invalidateOptionsMenu((ProductDetailActivity) mContext);
                        Toast.makeText(mContext, "Updated your Cart", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(mContext, "Sorry, Some error occurred", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, null) {
            @Override
            protected Map<String, String> getParams() {
               Log.i("productName", productName);
                Log.i("flag",String.valueOf(holder.flag));
                Log.i("quantity", sellers.get(pos).getQuantity());
                Log.i("price", String.valueOf(sellers.get(pos).getPriceAtSeller()));
                Log.i("sellerName", sellers.get(pos).getSellerName());
                Log.i("count", String.valueOf(countList.get(positionFlag)));
                Map<String, String> params = new HashMap<>();
                params.put("productName", productName);
                params.put("flag",String.valueOf(holder.flag));
                params.put("quantity", sellers.get(pos).getQuantity());
                params.put("price", String.valueOf(sellers.get(pos).getPriceAtSeller()));
                params.put("sellerName", sellers.get(pos).getSellerName());
                params.put("count", String.valueOf(countList.get(positionFlag)));
                return params;
            }
        };

        SingletonRequestQueue.getInstance(mContext).addToRequestQueue(stringRequest);


    }

    @Override
    public int getItemCount() {
        return sellers.size();
    }



    public class SellerListViewHolder extends RecyclerView.ViewHolder {

        ImageView sellerLogo;
        TextView sellerPrice;
        TextView itemCount, itemQuantity;
        ImageButton addButton, removeButton;
        Spinner PriceQuantitySpinner;
        RelativeLayout relativeLayout;
        int flag;

        public SellerListViewHolder(View itemView) {
            super(itemView);
            sellerLogo = (ImageView) itemView.findViewById(R.id.seller_logo);
            itemQuantity = (TextView) itemView.findViewById(R.id.product_quantity);
            PriceQuantitySpinner = (Spinner) itemView.findViewById(R.id.price_quantity_spinner);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.spinner_layout);
            sellerPrice = (TextView) itemView.findViewById(R.id.price_at_seller);
            addButton = (ImageButton) itemView.findViewById(R.id.add_button);
            removeButton = (ImageButton) itemView.findViewById(R.id.remove_button);
            itemCount = (TextView) itemView.findViewById(R.id.item_count);

        }
    }


}

