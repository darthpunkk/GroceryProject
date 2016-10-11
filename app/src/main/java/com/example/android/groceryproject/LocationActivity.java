package com.example.android.groceryproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LocationActivity extends AppCompatActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener,
        View.OnClickListener,
        ResultCallback<LocationSettingsResult>, ActivityCompat.OnRequestPermissionsResultCallback {

    protected static final String TAG = "MainActivity";
    private final String JSON_REQUEST_URL= Constants.getInstance().ip+"getLocation.php";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    protected static final int MY_PERMISSIONS_REQUEST = 1;

    protected GoogleApiClient mGoogleApiClient;
    CoordinatorLayout coordinatorLayout;
    protected LocationRequest mLocationRequest;


    protected LocationSettingsRequest mLocationSettingsRequest;


    protected Location mCurrentLocation;

    SearchView searchView;
    CardView locationButton;
    TextView ResultAddress;
    ListView listView;
    ProgressBar progressBar;
    private Boolean locationCheck;
    protected Boolean mRequestingLocationUpdates;

    LocationSharedPreference sharedPreference;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        sharedPreference = new LocationSharedPreference(this);
        Intent thisIntent = getIntent();
        Boolean fromHome = thisIntent.getBooleanExtra("home",false);
        locationCheck = sharedPreference.checkLocationAvailable();

        if (locationCheck && !fromHome) {
            Intent intent = new Intent(LocationActivity.this, HomeActivity.class);
            intent.putExtra("Address", sharedPreference.getLocation());
            startActivity(intent);
            finish();
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            progressBar = (ProgressBar) findViewById(R.id.location_progressbar);
            listView = (ListView) findViewById(R.id.location_list);
            searchView = (SearchView) findViewById(R.id.location_search_view);
            locationButton = (CardView) findViewById(R.id.location_button);
            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.location_layout);

            mRequestingLocationUpdates = false;



            setSearch();
            requestLocationPermissions();
            buildGoogleApiClient();
            createLocationRequest();
            buildLocationSettingsRequest();
            locationButton.setOnClickListener(this);
        }
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                Log.i(TAG, "Displaying  permission rationale to provide additional context.");

                Snackbar.make(coordinatorLayout, "Request permission", Snackbar.LENGTH_INDEFINITE)
                        .setAction("okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(LocationActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .show();
            } else {
                Log.i(TAG, "requesting permission");
                ActivityCompat.requestPermissions(LocationActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        Log.i(TAG, "" + Arrays.toString(grantResults));

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Snackbar.make(coordinatorLayout, "Location Available",
                            Snackbar.LENGTH_SHORT).show();

                } else {
                    Snackbar.make(coordinatorLayout, "Permission not granted",
                            Snackbar.LENGTH_SHORT).show();
                }
        }


    }

    protected void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }


    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    private void setSearch() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.length() >2){
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(LocationActivity.this,newText,Toast.LENGTH_SHORT).show();
                    makeVolleyRequest(newText);


                }
                else{
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.INVISIBLE);
                }

                return false;
            }
        });
    }


    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                mGoogleApiClient.registerConnectionCallbacks(this);
                mGoogleApiClient.registerConnectionFailedListener(this);
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    status.startResolutionForResult(LocationActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        if (!locationButton.isEnabled())
                            locationButton.setEnabled(true);
                        break;

                }
                break;
        }
    }


    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = true;
            }
        });

    }


    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = null;
            ArrayList<String> addressFragments = new ArrayList<String>();
            try {
                addresses = geocoder.getFromLocation(
                        mCurrentLocation.getLatitude(),
                        mCurrentLocation.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);


                addressFragments.add(address.getSubLocality());
                addressFragments.add(address.getLocality());
                addressFragments.add(address.getAdminArea());
                addressFragments.add(address.getCountryName());
                String s1 = TextUtils.join(System.getProperty("line.separator"), addressFragments);
                //ResultAddress.setText(s1);
                sharedPreference.setLocation(s1);
                Intent intent = new Intent(LocationActivity.this, HomeActivity.class);
                intent.putExtra("Address", sharedPreference.getLocation());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        }
    }

    protected void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");


        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            updateLocationUI();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateLocationUI();
        Toast.makeText(this, "Location Updated",
                Toast.LENGTH_SHORT).show();
        stopLocationUpdates();
        locationButton.setEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onClick(View v) {
        checkLocationSettings();

        //locationButton.setEnabled(false);


    }
    public void makeVolleyRequest(final String newText)
    {


        final List<String> searchList = new ArrayList<>();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(LocationActivity.this,android.R.layout.simple_list_item_1,searchList);

        listView.setAdapter(adapter);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_REQUEST_URL,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                listView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                //searchList.clear();
                Log.i("tagconvertstr", "*" + response + "*");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");
                    Log.i("boolean", "*" + success + "*");
                    if(success) {
                        JSONArray jsonArray = jsonObject.getJSONArray("search");
                        for(int i=0;i<jsonArray.length();i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            String Locality = object.getString("locality");
                            String City = object.getString("city");
                            String State = object.getString("state");
                            String result = Locality+", "+City+", "+State;
                            searchList.add(result);

                        }

                    }
                    else
                    {
                        Toast.makeText(LocationActivity.this,"No result found",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();




            }
        }, null) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("item",newText);
                Log.i("params",""+params+"");
                return params;
            }
        };

        SingletonRequestQueue.getInstance(this).addToRequestQueue(stringRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String address  = searchList.get(position);
                sharedPreference.setLocation(address);
                Intent intent = new Intent(LocationActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("Address", sharedPreference.getLocation());
                startActivity(intent);
                finish();

            }
        });

    }

}