package com.droneemployee.client.droneemployee;

/**
 * Created by simon on 06.06.16.
 */
public class Ticket {
    private Drone drone;
    private String id;

    public Ticket(Drone drone, String id){
        this.drone = drone;
        this.id = id;
    }

    public Drone getDrone() {
        return drone;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString(){
        return String.valueOf(this);
    }
}
