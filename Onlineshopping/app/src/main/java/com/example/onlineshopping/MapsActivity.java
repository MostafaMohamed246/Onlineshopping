package com.example.onlineshopping;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Geocoder geocoder;
    String Address_user;
    Button confirm_your_location;
    Bundle bundle;
int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        bundle = getIntent().getExtras();
        id =  bundle.getInt("customerid");

        mapFragment.getMapAsync(this);
        confirm_your_location = findViewById(R.id.button12);
        confirm_your_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, OrderCartActivity.class);
                intent.putExtra("Address",Address_user);
                intent.putExtra("c_id",id);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(this, Locale.getDefault());

        locationListener = new LocationListener() {
            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onLocationChanged(@NonNull Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
           // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            //Location lastKnownLocation =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng userLatLng = new LatLng(30.04441960,31.235711600);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLatLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
        }
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String address = "";
                mMap.clear();
                try {
                    List<Address>addressList=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    if(addressList != null && addressList.size() > 0){
                        address ="Address: \n";

                        if(addressList.get(0).getSubThoroughfare() != null){
                            address += addressList.get(0).getSubThoroughfare()+ " ";
                        }
                        if(addressList.get(0).getThoroughfare() != null){
                            address += addressList.get(0).getThoroughfare()+"\n";
                        }
                        if(addressList.get(0).getLocality() != null){
                            address += addressList.get(0).getLocality()+"\n";
                        }
                        if(addressList.get(0).getPostalCode() != null){
                            address = addressList.get(0).getPostalCode()+"\n";
                        }
                        if(addressList.get(0).getCountryName() != null){
                            address = addressList.get(0).getCountryName()+"\n";
                        }
                        Address_user = address;
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

    }
    @Override
    public void onRequestPermissionsResult (int requestCode , @NonNull String [] permissions ,@NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }
}