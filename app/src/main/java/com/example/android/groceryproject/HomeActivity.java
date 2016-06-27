package com.example.android.groceryproject;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends BaseActivity {

    FragmentTransaction fragmentTransaction;

    Toolbar mToolbar;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mToggle;


    String[] othersListText;
    TypedArray othersIcons;
    CustomExpandableAdapter customExpandableAdapter;

    List<String> listDataHeader, listPopularCategory;
    HashMap<String, List<String>> listDataChild;
    CustomListViewDrawerAdapter othersListAdapter;

    ArrayAdapter<String> PopularCategoryListAdapter;

    CustomListScroll customListScroll;
    ExpandableListView expandableListView;
    ListView popularCategoryListView;
    ListView otherslistView;

    LinearLayout ToolbarTitle;


    @Override
    protected void onResume() {
        Log.i("resume","resume");
        super.onResume();
        invalidateOptionsMenu();
    }




    private static final String JSON_ALL_CATEGORY_REQUEST_URL = "http://14.142.72.13/mandi/category.json";
    private static final String JSON_POP_CATEGORY_REQUEST_URL = "http://14.142.72.13/mandi/popularcategory.json";
    private static final String CATEGORY_NAME = "Category";
    private static final String SUBCATEGORY_NAME = "SubCategory";
    private static final String SUBITEM_NAME = "subitem";
    private static final String FRAGMENT_ARG = "fragment_argument";

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupActionBar();
        setupNavigationDrawer();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_placeholder, new HomePageFragment());
        fragmentTransaction.commit();

    }

    public void setupActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ToolbarTitle = (LinearLayout) findViewById(R.id.toolbar_layout);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mToggle);

        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.actionbar_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

    }

    public void setupNavigationDrawer() {
        customListScroll = new CustomListScroll();
        setPopularCategoryList();
        setAllCategoryList();
        setOthersList();
    }

    private void setPopularCategoryList() {


        popularCategoryListView = (ListView) findViewById(R.id.popular_categories_list);
        //popularCategoryListView.setNestedScrollingEnabled(false);

        listPopularCategory = new ArrayList<>();

        JsonObjectRequest PopularCategoryRequest = new JsonObjectRequest(Method.GET, JSON_POP_CATEGORY_REQUEST_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Log.i("tagconvertstr", "*" + response + "*");

                try {

                    JSONArray jsonArray = response.getJSONArray("PopularCategories");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject Item = jsonArray.getJSONObject(i);
                        String ItemName = Item.getString(CATEGORY_NAME);
                        listPopularCategory.add(ItemName);

                    }

                    PopularCategoryListAdapter = new ArrayAdapter<>(HomeActivity.this, R.layout.list_textview, listPopularCategory);
                    popularCategoryListView.setAdapter(PopularCategoryListAdapter);
                    customListScroll.setListViewHeightBasedOnChildren(popularCategoryListView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, null);

        SingletonRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(PopularCategoryRequest);

        popularCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(
                        getApplicationContext(), "item: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT)
                        .show();

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                ProductListFragment productListFragment = new ProductListFragment();
                Bundle bundle = new Bundle();
                bundle.putString(FRAGMENT_ARG,parent.getItemAtPosition(position).toString());
                productListFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_placeholder, productListFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                mDrawer.closeDrawer(GravityCompat.START);

            }
        });

    }

    private void setAllCategoryList() {

        expandableListView = (ExpandableListView) findViewById(R.id.expandable_listView);

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        JsonObjectRequest AllCategoryRequest = new JsonObjectRequest(Method.GET, JSON_ALL_CATEGORY_REQUEST_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i("tagconvertstr", "*" + response + "*");

                try {

                    JSONArray jsonArray1 = response.getJSONArray("AllCategories");


                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject Group = jsonArray1.getJSONObject(i);
                        String GroupName = Group.getString(CATEGORY_NAME);
                        listDataHeader.add(GroupName);
                        List<String> list = new ArrayList<>();
                        JSONArray jsonArray2 = Group.getJSONArray(SUBCATEGORY_NAME);

                        for (int j = 0; j < jsonArray2.length(); j++) {
                            JSONObject Child = jsonArray2.getJSONObject(j);
                            //Log.i("tagconvertstr", "*" + Child + "*");
                            String ChildName = Child.getString(SUBITEM_NAME);
                            Log.i("tagconvertstr", "*" + ChildName + "*");
                            list.add(ChildName);
                        }

                        listDataChild.put(GroupName, list);
                    }

                    customExpandableAdapter = new CustomExpandableAdapter(HomeActivity.this, listDataHeader, listDataChild);
                    expandableListView.setAdapter(customExpandableAdapter);
                    customListScroll.setListViewHeightBasedOnChildren(expandableListView);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);

        SingletonRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(AllCategoryRequest);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                customListScroll.setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                ProductListFragment productListFragment = new ProductListFragment();
                Bundle bundle = new Bundle();
                bundle.putString(FRAGMENT_ARG,listDataChild.get(listDataHeader.get(groupPosition)).get(
                        childPosition));
                productListFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_placeholder, productListFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                mDrawer.closeDrawer(GravityCompat.START);

                return false;
            }
        });

    }

    private void setOthersList() {

        otherslistView = (ListView) findViewById(R.id.other_list);
        othersListText = getResources().getStringArray(R.array.others_list_textitems);
        othersIcons = getResources().obtainTypedArray(R.array.others_icon_array_items);

        othersListAdapter = new CustomListViewDrawerAdapter(this, othersListText, othersIcons);
        otherslistView.setAdapter(othersListAdapter);
        customListScroll.setListViewHeightBasedOnChildren(otherslistView);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}


