package com.example.android.groceryproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    public TextView countView;
    public MenuItem item;
    public RelativeLayout cartView;
    static int cartItemCounter = 0;
    private static final String JSON_CART = Constants.getInstance().ip + "/cart.php";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())

        {
            case R.id.search:
                Intent SearchIntent = new Intent(this, SearchActivity.class);
                startActivity(SearchIntent);
                break;
            case android.R.id.home:
                finish();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Log.i(TAG, "on_prepare_options" + cartItemCounter);

        item = menu.findItem(R.id.add_cart);
        MenuItemCompat.setActionView(item, R.layout.badge_cart);
        cartView = (RelativeLayout) item.getActionView();
        cartView.setOnClickListener(this);
        countView = (TextView) cartView.findViewById(R.id.badge_textView);
        getTotalCartItems();

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.i(TAG, "on_create_options");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void getTotalCartItems() {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, JSON_CART, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    cartItemCounter = Integer.valueOf(response.getString("count"));
                    Log.i(TAG, "on Request" + cartItemCounter);

                    if (cartItemCounter == 0)
                        countView.setVisibility(View.INVISIBLE);
                    else {
                        countView.setVisibility(View.VISIBLE);
                        countView.setText(String.valueOf(cartItemCounter));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, null);

        SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
