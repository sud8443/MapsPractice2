package com.developersudhanshu.mapspractice2;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button searchButton;
    EditText searchText;
    Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchButton = (Button) findViewById(R.id.searchButton);
        searchText = (EditText) findViewById(R.id.searchText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String destination = searchText.getText().toString();
                if(destination.isEmpty()){
                    searchText.setError("This field can't be empty");
                }else{
                    //Method which sets the location to the enter string and also sets marker at that position
                    fetchTheEnteredLocation(destination);
                }
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // Async function is used to set callback which returns when the map is ready to get loaded
        // Note: Before running the map first get the API KEY by going to the link in the file - google_maps_api.xml
        mapFragment.getMapAsync(this);
    }

    private void fetchTheEnteredLocation(String destination) {
        //Geocoder is used to convert location from latitudes and longitudes and vice versa
        Geocoder geo = new Geocoder(MapsActivity.this);
        try {
            // getting the location in the form of a an address by the Geocoder
            ArrayList<Address> dest = (ArrayList<Address>) geo.getFromLocationName(destination,1);
            Double lat,lon;
            LatLng ltln;
            if(dest != null) {
                // Getting the latitude from the address fetched by the Geocoder
                lat = dest.get(0).getLatitude();
                // Getting the longitude from the address fetched by the Geocoder
                lon = dest.get(0).getLongitude();
                ltln = new LatLng(lat,lon);
                // Checking if previously any marker is set, if yes then removing it so that only one marker stays on the map
                // to avoid any kind of confusion by the user
                if(mMarker != null)
                    mMarker.remove();
                // Setting the global marker to the destination marker so that the previously set markers can be removed
                mMarker = mMap.addMarker(new MarkerOptions().position(ltln).title(destination));
                CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(ltln,20);
                //Animate the camera movement and also setting it to zoom so that user doesn't have to zoom himself to see the place
                mMap.animateCamera(camera);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        if(mMarker!=null)
            mMarker.remove();
        mMarker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
