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
import com.droneemployee.client.common.LatLngAlt;
import com.droneemployee.client.common.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by simon on 06.06.16.
 */
public class MapTools implements
        OnMapReadyCallback,
        SharedTaskList.Observer,
        SharedTaskIndex.Observer,
        SwitchButton.OnSwitchListener
{
    private static String LOGNAME = "MapTools";

    private GoogleMap map;
    private ArrayList<Polyline> polylinesForTasks;
    private int currentTaskIndex;

    private SharedTaskList sharedTaskList;
    private SharedTaskIndex sharedTaskIndex;

    public MapTools(SupportMapFragment mapFragment) {
        Log.i(LOGNAME, "IN MapTools()");
        mapFragment.getMapAsync(this);
        this.polylinesForTasks = new ArrayList<>();
        this.currentTaskIndex = SharedTaskIndex.NOTSET;
    }

    public GoogleMap getMap(){
        return map;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(LOGNAME, "IN onMapReady");
        this.map = googleMap;
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setMapToolbarEnabled(false);
        LatLng spb = new LatLng(59.9024, 30.2622);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(spb, 15));
    }

    @Override
    public void setSharedTaskList(SharedTaskList sharedTaskList) {
        this.sharedTaskList = sharedTaskList;
    }

    @Override
    public void updateAddWaypoint(int taskIndex, LatLngAlt newLatLngAlt) {
        Log.i(LOGNAME, "IN updateAddWaypoint");
        Polyline polyline = polylinesForTasks.get(taskIndex);
        List<LatLng> points = polyline.getPoints();
        points.add(new LatLng(newLatLngAlt.lat, newLatLngAlt.lon));
        polyline.setPoints(points);
    }

    @Override
    public void updateWaypoint(int taskIndex, int waypointIndex, LatLngAlt newLatLngAlt) {
        Log.i(LOGNAME, "IN updateRouteWaypoint: taskIndex: " + taskIndex +
                " waypointIndex: " + waypointIndex + " newLatLngAlt: " + newLatLngAlt);
    }

    @Override
    public void updateLoadTask(Task task) {
        LatLng latLng = new LatLng(task.getWaypoint(0).lat, task.getWaypoint(0).lon);
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.geodesic(true);
        polylineOptions.color(Color.BLACK);
        polylineOptions.add(latLng);
        Polyline polyline = map.addPolyline(polylineOptions);
        polylinesForTasks.add(polyline);

        map.addMarker(new MarkerOptions()
            .position(latLng)
            .title(task.getDroneAdress()));
    }

    @Override
    public void updateUploadTasks() {
        for (Polyline polyline : polylinesForTasks) {
            polyline.remove();
        }
        polylinesForTasks.clear();
    }

    @Override
    public void setSharedCurrentTask(SharedTaskIndex sharedTaskIndex) {
        this.sharedTaskIndex = sharedTaskIndex;
    }

    @Override
    public void updateCurrentTask(int taskIndex) {
        Log.i(LOGNAME, "In updateCurrentTask: taskIndex: " + taskIndex);
        if(currentTaskIndex != SharedTaskIndex.NOTSET){
            polylinesForTasks.get(currentTaskIndex).setColor(Color.BLACK);
        }
        currentTaskIndex = taskIndex;
        if(taskIndex == SharedTaskIndex.NOTSET){
            return;
        }
        Polyline polyline = polylinesForTasks.get(currentTaskIndex);
        polyline.setColor(Color.RED);
    }

    @Override
    public void switchOn() {
        Log.i(LOGNAME, "switchOn");

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //TODO: this method
                Log.i(LOGNAME, String.valueOf(latLng));
                if(currentTaskIndex != SharedTaskIndex.NOTSET){
                    LatLngAlt waypoint = new LatLngAlt(latLng.latitude, latLng.longitude, 20);
                    Log.i(LOGNAME, "IN onMapClick: waypoint: " + waypoint);
                    sharedTaskList.addWaypoint(currentTaskIndex, waypoint);
                }
            }
        });
    }

    @Override
    public void switchOff() {
        Log.i(LOGNAME, "switchOff");

        map.setOnMapClickListener(null);
    }

}

class TaskPolylineMap extends HashMap<Task, Polyline> {}