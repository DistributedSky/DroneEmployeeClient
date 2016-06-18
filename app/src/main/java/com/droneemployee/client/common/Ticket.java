package com.droneemployee.client.common;

/**
 * Created by simon on 06.06.16.
 */
public class Ticket {
    public Ticket(Drone drone, String id){
        mDrone = drone;
        mId = id;
    }

    public Drone getDrone() {
        return mDrone;
    }

    public String getId() {
        return mId;
    }

    @Override
    public String toString(){
        return String.valueOf(this);
    }

    private Drone mDrone;
    private String mId;
}
