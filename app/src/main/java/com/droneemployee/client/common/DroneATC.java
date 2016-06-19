package com.droneemployee.client.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 06.06.16.
 */
//TODO: consider how to make interface of this class better
public class DroneATC {
    private List<Drone> drones;
    private List<Coordinate> perimeter;
    private String id;
    private String name;

    public DroneATC(String id, String name){
        this.drones = new ArrayList<>();
        this.perimeter = new ArrayList<>();
        this.name = name == null? "UNDEFINED": name;
        this.id = id;
    }

    public List<Drone> getDrones() {
        return drones;
    }

    public List<Coordinate> getPerimeter() {
        return perimeter;
    }

    public String getName() {
        return name;
    }

    public List<String> getDronesIds(){
        ArrayList<String> allId = new ArrayList<>();
        for(Drone drone: drones){
            allId.add(drone.getAddress());
        }
        return allId;
    }

    @Override
    public String toString() {
        return name + " " + drones + perimeter;
    }
};
