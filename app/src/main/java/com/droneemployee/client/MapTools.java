package com.droneemployee.client;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;

import com.droneemployee.client.common.Coordinate;
import com.droneemployee.client.common.Task;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 06.06.16.
 */
public class MapTools implements
        OnMapReadyCallback,
        SharedTaskIndex.Observer,
        SwitchButton.OnSwitchListener
{
    private class Record {
        public Task task;
        public Polyline polyline;
        public Marker marker;
    }

    private static String TAG = "MapTools";

    private GoogleMap map;
    private ArrayList<Record> records = new ArrayList<>();
    private Polygon perimeter;
    private int currentTaskIndex;

    private SharedTaskIndex sharedTaskIndex;

    public MapTools(SupportMapFragment mapFragment) {
        Log.i(TAG, "IN MapTools()");
        mapFragment.getMapAsync(this);
        this.currentTaskIndex = SharedTaskIndex.NOTSET;
    }

    public void renderPoligon(List<Coordinate> coordinates) {
        PolygonOptions polygonOptions = new PolygonOptions();
        for (Coordinate coordinate : coordinates) {
            LatLng latLng = new LatLng(coordinate.lat, coordinate.lng);
            polygonOptions.add(latLng);
        }
        polygonOptions
                .fillColor(Color.argb(0x20, 0x00, 0x00, 0xff))
                .strokeColor(Color.argb(0xff, 0x00, 0x00, 0x60));
        perimeter = map.addPolygon(polygonOptions);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "IN onMapReady");
        this.map = googleMap;
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setMapToolbarEnabled(false);
        LatLng spb = new LatLng(59.9024, 30.2622);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(spb, 15));
    }

    public void loadTask(Task task) {
        Log.i(TAG, "IN MapTools.updateLoadTask(): task id = " + task.hashCode());
        LatLng latLng = new LatLng(task.getWaypoint(0).lat, task.getWaypoint(0).lng);
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.geodesic(true);
        polylineOptions.color(Color.BLACK);
        polylineOptions.add(latLng);
        Polyline polyline = map.addPolyline(polylineOptions);

        Marker marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(task.getDroneAdress()));

        Record record = new Record();
        record.task = task;
        record.polyline = polyline;
        record.marker = marker;

        records.add(record);
    }

    @NonNull
    public List<Task> uploadTasks() {
        Log.i(TAG, "In uploadTasks");

        ArrayList<Task> tasks = new ArrayList<>();

        for (Record record : records) {
            Task task = record.task;
            for (LatLng latLng : record.polyline.getPoints()) {
                //TODO: modify the waypoint altitude change
                task.addWaypoint(new Coordinate(latLng.latitude, latLng.longitude, 20));
            }
            tasks.add(task);

            record.polyline.remove();
            record.marker.remove();
        }
        records.clear();
        return tasks;
    }

    @Override
    public void setSharedCurrentTask(SharedTaskIndex sharedTaskIndex) {
        this.sharedTaskIndex = sharedTaskIndex;
    }

    @Override
    public void updateCurrentTask(int taskIndex) {
        Log.i(TAG, "In updateCurrentTask: taskIndex: " + taskIndex);
        if(currentTaskIndex != SharedTaskIndex.NOTSET){
            records.get(currentTaskIndex).polyline.setColor(Color.BLACK);
        }
        currentTaskIndex = taskIndex;
        if(taskIndex == SharedTaskIndex.NOTSET){
            return;
        }
        records.get(currentTaskIndex).polyline.setColor(Color.RED);
    }

    @Override
    public void switchOn() {
        Log.i(TAG, "switchOn");

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i(TAG, String.valueOf(latLng));
                if(currentTaskIndex != SharedTaskIndex.NOTSET){
                    Coordinate waypoint = new Coordinate(latLng.latitude, latLng.longitude, 20);
                    Log.i(TAG, "IN onMapClick: waypoint: " + waypoint);
                    Polyline polyline = records.get(currentTaskIndex).polyline;
                    List<LatLng> points = polyline.getPoints();
                    points.add(new LatLng(waypoint.lat, waypoint.lng));
                    polyline.setPoints(points);
                }
            }
        });
    }

    @Override
    public void switchOff() {
        Log.i(TAG, "switchOff");

        map.setOnMapClickListener(null);
    }
}