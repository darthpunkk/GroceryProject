package com.example.android.groceryproject;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.lsjwzh.widget.recyclerviewpager.LoopRecyclerViewPager;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment {

    private static final String JSON_PRODUCT_REQUEST_URL = "http://14.142.72.13/mandi/mobilelist.json";
    RecyclerView recyclerView1, recyclerView2;
    HorizontalRecyclerAdapter RecyclerAdapter;
    LinearLayoutManager manager1, manager2;
    GridLayoutManager manager3;
    NestedScrollView scrollView;
    FrameLayout frameLayout;
    ProgressBar progressBar;
    FragmentTransaction fragmentTransaction;
    int postion;


    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        TextView toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Ansal Palam Vihar,Gurgaon,India");

        setProgressBar(view);
        setRecyclerList(view);
        setSlideShow(view);

        return view;
    }

    public  void setSlideShow(View view){
        ArrayList<String> slideImage = new ArrayList<>();
        slideImage.add("http://slideshow.pluri.info/upload/example2.jpg");
        slideImage.add("http://www.w3schools.com/w3css/img_mountains.jpg");
        slideImage.add("http://slideshow.pluri.info/upload/example1.jpg");


        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.loopRecyclerViewPager);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(layout);
        final SlideshowRecyclerAdapter adapter =  new SlideshowRecyclerAdapter(slideImage,getActivity());
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setProgressBar(View view){

        scrollView = (NestedScrollView) view.findViewById(R.id.home_Scroll_view);
        scrollView.setVisibility(NestedScrollView.GONE);
        frameLayout = (FrameLayout) view.findViewById(R.id.homepage_fragment);
        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressBar.setVisibility(View.VISIBLE);
        params.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(params);
        frameLayout.addView(progressBar);
    }

    private void setRecyclerList(View view) {
        recyclerView1 = (RecyclerView)view.findViewById(R.id.recycle_list1);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.recycle_list2);

        manager1 = new LinearLayoutManager(getActivity());
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager2 = new LinearLayoutManager(getActivity());
        manager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager3 = new GridLayoutManager(getActivity(),3);



        final ArrayList<Product> productArrayList = new ArrayList<>();

        RecyclerAdapter = new HorizontalRecyclerAdapter(productArrayList,getActivity());

        RecyclerAdapter.setClickListenerInterface(new HorizontalRecyclerAdapter.clickListenerInterface() {
            @Override
            public void onItemClick(View v, int position) {

                Toast.makeText(getContext(), "you clicked on " + productArrayList.get(position).getProductName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),ProductDetailActivity.class);
                intent.putExtra("product",productArrayList.get(position).getProductName());
                startActivity(intent);

            }
        });

        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider);

        //recyclerView1.addItemDecoration(new DividerItemDecoration(getActivity(),dividerDrawable,1));
        recyclerView1.addItemDecoration(new DividerItemDecoration(getActivity(),dividerDrawable,0));

        recyclerView1.setLayoutManager(manager1);
        recyclerView2.setLayoutManager(manager2);

        recyclerView1.setAdapter(RecyclerAdapter);
        recyclerView2.setAdapter(RecyclerAdapter);





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

                scrollView.setVisibility(NestedScrollView.VISIBLE);
                progressBar.setVisibility(View.GONE);

                RecyclerAdapter.notifyDataSetChanged();

            }
        }, null);

        SingletonRequestQueue.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);

    }

}
