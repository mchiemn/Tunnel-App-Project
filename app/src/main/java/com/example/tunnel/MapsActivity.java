package com.example.tunnel;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.abs;

public class MapsActivity extends MainActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
    double tunnelLat, tunnelLng;
    EditText mSearchText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Creating text editor.
        mSearchText = (EditText) findViewById(R.id.Search_Input);
        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyA5iMjzVLhN_pUtI7VSCckINvY_7q2o_Bs");
        //Making SearchText not focused
        mSearchText.setFocusable(false);
        //Creating Intent for button
        mSearchText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Initializing Place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);
                //Creating Intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .build(MapsActivity.this);
                //Start activity result
                startActivityForResult(intent, 100);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //Initialize the place
            Place place = Autocomplete.getPlaceFromIntent(data);
            mSearchText.setText(place.getAddress());
            String searchString = mSearchText.getText().toString();
            //Create a geocoder to get the lat and long of address
            Geocoder tunnelCoder = new Geocoder(this, Locale.getDefault());
            try{
                List<Address> address = tunnelCoder.getFromLocationName(searchString, 1);
                //Calculations for the latitude and longitude for the tunnel
                tunnelLat = address.get(0).getLatitude() * -1;
                if(address.get(0).getLongitude() < 0){
                    tunnelLng = (180 - abs(address.get(0).getLongitude()));}
                else
                    tunnelLng = (address.get(0).getLongitude() - 180);
                //Set the lat and lng then create a marker
                LatLng endOfTunnel = new LatLng(tunnelLat, tunnelLng);
                mMap.addMarker(new MarkerOptions().position(endOfTunnel).title("You tunneled here!"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(endOfTunnel));
            } catch (IOException e){
                Log.e(TAG, "tunnelLocate: IOException: " + e.getMessage());
            }

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}