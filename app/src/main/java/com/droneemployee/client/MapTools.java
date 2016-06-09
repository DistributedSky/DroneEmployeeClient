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
import com.droneemployee.client.droneemployee.LatLngAlt;
import com.droneemployee.client.droneemployee.Task;

import java.util.HashMap;
import java.util.List;

/**
 * Created by simon on 06.06.16.
 */
public class MapTools implements
        OnMapReadyCallback,
        SwitchButton.OnSwitchListener,
        TaskDataMediator.ChangeCurrentTaskObserver,
        TaskDataMediator.AddRouteWaypointObserver,
        TaskDataMediator.ChangeRouteWaypointObserver,
        TaskDataMediator.LoadNewTaskObserver,
        TaskDataMediator.UploadTasksObserver
{
    private static String LOGNAME = "MapTools";

    private GoogleMap map;
    private Task currentTask;
    private TaskPolylineMap taskPolylineMap;

    private TaskDataMediator taskDataMediator;

    public MapTools(SupportMapFragment mapFragment) {
        Log.i(LOGNAME, "IN MapTools()");
        mapFragment.getMapAsync(this);
        currentTask = null;
        taskPolylineMap = new TaskPolylineMap();
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
    public void switchOn() {
        Log.i(LOGNAME, "switchOn");

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //TODO: this method
                Log.i(LOGNAME, String.valueOf(latLng));
                if(currentTask != null){
                    LatLngAlt waypoint = new LatLngAlt(latLng.latitude, latLng.longitude, 20);
                    Log.i(LOGNAME, "IN onMapClick: waypoint: " + waypoint);
                    taskDataMediator.addRouteWaypoint(currentTask, waypoint);
                }
            }
        });
    }

    @Override
    public void switchOff() {
        Log.i(LOGNAME, "switchOff");

        map.setOnMapClickListener(null);
    }

    @Override
    public void updateCurrentTask(Task task) {
        Log.i(LOGNAME, "In updateCurrentTask: task: " + task);
        if(currentTask != null){
            taskPolylineMap.get(currentTask).setColor(Color.BLACK);
        }
        if(task == null){
            currentTask = null;
            return;
        }
        currentTask = task;
        Polyline polyline = taskPolylineMap.get(task);
        polyline.setColor(Color.RED);
    }

    @Override
    public void updateAddRouteWaypoint(Task task, LatLngAlt newLatLngAlt){
        //TODO: this method
        Log.i(LOGNAME, "IN updateAddRouteWaypoint");
        Polyline polyline = taskPolylineMap.get(task);
        List<LatLng> points = polyline.getPoints();
        points.add(new LatLng(newLatLngAlt.lat, newLatLngAlt.lon));
        polyline.setPoints(points);
    }

    public void setTaskDataMediator(TaskDataMediator taskDataMediator) {
        this.taskDataMediator = taskDataMediator;
    }

    @Override
    public void updateRouteWaypoint(Task task, int waypointIndex, LatLngAlt newLatLngAlt) {
        Log.i(LOGNAME, "IN updateRouteWaypoint: task: " + task +
                " latLngAlt: " + waypointIndex + " newLatLngAlt: " + newLatLngAlt);
    }

    @Override
    public void updateLoadNewTask(Task task) {
        LatLng latLng = new LatLng(task.getWaypoint(0).lat, task.getWaypoint(0).lon);
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.geodesic(true);
        polylineOptions.color(Color.BLACK);
        polylineOptions.add(latLng);
        Polyline polyline = map.addPolyline(polylineOptions);
        taskPolylineMap.put(task, polyline);
        map.addMarker(new MarkerOptions().position(latLng));
    }

    @Override
    public void updateUploadTasks() {
        for (Polyline polyline : taskPolylineMap.values()) {
            polyline.remove();
        }
        taskPolylineMap = new TaskPolylineMap();
    }
}

class TaskPolylineMap extends HashMap<Task, Polyline> {}