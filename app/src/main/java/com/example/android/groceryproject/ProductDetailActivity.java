package com.example.android.groceryproject;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDetailActivity extends BaseActivity{

    Toolbar mToolbar;
    private String Product;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    LinearLayout pagerIndicator;
    ProgressBar progressBar;
    NestedScrollView nestedScrollView;
    ArrayList<String> specsName, specsDescription, ImageArrayList;
    RecyclerView specsRecyclerView,sellerRecyclerView;
    private static final String JSON_PRODUCT_REQUEST_URL = "http://14.142.72.13/mandi/description.json";
    SpecsRecyclerAdapter specsRecyclerAdapter;
    SellerRecyclerAdapter sellerRecyclerAdapter;
    LinearLayoutManager specsLayoutManager,sellerLayoutManager;
    ArrayList<Product> ProductArrayObject;
    ArrayList<Seller> SellerArrayObject;
    TextView ProductName;
    MaterialFavoriteButton favoriteButton;
    int count =0;
    static int ResponseCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        mToolbar = (Toolbar) findViewById(R.id.product_detail_toolbar);
        // viewPager = (ViewPager) findViewById(R.id.pager);


        ProductName = (TextView) findViewById(R.id.product_detail_name);
        favoriteButton = (MaterialFavoriteButton) findViewById(R.id.product_detail_favorite_button);
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerIndicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        nestedScrollView = (NestedScrollView) findViewById(R.id.product_detail_scrollview);
        progressBar = (ProgressBar)findViewById(R.id.product_detail_progressbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Product details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Product = intent.getStringExtra("product");

        getJsonValue(Product);

        adapter = new ViewPagerAdapter(ImageArrayList, this);

        Field mScroller;

        try {
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1,false);


        final Handler handler = new Handler();

       /* handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(count==adapter.getCount()) {
                    count = 0;
                }
                viewPager.setCurrentItem(count++, true);
                handler.postDelayed(this, 5000);

            }
        },2000);*/

        specsRecyclerAdapter = new SpecsRecyclerAdapter(specsName, specsDescription, this);
        sellerRecyclerAdapter = new SellerRecyclerAdapter(SellerArrayObject,this);

        specsRecyclerView = (RecyclerView) findViewById(R.id.specs_view);
        sellerRecyclerView = (RecyclerView) findViewById(R.id.seller_view);

        specsLayoutManager = new LinearLayoutManager(this);
        specsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        specsRecyclerView.setLayoutManager(specsLayoutManager);

        sellerLayoutManager = new LinearLayoutManager(this);
        sellerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        sellerRecyclerView.setLayoutManager(sellerLayoutManager);

        
        specsRecyclerView.setNestedScrollingEnabled(false);
        sellerRecyclerView.setNestedScrollingEnabled(false);
        
        specsRecyclerView.setAdapter(specsRecyclerAdapter);
        sellerRecyclerView.setAdapter(sellerRecyclerAdapter);


        final int[] ATTRS = {android.R.attr.listDivider};
        final TypedArray a = this.obtainStyledAttributes(ATTRS);
        Drawable dividerDrawable = a.getDrawable(0);
        specsRecyclerView.addItemDecoration(new DividerItemDecoration(this, dividerDrawable, 0));
        sellerRecyclerView.addItemDecoration(new DividerItemDecoration(this, dividerDrawable, 0));
    }

    public void getJsonValue(final String Product) {

        specsName = new ArrayList<>();
        specsDescription = new ArrayList<>();
        ProductArrayObject = new ArrayList<>();
        SellerArrayObject = new ArrayList<>();
        ImageArrayList = new ArrayList<>();
        final Product product = new Product();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_PRODUCT_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("tagconvertstr", "*" + response + "*");
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    product.setProductName(jsonObject.getString("name"));
                    product.setitFavorite(jsonObject.getBoolean("favourite"));
                    JSONArray ImageArray = jsonObject.getJSONArray("image");
                    for (int i = 0; i < ImageArray.length(); i++)
                        ImageArrayList.add((String) ImageArray.get(i));

                    for (String j : ImageArrayList) {
                        Log.i("link", j);
                    }

                    JSONArray SellerArray = jsonObject.getJSONArray("seller");

                    for (int i = 0; i < SellerArray.length(); i++) {
                        Seller seller = new Seller();
                        JSONObject SellerJson = SellerArray.getJSONObject(i);
                        seller.setSellerName(SellerJson.getString("sname"));
                        seller.setSellerLogoUrl(SellerJson.getString("logo"));
                        seller.setPriceAtSeller(SellerJson.getInt("price"));
                        SellerArrayObject.add(seller);

                    }
                    for (Seller j : SellerArrayObject) {
                        Log.i("name", j.getSellerName());
                        Log.i("logo", j.getSellerLogoUrl());
                        Log.i("price", "₹" + j.getPriceAtSeller());

                    }


                    JSONArray jsonArray = jsonObject.getJSONArray("highlights");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String name = object.getString("name");
                        String Description = object.getString("Description");
                        specsName.add(name);
                        specsDescription.add(Description);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.VISIBLE);
                ProductName.setText(product.getProductName());
                favoriteButton.setFavorite(product.isitFavorite(), false);
                adapter.notifyDataSetChanged();
                setViewPagerIndicator();
                specsRecyclerAdapter.notifyDataSetChanged();
                sellerRecyclerAdapter.notifyDataSetChanged();

            }
        }, null) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("productName", Product);
                return params;
            }
        };

        SingletonRequestQueue.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void setViewPagerIndicator() {

        final int dotCount = adapter.getCount();
        final ImageView[] dots = new ImageView[dotCount];
        for (int i = 0; i < dotCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.unselecteddot, null));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(10, 0, 10, 0);
            pagerIndicator.addView(dots[i],params);

        }
        dots[0].setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.selecteddot,null));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i =0;i<dotCount;i++)
                    dots[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.unselecteddot, null));

                dots[position].setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.selecteddot,null));


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*int total = viewPager.getAdapter().getCount() - 1;
                int currentPage = viewPager.getCurrentItem();
                if (currentPage == total)
                    viewPager.setCurrentItem(1,false);

                else if (currentPage==0)
                        viewPager.setCurrentItem(total-1,false);*/
            }
        });
    }

}
