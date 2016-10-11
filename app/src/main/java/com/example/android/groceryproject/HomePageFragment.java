package com.example.android.groceryproject;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.lsjwzh.widget.recyclerviewpager.LoopRecyclerViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment {

    private static final String JSON_PRODUCT_REQUEST_URL = Constants.getInstance().ip+"FruitList.json";
    private static final String JSON_CATEGORY_REQUEST_URL = Constants.getInstance().ip+"subcategory.json";
    private static final String JSON_DEALS_REQUEST_URL = Constants.getInstance().ip+"Deals.json";
    private static final String FRAGMENT_ARG = "fragment_argument";
    RecyclerView recyclerView1, recyclerView2;
    HorizontalRecyclerAdapter RecyclerAdapter;
    LinearLayoutManager linearManager;
    GridLayoutManager GridManager;
    NestedScrollView scrollView;
    ProgressBar progressBar;
    TextView ViewAllButton;
    LinearLayoutManager manager;
    TextView expandText;
    LinearLayout linearLayout;
    LoopRecyclerViewPager RecyclerLoopViewPager;
    SlideshowRecyclerAdapter adapter1;
    FragmentTransaction fragmentTransaction;
    boolean categoryIsExpanded = false;
    GridRecyclerAdapter GridAdapter;
    int mLongAnimationDuration;
    Handler handler;
    Runnable runnable;
    final int speedScroll = 5000;
    boolean resume = false;

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        if(handler!=null){
            handler.removeCallbacks(runnable);
            resume = true;
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if(resume){
            if(handler!=null)
            handler.postDelayed(runnable, speedScroll);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater,container,savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        mLongAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);
        scrollView = (NestedScrollView) view.findViewById(R.id.home_Scroll_view);
        progressBar = (ProgressBar) view.findViewById(R.id.homefragment_progressbar);
        RecyclerLoopViewPager = (LoopRecyclerViewPager) view.findViewById(R.id.pager);
        manager = new LinearLayoutManager(getActivity());
        ViewAllButton = (TextView) view.findViewById(R.id.view_all_button);

        recyclerView1 = (RecyclerView) view.findViewById(R.id.recycle_list1);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.recycle_list2);

        linearLayout = (LinearLayout) view.findViewById(R.id.category_expand);
        expandText = (TextView) view.findViewById(R.id.category_expand_text);
        
        linearManager = new LinearLayoutManager(getActivity());



        resume=false;

        setCategory();
        setRecyclerList();
        setSlideShow();

        return view;
    }

    public void setCategory() {

        final ArrayList<Category> categoryArrayList = new ArrayList<>();
       
        recyclerView2.setNestedScrollingEnabled(false);
        GridManager = new GridLayoutManager(getActivity(), 3);
        recyclerView2.setLayoutManager(GridManager);
        GridAdapter = new GridRecyclerAdapter(categoryArrayList, getActivity());


        Drawable dividerDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.divider, null);

        recyclerView2.addItemDecoration(new DividerItemDecoration(getActivity(), dividerDrawable, 0));
        recyclerView2.addItemDecoration(new DividerItemDecoration(getActivity(), dividerDrawable, 1));
        recyclerView2.setAdapter(GridAdapter);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_CATEGORY_REQUEST_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Log.i("tagconvertstr category", "*" + response + "*");

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Category category = new Category();
                        category.setName(object.getString("categoryName"));
                        category.setUrl(object.getString("categoryIcon"));
                        categoryArrayList.add(category);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                scrollView.setAlpha(0f);
                scrollView.setVisibility(NestedScrollView.VISIBLE);
                progressBar.setVisibility(View.GONE);
                scrollView.animate()
                        .alpha(1f)
                        .setDuration(mLongAnimationDuration)
                        .setListener(null);
                GridAdapter.notifyDataSetChanged();
                expandCategory();


            }
        }, null);

        SingletonRequestQueue.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);

        GridAdapter.setClickListenerInterface(new GridRecyclerAdapter.clickListenerInterface() {
            @Override
            public void onItemClick(View v, int position) {

                Toast.makeText(getContext(), "you clicked on " + categoryArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                ProductListFragment productListFragment = new ProductListFragment();
                Bundle bundle = new Bundle();
                bundle.putString(FRAGMENT_ARG,categoryArrayList.get(position).getName());
                productListFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_placeholder, productListFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

    }

    private void expandCategory() {

        
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoryIsExpanded = !categoryIsExpanded;
                if (categoryIsExpanded) {

                    GridAdapter.setMaxItems(0);

                    GridAdapter.notifyDataSetChanged();

                    expandText.setText(R.string.less_categories);

                } else {
                    GridAdapter.setMaxItems(1);
                    GridAdapter.notifyDataSetChanged();
                    expandText.setText(R.string.more_categories);
                }

            }
        });


    }

    public void setSlideShow() {
        final ArrayList<String> slideImage = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_DEALS_REQUEST_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Log.i("vedant", "*" + response + "*");

                for (int i = 0; i < response.length(); i++) {
                    try {
                        String url = (String) response.get(i);
                        slideImage.add(url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                RecyclerLoopViewPager.setLayoutManager(manager);
                adapter1 = new SlideshowRecyclerAdapter(slideImage, getActivity());
                RecyclerLoopViewPager.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();
                autoScroll();


            }
        }, null);

        SingletonRequestQueue.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);


    }

    public void autoScroll() {

        handler = new Handler();
        runnable = new Runnable() {

            @Override
            public void run() {
                int count = RecyclerLoopViewPager.getCurrentPosition();
                RecyclerLoopViewPager.smoothScrollToPosition(++count);
                handler.postDelayed(this, speedScroll);


            }
        };

        handler.postDelayed(runnable, speedScroll);
        RecyclerLoopViewPager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == recyclerView.SCROLL_STATE_DRAGGING) {
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 10000);
                }
            }
        });

    }

    private void setRecyclerList() {

        linearManager.setOrientation(LinearLayoutManager.HORIZONTAL);


        final ArrayList<Product> productArrayList = new ArrayList<>();

        RecyclerAdapter = new HorizontalRecyclerAdapter(productArrayList, getActivity());

        RecyclerAdapter.setClickListenerInterface(new HorizontalRecyclerAdapter.clickListenerInterface() {
            @Override
            public void onItemClick(View v, int position) {

                Toast.makeText(getContext(), "you clicked on " + productArrayList.get(position).getProductName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("product", productArrayList.get(position).getProductName());
                startActivity(intent);

            }
        });

        recyclerView1.setLayoutManager(linearManager);
        recyclerView1.setAdapter(RecyclerAdapter);

       /* recyclerView1.addOnItemTouchListener(new RecyclerOnItemClickListener(getContext(), new RecyclerOnItemClickListener.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Toast.makeText(getContext(), "you clicked on " + productArrayList.get(position).getProductName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),ProductDetailActivity.class);
                intent.putExtra("product",productArrayList.get(position).getProductName());
                startActivity(intent);
            }
        }));*/


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_PRODUCT_REQUEST_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Log.i("tagconvertstr", "*" + response + "*");

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Product product = new Product();
                        product.setProductName(object.getString("name"));
                        product.setProductPrice(object.getInt("price"));
                        product.setProductImageUrl(object.getString("image"));
                        productArrayList.add(product);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                RecyclerAdapter.notifyDataSetChanged();

            }
        }, null);

        SingletonRequestQueue.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);

        ViewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_placeholder, new ViewAllFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

    }


}
