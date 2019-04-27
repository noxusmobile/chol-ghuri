package com.example.cholghuri;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import retrofit2.http.Url;

public class LocationMap extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private double latitude,longitude;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private ImageButton schoolsIB,hospitalsIB,restaurantsIB;
    private int ProximityRadious=10000;
    private static final int Request_User_Location_Code =99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);
        schoolsIB=findViewById(R.id.schoolsIB);
        hospitalsIB=findViewById(R.id.hospitalsIB);
        restaurantsIB=findViewById(R.id.restaurantsIB);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }

        geocoder = new Geocoder(LocationMap.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("MapActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapActivity", "Can't find style. Error: ", e);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
//        LatLng sydney = new LatLng(23.751157, 90.393499);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Dhaka").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    public boolean checkUserLocationPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            return false;
        }
        else{
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Request_User_Location_Code:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

                        if(googleApiClient == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        lastLocation=location;
        if(currentUserLocationMarker!=null){
            currentUserLocationMarker.remove();
        }
        LatLng latLng= new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions= new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("User current location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentUserLocationMarker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(14));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if(googleApiClient!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        }



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void searchLocation(View view) {
        String hospital="hospital", school= "school", restaurant="restaurant";
        Object transferData[]= new Object[2];
        GetNearbyPlaces getNearbyPlaces= new GetNearbyPlaces();




        switch (view.getId()){
            case R.id.searchLocationIB:
                EditText addressField= (EditText)findViewById(R.id.searchLocationET);
                String address=addressField.getText().toString();

                List<Address> addressList = null;
                MarkerOptions userMarkerOptions= new MarkerOptions();
                if(!TextUtils.isEmpty(address)){
                    Geocoder geocoder =new Geocoder(this);
                    try {
                        addressList=geocoder.getFromLocationName(address,6);
                        if(addressList!=null){
                            for(int i=0;i<addressList.size();i++){
                                Address userAddress= addressList.get(i);
                                LatLng latLng= new LatLng(userAddress.getLatitude(),userAddress.getLongitude());
                                //mMap.clear();

                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                currentUserLocationMarker=mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

                            }

                        }
                        else{
                            Toast.makeText(this, "Location not found ..", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(this, "Please write any location name", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.hospitalsIB:
                mMap.clear();
                String url =getUrl(latitude,longitude,hospital);
                transferData[0]=mMap;
                transferData[1]=url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Hospital", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Hospital..", Toast.LENGTH_SHORT).show();

                break;

            case R.id.schoolsIB:
                mMap.clear();
                String url2 =getUrl(latitude,longitude,school);
                transferData[0]=mMap;
                transferData[1]=url2;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Schools", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Schools..", Toast.LENGTH_SHORT).show();

                break;

            case R.id.restaurantsIB:
                mMap.clear();
                String url3 =getUrl(latitude,longitude,restaurant);
                transferData[0]=mMap;
                transferData[1]=url3;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Restaurants", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Restaurants..", Toast.LENGTH_SHORT).show();

                break;
        }
    }
    private String getUrl(double latitude,double longitude,String nearbyPlace){
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?");
        googleURL.append("location=" +latitude+ ","+longitude);
        googleURL.append("&radious=" +ProximityRadious);
        googleURL.append("&type="+nearbyPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key="+"AIzaSyB9rSETXdUiSqp6-y_3CY64B35obpMY9ew");

        Log.d("LocationMap","url = " +googleURL.toString());


        return googleURL.toString();



    }
}

