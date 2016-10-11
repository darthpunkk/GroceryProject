package com.example.android.groceryproject;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddToCartActivity extends AppCompatActivity {

    private static final String JSON_CART_ITEM_REQUEST_URL= Constants.getInstance().ip+"getCart.php";

    Button StartShoppingButton;
    static RecyclerView recyclerView;
    CartRecyclerAdapter adapter;
    LinearLayoutManager manager;
    ArrayList<Product> arrayList;
    static LinearLayout emptyCart;
    static RelativeLayout checkOut;
    int ResultCode= Activity.RESULT_CANCELED;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                updatePreviousActivity();

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);



        Toolbar CartToolbar = (Toolbar) findViewById(R.id.add_to_cart_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.add_cart_recycler);
        emptyCart = (LinearLayout) findViewById(R.id.empty_cart_layout);
        checkOut = (RelativeLayout)findViewById(R.id.checkoutLayout);
        arrayList = new ArrayList<>();
        adapter = new CartRecyclerAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        getJsonData();
        adapter.setCodeInterface(new CartRecyclerAdapter.ResultCodeInterface() {
            @Override
            public void setResultCode() {

                ResultCode = Activity.RESULT_OK;
                Log.i("code",ResultCode+"");

            }
        });


        setSupportActionBar(CartToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Your Cart");

        StartShoppingButton = (Button) findViewById(R.id.start_shopping_button);
        StartShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddToCartActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


    }
    private void getJsonData(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,JSON_CART_ITEM_REQUEST_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i("vedant", "*" + response + "*");

                for (int i = 0; i < response.length(); i++) {
                    try {
                        Product product = new Product();
                        JSONObject object = response.getJSONObject(i);

                        product.setProductName(object.getString("P_name"));
                        product.setProductPrice(object.getInt("P_price"));
                        product.setSellerName(object.getString("P_seller"));
                        product.setItemCount(object.getInt("P_count"));
                        product.setItemQuantity(object.getString("P_quantity"));
                        arrayList.add(product);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                adapter.notifyDataSetChanged();
                if(adapter.getItemCount()==0) {
                    emptyCart.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    checkOut.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }



            }
        }, null);

        SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
        updatePreviousActivity();

    }
    private void updatePreviousActivity(){
        Intent intent = new Intent();
        setResult(getResultCode(),intent);
        finish();
    }
    public void setResultCode(){
        ResultCode = Activity.RESULT_OK;
    }
    public int getResultCode(){
        return ResultCode;
    }
}
