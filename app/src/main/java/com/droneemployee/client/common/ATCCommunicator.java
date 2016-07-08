package com.droneemployee.client.common;

/**
 * Created by simon on 19.06.16.
 */
public abstract class ATCCommunicator {
    public abstract DroneATC fetchDroneAtc();
    public abstract Ticket buyTicket(Drone drone);
    public abstract void sendTask(Task task);
}
