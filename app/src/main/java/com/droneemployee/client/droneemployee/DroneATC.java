package com.droneemployee.client.droneemployee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 06.06.16.
 */
//TODO: consider how to make interface of this class better
public class DroneATC {
    private List<Drone> mDrones;
    private List<Coordinate> mPerimetr;
    private String mName;

    public DroneATC(String name){
        mDrones = new ArrayList<>();
        mPerimetr = new ArrayList<>();
        mName = name == null? "UNDEFINED": name;
    }

    public List<Drone> getDrones() {
        return mDrones;
    }

    public List<Coordinate> getPerimeter() {
        return mPerimetr;
    }

    public String getName() {
        return mName;
    }

    public List<String> getDronesIds(){
        ArrayList<String> allId = new ArrayList<>();
        for(Drone drone: mDrones){
            allId.add(drone.getAddress());
        }
        return allId;
    }
};
