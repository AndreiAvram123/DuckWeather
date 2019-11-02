package com.example.andrei.duckweather.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrei.duckweather.R;
import com.example.andrei.duckweather.model.Constraints;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChangeLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener
        , LocationListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private TextView locationTextView;
    private ProgressBar loadingMap;
    private TextView loadingMessage;
    private Location currentLocation;
    private static final int APP_REQUEST_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton checkFab = findViewById(R.id.fab_change_location_activity);
        locationTextView = findViewById(R.id.location_text_view);
        loadingMap = findViewById(R.id.loading_map);
        loadingMessage = findViewById(R.id.loading_location_label);

        checkFab.setOnClickListener(view -> updateLocationInApp());
    }

    private void updateLocationInApp() {
        if (currentLocation != null) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constraints.KEY_LOCATION_REQUEST_LOCATION, locationTextView.getText().toString());
            returnIntent.putExtra(Constraints.KEY_LATITUDE_REQUEST_LOCATION, currentLocation.getLatitude() + "");
            returnIntent.putExtra(Constraints.KEY_LONGITUDE_REQUEST_LOCATION, currentLocation.getLongitude() + "");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        //check if permissions have been granted
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {    //if so request location
            requestLocation();
        } else {
            //if the permission has not been granted for the first time
            //we try to display a message to the user explaining why we
            //need that particular permission,very helpful method
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Location permission is needed in order for the app to work ", Toast.LENGTH_SHORT).show();
            }
            //the way we request permissions
            //this is not gonna be executed if the user explicitly does not want
            //to give this permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    APP_REQUEST_PERMISSIONS);
        }


    }

    private void requestLocation() {
        if (isGpsEnabled()) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
        }else{
          new AlertDialog.Builder(this)
                           .setTitle("GPS LOCATION REQUIRED")
                           .setMessage("In order for the location to work you must enable GPS")
                           .setPositiveButton("Alright",(view,which)->finish())
                            .show();
        }
    }

    /**
     * @return - true if gps is active
     */
    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //Methods from the LocationListener
    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            currentLocation = location;

            loadingMap.setVisibility(View.INVISIBLE);
            loadingMessage.setVisibility(View.INVISIBLE);

            drawMarker();

            displayLocation();
        }
    }

    /**
     * Call the method execute() on the BackgroundTask object
     * and get the location name in the background
     */
    private void displayLocation() {
        new BackgroundTask().execute(
                new LatLng(currentLocation.getLatitude(),
                            currentLocation.getLongitude())
        );
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void drawMarker() {
            LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

            mMap.addMarker(new MarkerOptions().position(latLng)
                           .title("Current Position")
                            .visible(true)
            );
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                latLng,15);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(location);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        //the current user location
        if(currentLocation!=null) {
            currentLocation.setLatitude(latLng.latitude);
            currentLocation.setLongitude(latLng.longitude);
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(latLng));
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                    latLng,15);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(location);
            displayLocation();

        }
        }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * The background Task class is used with the Geocoder object
     * because it needs to run in the background
     * as Google suggests
     */
    class BackgroundTask extends AsyncTask<LatLng,Void,String>{



         @Override
         protected String doInBackground(LatLng... latLngs)  {
             Geocoder geocoder = new Geocoder(ChangeLocationActivity.this,Locale.getDefault());
             try {
                 List<Address> addresses = geocoder.getFromLocation(latLngs[0].latitude,latLngs[0].longitude,1);

                 if(addresses.get(0).getAddressLine(0)!=null) {
                     return addresses.get(0).getAddressLine(0);
                 }
                 else return  addresses.get(0).getFeatureName();
             } catch (IOException e) {
                 e.printStackTrace();
             }

             return null;
         }

         @Override
         protected void onPostExecute(String address) {
             super.onPostExecute(address);
             if(address!=null){
                 runOnUiThread(()->locationTextView.setText(address));
             }else {
                 Toast.makeText(ChangeLocationActivity.this, "NOT ABLE TO FIND LOCATION...", Toast.LENGTH_SHORT).show();
             }
         }
     }

    /**
     * Request the user to give his permission to use
     * some features
     * @param requestCode - return a request code based on the permission that we ask for
     * @param permissions
     * @param grantResults - we need grandResults[0],that's where the permission is stored
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case APP_REQUEST_PERMISSIONS:{
                if(grantResults .length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    requestLocation();
                }else {
                    Toast.makeText(this, "Permission has not been granted", Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
