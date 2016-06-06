package com.simon.droneemployeeclient;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.simon.droneemployeeclient.droneflat.Drone;
import com.simon.droneemployeeclient.droneflat.LatLngAlt;
import com.simon.droneemployeeclient.droneflat.SwitchButton;
import com.simon.droneemployeeclient.droneflat.Task;

/**
 * Created by simon on 06.06.16.
 */
public class MapTools implements OnMapReadyCallback, SwitchButton.OnSwitchListener {
    MapTools(SupportMapFragment mapFragment) {
        Log.i(LOGNAME, "IN MapTools()");
        mapFragment.getMapAsync(this);
        mCurrentTask = null;
    }

    public void setCurrentTask(Task task){
        mCurrentTask = task;
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
        //mMap.addMarker(new MarkerOptions().position(spb).title("Marker in SPb"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spb, 15));
    }

    @Override
    public void switchOn() {
        Log.i(LOGNAME, "switchOn");

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i(LOGNAME, String.valueOf(latLng));
                if(mCurrentTask != null){
                    mCurrentTask.addWaypoint(
                            new LatLngAlt(latLng.latitude, latLng.longitude, 10));
                }
            }
        });
    }

    @Override
    public void switchOff() {
        Log.i(LOGNAME, "switchOff");

        mMap.setOnMapClickListener(null);
    }

    private static String LOGNAME = "MapTools";
    private GoogleMap mMap;
    private Task mCurrentTask;
}