package com.droneemployee.client;

import com.droneemployee.client.droneemployee.Coordinate;
import com.droneemployee.client.droneemployee.Drone;
import com.droneemployee.client.droneemployee.DroneEmployeeFetcher;
import com.droneemployee.client.droneemployee.DroneATC;
import com.droneemployee.client.droneemployee.Task;

/**
 * To work on unit tests, switch the LibraryDroneTest Artifact in the Build Variants view.
 */
public class LibraryDroneTest {
    @org.junit.Test
    public void addition_isCorrect() throws Exception {
        Coordinate coordinate = new Coordinate(10, 20, 30);
        System.out.println("coordinate: " + coordinate);

        Drone drone = new Drone("asdf", coordinate, Drone.State.AVAILABLE);
        System.out.println(drone);
    }

    @org.junit.Test
    public void droneEmployeeBaseTest() throws Exception {
        DroneEmployeeFetcher deb = new DroneEmployeeFetcher();
        DroneATC droneATC = deb.fetchData();
        System.out.println(droneATC);

        Drone drone = droneATC.getDrones().get(2);
        Task task = new Task(deb.byTicket(drone));
        task.addWaypoint(new Coordinate(59.905653, 30.259567, 10));
        task.addWaypoint(new Coordinate(59.901743, 30.258366, 10));

        deb.sendTask(task);
    }
}