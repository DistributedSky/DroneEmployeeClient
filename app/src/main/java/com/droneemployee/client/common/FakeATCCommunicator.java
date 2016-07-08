package com.droneemployee.client.common;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by simon on 06.06.16.
 */
public class FakeATCCommunicator extends ATCCommunicator {
    public FakeATCCommunicator() {
        super();
    }

    @Override
    public DroneATC fetchDroneAtc(){
        DroneATC droneATC = new DroneATC("0xfba455270712957", "Current ATC");
        droneATC.getDrones().add(new Drone(genirateHexRandomString(), new Coordinate(59.903322, 30.267378, 10), Drone.State.AVAILABLE));
        droneATC.getDrones().add(new Drone(genirateHexRandomString(), new Coordinate(59.900617, 30.262357, 10), Drone.State.AVAILABLE));
        droneATC.getDrones().add(new Drone(genirateHexRandomString(), new Coordinate(59.904850, 30.258881, 10), Drone.State.AVAILABLE));

        droneATC.getPerimeter().add(new Coordinate(59.89873582670883, 30.264046601951126));
        droneATC.getPerimeter().add(new Coordinate(59.90170036428617, 30.269991047680378));
        droneATC.getPerimeter().add(new Coordinate(59.904817626983764, 30.26839211583138));
        droneATC.getPerimeter().add(new Coordinate(59.90707758975418, 30.259357094764713));
        droneATC.getPerimeter().add(new Coordinate(59.90601260377334, 30.256667844951153));
        droneATC.getPerimeter().add(new Coordinate(59.90298674767275, 30.25417808443308));
        droneATC.getPerimeter().add(new Coordinate(59.90122941209721, 30.250471271574497));
        droneATC.getPerimeter().add(new Coordinate(59.899861914715544, 30.251800306141376));
        droneATC.getPerimeter().add(new Coordinate(59.90001290883071, 30.258453860878944));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

        return droneATC;
    }

    @Override
    public Ticket buyTicket(Drone drone){
        return new Ticket(drone, genirateHexRandomString());
    }

    @Override
    public void sendTask(Task task){
        //System.out.println(TAG + ": SEND: " + task);
        Log.i(TAG, "SEND: " + task);
    }

    @NonNull
    private String genirateHexRandomString(){
        return Integer.toHexString((int) (Math.random() * 15728639 + 1048576)).toUpperCase();
    }

    private static String TAG = "FakeATCCommunicator";
}
