package com.droneemployee.client.common;

import java.util.ArrayList;

/**
 * Created by simon on 06.06.16.
 */
//TODO: refactor this class; remove repeat info
public class Task {
    private Drone drone;
    private String droneAdress;
    private String ticketId;
    private ArrayList<Coordinate> route = new ArrayList<>();

    public Task(Ticket ticket){
        Drone drone = ticket.getDrone();
        this.drone = drone;
        this.droneAdress = drone.getAddress();
        this.ticketId = ticket.getId();

        route.add(drone.getLastPosition());
    }

    public void addWaypoint(Coordinate waypoint){
        route.add(waypoint);
    }

    public Coordinate getWaypoint(int i){
        return route.get(i);
    }

    public Coordinate setWaypoint(int i, Coordinate coordinate){
        return route.set(i, coordinate);
    }

    public int size() {
        return route.size();
    }

    @Override
    public String toString(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Task(");
        stringBuffer.append("drone=" + drone);
        stringBuffer.append(", route=" + route);
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    public String getDroneAdress() {
        return droneAdress;
    }
}
