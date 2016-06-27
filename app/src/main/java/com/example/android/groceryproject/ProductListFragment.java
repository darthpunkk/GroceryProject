package com.example.android.groceryproject;


import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends Fragment {

    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    LinearLayoutManager manager;
    VerticalRecyclerAdapter RecyclerAdapter;
    static int choice;
    ArrayList<Product> sortedProductArrayList;
    ArrayList<Product> productArrayList;
    TextView CategoryName;
    int itemCount=0;

    private static final String JSON_PRODUCT_REQUEST_URL = "http://14.142.72.13/mandi/mobilelist.json";
    private static final String FRAGMENT_ARG = "fragment_argument";


    public ProductListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_product_list, container, false);
        // Inflate the layout for this fragment

        String arg;
        setProgressBar(view);
        if(getArguments()!=null) {
            arg = getArguments().getString(FRAGMENT_ARG);
            CategoryName = (TextView)view.findViewById(R.id.product_list_category_name);
            CategoryName.setText(arg+"("+itemCount+")");
            setProductList(arg,view);
        }

        LinearLayout SortLinearLayout = (LinearLayout)view.findViewById(R.id.sort_layout);
        SortLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                choice=0;

               final  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.ThemeDialogCustom);

               final AlertDialog dialog = builder.setTitle(R.string.sort_by).setSingleChoiceItems(R.array.sort_by_array, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        choice = which;
                       // Log.i("choice",""+choice+"");

                    }
                }).setPositiveButton(R.string.sort, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (choice){

                            case 0:
                                sortListPopular();
                                break;
                            case 1:
                                sortListLowPrice();
                                break;
                            case 2:
                                sortListHighPrice();
                                break;
                            case 3:
                                sortListAlphabetical();
                                break;
                        }

                    }
                }).setNegativeButton(R.string.cancel,null).create();

                dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                    }
                });
                dialog.show();

            }


        });


    return view;

    }
    private void setProductList(final String arg, View view){

        recyclerView = (RecyclerView)view.findViewById(R.id.product_list);
        recyclerView.setNestedScrollingEnabled(false);

        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        productArrayList = new ArrayList<>();
        sortedProductArrayList = new ArrayList<>();

        RecyclerAdapter = new VerticalRecyclerAdapter(sortedProductArrayList,getActivity());
        RecyclerAdapter.setClickListenerInterface(new VerticalRecyclerAdapter.clickListenerInterface() {
            @Override
            public void clickView(View view, int position) {
                Toast.makeText(getContext(), "you clicked on " + sortedProductArrayList.get(position).getProductName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),ProductDetailActivity.class);
                intent.putExtra("product",sortedProductArrayList.get(position).getProductName());
                startActivity(intent);

            }

            @Override
            public void clickFavourite(MaterialFavoriteButton button, int position, List<Product> products) {
                Boolean check = ( products.get(position).isitFavorite())?false:true;
                products.get(position).setitFavorite(check);
                if(check)
                    Toast.makeText(getContext(),R.string.favorite,Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(),R.string.unfavorite,Toast.LENGTH_SHORT).show();

                button.setFavorite(products.get(position).isitFavorite(),true);
            }
        });

        recyclerView.setLayoutManager(manager);
        final int[] ATTRS = {android.R.attr.listDivider};
        final TypedArray a = getActivity().obtainStyledAttributes(ATTRS);
        Drawable dividerDrawable = a.getDrawable(0);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),dividerDrawable,0));
        recyclerView.setAdapter(RecyclerAdapter);


        /*recyclerView.addOnItemTouchListener(new RecyclerOnItemClickListener(getContext(), new RecyclerOnItemClickListener.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Toast.makeText(getContext(), "you clicked on " + sortedProductArrayList.get(position).getProductName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),ProductDetailActivity.class);
                intent.putExtra("product",sortedProductArrayList.get(position).getProductName());
                startActivity(intent);
            }
        }));*/


       StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_PRODUCT_REQUEST_URL,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.i("tagconvertstr", "*" + response + "*");
                try {
                    JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        Product product = new Product();
                        product.setProductName(object.getString("name"));
                        product.setProductPrice(object.getInt("price"));
                        product.setProductImageUrl(object.getString("image"));

                        int count= object.getInt("sellercount");
                        String seller = (count>1)?" sellers":" seller";
                        product.setSellerCount("At "+count+ seller);
                        product.setitFavorite(object.getBoolean("favorite"));
                        productArrayList.add(product);
                        sortedProductArrayList.add(product);
                    }
                } catch (JSONException e) {
                        e.printStackTrace();
                    }

                relativeLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                RecyclerAdapter.notifyDataSetChanged();
                itemCount = RecyclerAdapter.getItemCount();
                CategoryName.setText(arg+" ("+itemCount+")");

            }
        }, null) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("title",arg);
                return params;
            }
        };

        SingletonRequestQueue.getInstance(getContext()).addToRequestQueue(stringRequest);



    }

    public void sortListPopular()
    {
        sortedProductArrayList = (ArrayList<Product>)productArrayList.clone();
        RecyclerAdapter.setProductsList(sortedProductArrayList);
        RecyclerAdapter.notifyDataSetChanged();

    }
    public void sortListLowPrice()
    {
        Collections.sort(sortedProductArrayList, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return ((Integer)lhs.getProductPrice()).compareTo(rhs.getProductPrice());
            }
        });
        RecyclerAdapter.setProductsList(sortedProductArrayList);
        RecyclerAdapter.notifyDataSetChanged();
    }
    public void  sortListHighPrice()
    {
            Collections.sort(sortedProductArrayList, new Comparator<Product>() {
                @Override
                public int compare(Product lhs, Product rhs) {
                    return ((Integer)rhs.getProductPrice()).compareTo(lhs.getProductPrice());
                }
            });
            RecyclerAdapter.setProductsList(sortedProductArrayList);
            RecyclerAdapter.notifyDataSetChanged();
    }
    public void  sortListAlphabetical()
    {
        Collections.sort(sortedProductArrayList, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return lhs.getProductName().compareToIgnoreCase(rhs.getProductName());
            }
        });
        RecyclerAdapter.setProductsList(sortedProductArrayList);
        RecyclerAdapter.notifyDataSetChanged();
    }

    private void setProgressBar(View view){

        relativeLayout = (RelativeLayout) view.findViewById(R.id.product_list_relative_layout);
        relativeLayout.setVisibility(view.GONE);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.product_list_layout);
        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
       FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressBar.setVisibility(View.VISIBLE);
        params.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(params);
        frameLayout.addView(progressBar);
    }

}
