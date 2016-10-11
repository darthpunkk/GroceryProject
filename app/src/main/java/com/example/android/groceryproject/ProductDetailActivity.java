package com.example.android.groceryproject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProductDetailActivity extends BaseActivity {

    private static final String JSON_PRODUCT_REQUEST_URL = Constants.getInstance().ip + "description.json";
    private static final String JSON_SELLER_REQUEST_URL = Constants.getInstance().ip + "seller.json";

    Toolbar mToolbar;
    ViewPager viewPager;
    ViewPagerAdapter adapter;

    ProgressBar progressBar;
    NestedScrollView nestedScrollView;
    ArrayList<String> specsName, specsDescription, ImageArrayList;
    RecyclerView specsRecyclerView, sellerRecyclerView;
    SpecsRecyclerAdapter specsRecyclerAdapter;
    SellerRecyclerAdapter sellerRecyclerAdapter;
    LinearLayoutManager specsLayoutManager, sellerLayoutManager;
    ArrayList<Product> ProductArrayObject;
    ArrayList<Seller> SellerArrayObject;
    TextView ProductName;
    MaterialFavoriteButton favoriteButton;
    private String productName;
    private int mLongAnimationDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        productName = intent.getStringExtra("product");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        mToolbar = (Toolbar) findViewById(R.id.product_detail_toolbar);
        mLongAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);
        ProductName = (TextView) findViewById(R.id.product_detail_name);
        favoriteButton = (MaterialFavoriteButton) findViewById(R.id.product_detail_favorite_button);
        viewPager = (ViewPager) findViewById(R.id.pager);
        nestedScrollView = (NestedScrollView) findViewById(R.id.product_detail_scrollview);
        progressBar = (ProgressBar) findViewById(R.id.product_detail_progressbar);
        specsRecyclerView = (RecyclerView) findViewById(R.id.specs_view);
        sellerRecyclerView = (RecyclerView) findViewById(R.id.seller_view);


       /* final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.collapse_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                scrollRange = appBarLayout.getTotalScrollRange();
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Product Details");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });*/

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SellerArrayObject = new ArrayList<>();

        getJsonValue(productName);
        setSeller(productName);

        adapter = new ViewPagerAdapter(ImageArrayList, this);


        viewPager.setAdapter(adapter);


        specsRecyclerAdapter = new SpecsRecyclerAdapter(specsName, specsDescription, this);
        sellerRecyclerAdapter = new SellerRecyclerAdapter(SellerArrayObject, this, productName);


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

    public void getJsonValue(final String productName) {

        specsName = new ArrayList<>();
        specsDescription = new ArrayList<>();
        ProductArrayObject = new ArrayList<>();

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
                specsRecyclerAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                nestedScrollView.setAlpha(0f);
                nestedScrollView.setVisibility(View.VISIBLE);
                nestedScrollView.animate()
                        .alpha(1f)
                        .setDuration(mLongAnimationDuration)
                        .setListener(null);
                ProductName.setText(product.getProductName());
                favoriteButton.setFavorite(product.isitFavorite(), false);
                adapter.notifyDataSetChanged();
                setViewPagerIndicator();



            }
        }, null) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("productName", productName);
                return params;
            }
        };

        SingletonRequestQueue.getInstance(this).addToRequestQueue(stringRequest);
    }
    public void setSeller(final String productName){
        SellerArrayObject.clear();
        Log.i("setSeller","setSeller");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_SELLER_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("tagconvertstr", "*" + response + "*");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray SellerArray = jsonObject.getJSONArray("seller");

                    for (int i = 0; i < SellerArray.length(); i++) {
                        Seller seller = new Seller();
                        JSONObject SellerJson = SellerArray.getJSONObject(i);
                        seller.setSellerName(SellerJson.getString("sname"));
                        seller.setSellerLogoUrl(SellerJson.getString("logo"));
                        JSONArray price_quantity = SellerJson.getJSONArray("pricequantity");
                        ArrayList<Integer> priceList = new ArrayList<>();
                        ArrayList<String> quantityList = new ArrayList<>();
                        ArrayList<Integer> countList = new ArrayList<>();
                        for (int j = 0; j < price_quantity.length(); j++) {
                            JSONObject object1 = price_quantity.getJSONObject(j);
                            priceList.add(object1.getInt("price"));
                            quantityList.add(object1.getString("quantity"));
                            countList.add(object1.getInt("itemCount"));
                        }
                        seller.setArrayLists(quantityList, countList, priceList);
                        SellerArrayObject.add(seller);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                sellerRecyclerAdapter.notifyDataSetChanged();

            }
        }, null) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("productName", productName);
                return params;
            }
        };

        SingletonRequestQueue.getInstance(this).addToRequestQueue(stringRequest);

    }
    public void setViewPagerIndicator() {
        LinearLayout pagerIndicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        final int dotCount = adapter.getCount();
        final ImageView[] dots = new ImageView[dotCount];
        for (int i = 0; i < dotCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.unselecteddot, null));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(10, 0, 10, 0);
            pagerIndicator.addView(dots[i], params);

        }
        dots[0].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.selecteddot, null));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotCount; i++)
                    dots[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.unselecteddot, null));

                dots[position].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.selecteddot, null));


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

    @Override
    public void onClick(View v) {
        Intent CartIntent = new Intent(ProductDetailActivity.this, AddToCartActivity.class);

        startActivityForResult(CartIntent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Log.i("resultCode",""+resultCode+" "+requestCode);
            if(resultCode == Activity.RESULT_OK)
            {
            invalidateOptionsMenu();
            sellerRecyclerView.invalidate();
            setSeller(productName);
            }

        }
    }
}
