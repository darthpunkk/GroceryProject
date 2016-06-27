package com.example.android.groceryproject;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseActivity extends AppCompatActivity {

    Menu menu;
    public TextView countView;
    public MenuItem item;
    public RelativeLayout cartView;
    static boolean isVisible=false;
    static int counter=0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())

        {
            case R.id.search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;

            /*case R.id.add_cart:
                Intent intent1 = new Intent(this, SearchActivity.class);
                startActivity(intent1);*/
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i("count","onprepare"+counter);
        item = menu.findItem(R.id.add_cart);
        MenuItemCompat.setActionView(item,R.layout.badge_cart);
        cartView = (RelativeLayout)item.getActionView();
        countView = (TextView)cartView.findViewById(R.id.badge_textView);
        if(counter==0)
            countView.setVisibility(View.INVISIBLE);
        else {
            countView.setVisibility(View.VISIBLE);
            countView.setText(String.valueOf(counter));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("TAG","onCreateOptions");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
