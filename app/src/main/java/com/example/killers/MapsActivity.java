package com.example.killers;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng>arrayList=new ArrayList<LatLng>();
    LatLng astana = new LatLng(51.128352, 71.430454);
    LatLng police7 = new LatLng(51.152642, 71.462895);
    LatLng police5 = new LatLng(51.152621, 71.462621);
    LatLng MVD_RK = new LatLng(51.152329, 71.454791);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        arrayList.add(police5);
        arrayList.add(police7);
        arrayList.add(MVD_RK);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i=0;i<arrayList.size();i++){
            mMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title("Police")); //без этого
            mMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i))); // и этого
            mMap.addMarker(new MarkerOptions().position(astana).title("Welcome to Nur-Sultan")); //для каждого участка сделать отдельную строку с названием
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(astana, 18), 5000, null);


        }
    }
}