package com.droneemployee.client;

import android.graphics.Color;
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
        SharedTaskList.Observer,
        SharedTaskIndex.Observer,
        SwitchButton.OnSwitchListener
{
    private static String TAG = "MapTools";

    private GoogleMap map;
    private ArrayList<Polyline> polylinesForTasks;
    private ArrayList<Marker> markersForTasks;
    private Polygon polygon;
    private int currentTaskIndex;

    private SharedTaskList sharedTaskList;
    private SharedTaskIndex sharedTaskIndex;

    public MapTools(SupportMapFragment mapFragment) {
        Log.i(TAG, "IN MapTools()");
        mapFragment.getMapAsync(this);
        this.polylinesForTasks = new ArrayList<>();
        this.markersForTasks = new ArrayList<>();
        this.currentTaskIndex = SharedTaskIndex.NOTSET;
    }

    public void renderPoligon(List<Coordinate> coordinates) {
        PolygonOptions polygonOptions = new PolygonOptions();
        for (Coordinate coordinate : coordinates) {
            LatLng latLng = new LatLng(coordinate.lat, coordinate.lon);
            polygonOptions.add(latLng);
        }
        polygonOptions
                .fillColor(Color.argb(0x20, 0x00, 0x00, 0xff))
                .strokeColor(Color.argb(0xff, 0x00, 0x00, 0x60));
        polygon = map.addPolygon(polygonOptions);
    }

    public GoogleMap getMap(){
        return map;
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
        //map.addPolygon(polygonOptions);
    }

    @Override
    public void setSharedTaskList(SharedTaskList sharedTaskList) {
        this.sharedTaskList = sharedTaskList;
    }

    @Override
    public void updateAddWaypoint(int taskIndex, Coordinate newCoordinate) {
        Log.i(TAG, "IN updateAddWaypoint");
        Polyline polyline = polylinesForTasks.get(taskIndex);
        List<LatLng> points = polyline.getPoints();
        points.add(new LatLng(newCoordinate.lat, newCoordinate.lon));
        polyline.setPoints(points);
    }

    @Override
    public void updateWaypoint(int taskIndex, int waypointIndex, Coordinate newCoordinate) {
        Log.i(TAG, "IN updateRouteWaypoint: taskIndex: " + taskIndex +
                " waypointIndex: " + waypointIndex + " newCoordinate: " + newCoordinate);
    }

    @Override
    public void updateLoadTask(Task task) {
        Log.i(TAG, "IN MapTools.updateLoadTask(): task id = " + task.hashCode());
        LatLng latLng = new LatLng(task.getWaypoint(0).lat, task.getWaypoint(0).lon);
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.geodesic(true);
        polylineOptions.color(Color.BLACK);
        polylineOptions.add(latLng);
        Polyline polyline = map.addPolyline(polylineOptions);
        polylinesForTasks.add(polyline);

        Marker marker = map.addMarker(new MarkerOptions()
            .position(latLng)
            .title(task.getDroneAdress()));
        markersForTasks.add(marker);
    }

    @Override
    public void updateUploadTasks() {
        for (Polyline polyline : polylinesForTasks) {
            polyline.remove();
        }
        for (Marker marker : markersForTasks) {
            marker.remove();
        }
        polylinesForTasks.clear();
        currentTaskIndex = SharedTaskIndex.NOTSET;
    }

    @Override
    public void setSharedCurrentTask(SharedTaskIndex sharedTaskIndex) {
        this.sharedTaskIndex = sharedTaskIndex;
    }

    @Override
    public void updateCurrentTask(int taskIndex) {
        Log.i(TAG, "In updateCurrentTask: taskIndex: " + taskIndex);
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
        Log.i(TAG, "switchOn");

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i(TAG, String.valueOf(latLng));
                if(currentTaskIndex != SharedTaskIndex.NOTSET){
                    Coordinate waypoint = new Coordinate(latLng.latitude, latLng.longitude, 20);
                    Log.i(TAG, "IN onMapClick: waypoint: " + waypoint);
                    sharedTaskList.addWaypoint(currentTaskIndex, waypoint);

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