package com.droneemployee.client.droneemployee;

/**
 * Created by simon on 06.06.16.
 */
public class Task {
    public Task(Ticket ticket){
        mDrone = ticket.getDrone();
        mRoute = new Route();
    }

    public void addWaypoint(LatLngAlt waypoint){
        mRoute.add(waypoint);
    }

    public LatLngAlt getWaypoint(int i){
        return mRoute.get(i);
    }

    @Override
    public String toString(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Task(");
        stringBuffer.append("drone=" + mDrone);
        stringBuffer.append(", route=" + mRoute);
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    private Drone mDrone;
    private Route mRoute;
}
