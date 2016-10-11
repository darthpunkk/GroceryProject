package com.example.android.groceryproject;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity {

    FragmentTransaction fragmentTransaction;

    Toolbar mToolbar;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mToggle;

    Intent intent;
    String[] othersListText;

    CustomExpandableAdapter customExpandableAdapter;

    List<String> listDataHeader, listPopularCategory;
    HashMap<String, List<String>> listDataChild;

    MenuItem Login;
    ArrayAdapter<String> PopularCategoryListAdapter;
    CustomListScroll customListScroll;
    ExpandableListView expandableListView;
    ListView popularCategoryListView;
    ListView otherslistView;
    NavigationView navigationView;
    LinearLayout ToolbarTitle, linearLayout;
    LoginSharedPreference loginSharedPreference;
    CircleImageView circleImageView;
    TextView acc_name,acc_email;
    LocationSharedPreference lsp;
    private static final String JSON_ALL_CATEGORY_REQUEST_URL = Constants.getInstance().ip + "category.json";
    private static final String JSON_POP_CATEGORY_REQUEST_URL = Constants.getInstance().ip + "popularcategory.json";
    private static final String CATEGORY_NAME = "Category";
    private static final String SUBCATEGORY_NAME = "SubCategory";
    private static final String SUBITEM_NAME = "subitem";
    private static final String FRAGMENT_ARG = "fragment_argument";

    CustomListViewDrawerAdapter othersListAdapter;
    TypedArray othersIcons;
    Menu menu;

    @Override
    protected void onResume() {
        Log.i("resume", "resume");
        super.onResume();
        invalidateOptionsMenu();
    }

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
        linearLayout = (LinearLayout) findViewById(R.id.connection_container);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ToolbarTitle = (LinearLayout) findViewById(R.id.toolbar_layout);
        loginSharedPreference = new LoginSharedPreference(this);
        lsp = new LocationSharedPreference(this);
        View view = navigationView.getHeaderView(0);

        circleImageView = (CircleImageView)view.findViewById(R.id.navigation_header_profile_image);
        acc_name = (TextView)view.findViewById(R.id.navigation_header_name);
        acc_email = (TextView) view.findViewById(R.id.navigation_header_email);
       if(loginSharedPreference.isLoggedIn()){
            Picasso.with(this).load(loginSharedPreference.getKeyProfileUrl()).noFade().into(circleImageView);
            acc_name.setText(loginSharedPreference.getKeyName());
           acc_email.setVisibility(View.VISIBLE);
            acc_email.setText(loginSharedPreference.getKeyMail());
        }

        menu = navigationView.getMenu();
        MenuItem location = menu.findItem(R.id.nav_location);
        Login = menu.findItem(R.id.nav_login);
        location.setTitle(lsp.getLocation());
        if(loginSharedPreference.isLoggedIn()){
            Login.setTitle("Log Out");

        }
        Retry();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_location:
                        Intent intent = new Intent(HomeActivity.this, LocationActivity.class);
                        intent.putExtra("home", true);
                        startActivity(intent);
                        break;

                    case R.id.nav_home:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_placeholder, new HomePageFragment());
                        fragmentTransaction.commit();
                        break;
                    case R.id.nav_favourites:
                        intent = new Intent(HomeActivity.this, FavouritesActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_login:
                        if(!loginSharedPreference.isLoggedIn()) {
                            intent = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else{
                            loginSharedPreference.LogOut();
                            Login.setTitle("Login");
                            acc_name.setText("Guest");
                            acc_email.setVisibility(View.INVISIBLE);
                            circleImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_account_circle));
                            Toast.makeText(getApplicationContext(),"Logged out",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.nav_rate_us:
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store"));
                        startActivity(intent);
                        break;
                    case R.id.nav_contact_us:
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                        intent.setData(Uri.parse("test@gmail.com"));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Contact us");
                        startActivity(intent);
                        break;

                }
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    private void Retry() {

        linearLayout.setVisibility(View.GONE);
        setupActionBar();
        //setupNavigationDrawer();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_placeholder, new HomePageFragment());
        //fragmentTransaction.
        fragmentTransaction.commit();

    }

    public void setupActionBar() {


        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        // Intent intent = getIntent();
        // Log.i("HomePageFragment",intent.getStringExtra("Address"));
        toolbarTitle.setText(lsp.getLocation());


        ToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LocationActivity.class);
                intent.putExtra("home", true);
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

    }

   /* public void setupNavigationDrawer() {
        customListScroll = new CustomListScroll();
        setHomeActionLayout();
        setPopularCategoryList();
        setAllCategoryList();
        setOthersList();
    }*/

   /* private void setHomeActionLayout() {

        TextView HomeText = (TextView) findViewById(R.id.home);
        HomeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_placeholder, new HomePageFragment());
                fragmentTransaction.commit();
                mDrawer.closeDrawer(GravityCompat.START);

            }
        });

    }

    /*private void setPopularCategoryList() {


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
                        listPopularCategory.add((String) jsonArray.get(i));
                    }

                    PopularCategoryListAdapter = new ArrayAdapter<>(HomeActivity.this, R.layout.custom_textview, listPopularCategory);
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
                bundle.putString(FRAGMENT_ARG, parent.getItemAtPosition(position).toString());
                productListFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_placeholder, productListFragment, "productList");
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

                            list.add((String) jsonArray2.get(j));
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
                bundle.putString(FRAGMENT_ARG, listDataChild.get(listDataHeader.get(groupPosition)).get(
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
        // othersIcons = getResources().obtainTypedArray(R.array.others_icon_array_items);
        //othersListAdapter = new CustomListViewDrawerAdapter(this, othersListText, othersIcons);
        ArrayAdapter<String> othersListAdapter = new ArrayAdapter<>(HomeActivity.this, R.layout.custom_textview, othersListText);
        otherslistView.setAdapter(othersListAdapter);
        customListScroll.setListViewHeightBasedOnChildren(otherslistView);
        otherslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        intent = new Intent(HomeActivity.this, FavouritesActivity.class);
                        startActivity(intent);
                        break;

                    case 2:
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store"));
                        startActivity(intent);
                        break;

                    case 3:
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                        intent.setData(Uri.parse("test@gmail.com"));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Contact us");
                        startActivity(intent);
                        break;
                }
            }
        });
    }*/

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public void onClick(View v) {
        Intent CartIntent = new Intent(HomeActivity.this, AddToCartActivity.class);
        startActivity(CartIntent);
    }
}


