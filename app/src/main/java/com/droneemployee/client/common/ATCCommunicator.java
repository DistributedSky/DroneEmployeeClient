package com.droneemployee.client.common;

import java.util.List;

/**
 * Created by simon on 19.06.16.
 */
public abstract class ATCCommunicator {
    public abstract DroneATC fetchDroneAtc(Coordinate currentLocation);
    public abstract Ticket buyTicket(Drone drone);
    public abstract boolean sendTasks(List<Task> tasks);
}
