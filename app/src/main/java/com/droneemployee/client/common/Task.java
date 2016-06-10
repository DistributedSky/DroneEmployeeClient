package com.droneemployee.client.common;

/**
 * Created by simon on 06.06.16.
 */
public class Task {
    private Drone drone;
    private Route route;

    public Task(Ticket ticket){
        drone = ticket.getDrone();
        route = new Route();
        route.add(drone.getLastPosition());
    }

    public void addWaypoint(LatLngAlt waypoint){
        route.add(waypoint);
    }

    public LatLngAlt getWaypoint(int i){
        return route.get(i);
    }

    public void setWaypoint(int i, LatLngAlt latLngAlt){
        route.set(i, latLngAlt);
    }

    public int size(){
        return route.size();
    }

    public String getDroneAdress() {
        return drone.getAddress();
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
}
