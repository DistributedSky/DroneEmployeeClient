package com.simon.droneemployeeclient;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by simon on 06.06.16.
 */
public class MapTools implements OnMapReadyCallback {
    private static String LOGNAME = "MapTools";
    private GoogleMap mMap;

    MapTools(SupportMapFragment mapFragment) {
        Log.i(LOGNAME, "IN MapTools()");
        mapFragment.getMapAsync(this);
    }

    GoogleMap getMap(){
        return mMap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(LOGNAME, "IN onMapReady");
        this.mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng spb = new LatLng(59.9024, 30.2622);
        mMap.addMarker(new MarkerOptions().position(spb).title("Marker in SPb"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spb, 15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i(LOGNAME, String.valueOf(latLng));
                //mMap.setOnMapClickListener(null);
            }
        });
    }
}
