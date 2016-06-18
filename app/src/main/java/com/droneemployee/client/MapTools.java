package com.droneemployee.client;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.droneemployee.client.common.Coordinate;
import com.droneemployee.client.common.Task;

import java.util.HashMap;
import java.util.List;

/**
 * Created by simon on 06.06.16.
 */
public class MapTools implements OnMapReadyCallback, SwitchButton.OnSwitchListener {

    public MapTools(SupportMapFragment mapFragment) {
        Log.i(LOGNAME, "IN MapTools()");
        mapFragment.getMapAsync(this);
        mCurrentTask = null;
        mTaskPolylineMap = new TaskPolylineMap();
    }

    public void setCurrentTask(Task task){
        if(mCurrentTask != null){
            mTaskPolylineMap.get(mCurrentTask).setColor(Color.BLACK);
        }
        if(task == null){
            mCurrentTask = null;
            return;
        }
        mCurrentTask = task;
        if(!mTaskPolylineMap.containsKey(task)){
            LatLng latLng = new LatLng(task.getWaypoint(0).lat, task.getWaypoint(0).lon);
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.geodesic(true);
            polylineOptions.color(Color.RED);
            polylineOptions.add(latLng);
            Polyline polyline = mMap.addPolyline(polylineOptions);
            mTaskPolylineMap.put(task, polyline);
            mMap.addMarker(new MarkerOptions().
                    position(latLng));
        } else {
            Polyline polyline = mTaskPolylineMap.get(task);
            polyline.setColor(Color.RED);
        }
    }

    public void clearRoute(){
        //TODO: ref
    }

    public GoogleMap getMap(){
        return mMap;
    }

    /*
    PolylineOptions drawRoute(Route route, boolean isActive){
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.geodesic(true);
        polylineOptions.color(isActive? Color.RED: Color.BLACK);

        for(Coordinate latLngAlt: route) {
            polylineOptions.add(new LatLng(latLngAlt.lat, latLngAlt.lon));
        }
        polylineOptions.add(new LatLng(route.get(0).lat, route.get(0).lon));

        return polylineOptions;
    }
    */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(LOGNAME, "IN onMapReady");
        this.mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        LatLng spb = new LatLng(59.9024, 30.2622);
        //LatLng spb2 = new LatLng(59.9013, 30.2613);
        //mMap.addMarker(new MarkerOptions().position(spb).title("Marker in SPb"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spb, 15));

        /*
        PolylineOptions polylineOptions = new PolylineOptions().
                geodesic(true).
                color(Color.RED).
                add(spb).
                add(spb2);
        Polyline polyline = mMap.addPolyline(polylineOptions);
        polyline.setColor(Color.BLUE);
        */
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
                            new Coordinate(latLng.latitude, latLng.longitude, 20));

                    Polyline polyline = mTaskPolylineMap.get(mCurrentTask);
                    List<LatLng> points = polyline.getPoints();
                    points.add(latLng);
                    polyline.setPoints(points);
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
    private TaskPolylineMap mTaskPolylineMap;
}

class TaskPolylineMap extends HashMap<Task, Polyline> {}