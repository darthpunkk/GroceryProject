package com.example.android.groceryproject;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewAllFragment extends Fragment {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    DealsRecyclerAdapter dealsRecyclerAdapter;
    private static final String JSON_PRODUCT_REQUEST_URL = Constants.getInstance().ip+"FruitList.json";
    int mLongAnimationDuration;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    GridAdapter adapter;
    GridView gridView;


    public ViewAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_all, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLongAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);
        final ArrayList<Deals> DealsArrayList = new ArrayList<>();

        dealsRecyclerAdapter = new DealsRecyclerAdapter(DealsArrayList,getActivity());
       dealsRecyclerAdapter.setClickListenerInterface(new DealsRecyclerAdapter.clickListenerInterface() {
            @Override
            public void onItemClick(View v, int position) {

                Toast.makeText(getContext(), "you clicked on " + DealsArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        /*gridView = (GridView) view.findViewById(R.id.gridView);
        adapter  = new GridAdapter(DealsArrayList,getActivity());
        gridView.setAdapter(adapter);*/
        progressBar = (ProgressBar)view.findViewById(R.id.view_all_progressbar);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.view_all_layout);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycle_list3);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(dealsRecyclerAdapter);
        //recyclerView.addItemDecoration(new SpacesItemDecoration(50));




        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_PRODUCT_REQUEST_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Log.i("tagconvertstr", "*" + response + "*");

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Deals deals = new Deals();
                        deals.setName(object.getString("name"));
                        deals.setPrice(object.getInt("price"));
                        deals.setUrl(object.getString("image"));
                        DealsArrayList.add(deals);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
               relativeLayout.setAlpha(0f);
               relativeLayout.setVisibility(NestedScrollView.VISIBLE);
                progressBar.setVisibility(View.GONE);
               relativeLayout.animate()
                        .alpha(1f)
                        .setDuration(mLongAnimationDuration)
                        .setListener(null);


                dealsRecyclerAdapter.notifyDataSetChanged();

            }
        }, null);

        SingletonRequestQueue.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);
    }
}
