package com.simon.droneemployeeclient.droneflat;

import java.util.ArrayList;

/**
 * Created by simon on 06.06.16.
 */
public class DroneList extends ArrayList<Drone> {
    public ArrayList<String> getAllId(){
        ArrayList<String> allId = new ArrayList<String>();
        for(Drone drone: this){
            allId.add(drone.getAddress());
        }
        return allId;
    }
};
