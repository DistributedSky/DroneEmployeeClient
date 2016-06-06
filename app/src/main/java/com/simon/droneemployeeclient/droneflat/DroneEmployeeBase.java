package com.simon.droneemployeeclient.droneflat;

import android.util.Log;

/**
 * Created by simon on 06.06.16.
 */
public class DroneEmployeeBase {
    public DroneEmployeeBase(){}

    public DroneBase loadAvailableDrones(){
        DroneBase droneBase = new DroneBase();
        droneBase.add(new Drone(genirateHexRandomString(), Drone.State.AVAILABLE,
                new LatLngAlt(59.903322, 30.267378, 10)));
        droneBase.add(new Drone(genirateHexRandomString(), Drone.State.AVAILABLE,
                new LatLngAlt(59.900617, 30.262357, 10)));
        droneBase.add(new Drone(genirateHexRandomString(), Drone.State.AVAILABLE,
                new LatLngAlt(59.904850, 30.258881, 10)));

        return droneBase;
    }

    public Ticket byTicket(Drone drone){
        return new Ticket(drone, genirateHexRandomString());
    }

    public void sendTask(Task task){
        //System.out.println(LOG + ": SEND: " + task);
    }

    private String genirateHexRandomString(){
        return Integer.toHexString((int) (Math.random() * 15728639 + 1048576)).toUpperCase();
    }

    private static String LOG = "DroneEmployeeBase";
}
