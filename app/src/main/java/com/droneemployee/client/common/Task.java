package com.droneemployee.client.common;

/**
 * Created by simon on 06.06.16.
 */
public class Task {
    private Drone mDrone;
    private Route mRoute;
    private String mDroneAdress;

    public Task(Ticket ticket){
        mDrone = ticket.getDrone();
        mDroneAdress = ticket.getDrone().getAddress();
        mRoute = new Route();
    }

    public void addWaypoint(Coordinate waypoint){
        mRoute.add(waypoint);
    }

    public Coordinate getWaypoint(int i){
        return mRoute.get(i);
    }

    public Coordinate setWaypoint(int i, Coordinate coordinate){
        return mRoute.set(i, coordinate);
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

    public String getDroneAdress() {
        return mDroneAdress;
    }
}
