package com.simon.droneemployeeclient;

import com.simon.droneemployeeclient.droneemployee.Drone;
import com.simon.droneemployeeclient.droneemployee.DroneList;
import com.simon.droneemployeeclient.droneemployee.DroneEmployeeBase;
import com.simon.droneemployeeclient.droneemployee.LatLngAlt;
import com.simon.droneemployeeclient.droneemployee.Task;

/**
 * To work on unit tests, switch the LibraryDroneTest Artifact in the Build Variants view.
 */
public class LibraryDroneTest {
    @org.junit.Test
    public void addition_isCorrect() throws Exception {
        LatLngAlt latLngAlt = new LatLngAlt(10, 20, 30);
        System.out.println("latLngAlt: " + latLngAlt);

        Drone drone = new Drone("asdf", Drone.State.AVAILABLE, latLngAlt);
        System.out.println(drone);
    }

    @org.junit.Test
    public void droneEmployeeBaseTest() throws Exception {
        DroneEmployeeBase deb = new DroneEmployeeBase();
        DroneList droneBase = deb.loadAvailableDrones();
        System.out.println(droneBase);

        Drone drone = droneBase.get(2);
        Task task = new Task(deb.byTicket(drone));
        task.addWaypoint(new LatLngAlt(59.905653, 30.259567, 10));
        task.addWaypoint(new LatLngAlt(59.901743, 30.258366, 10));

        deb.sendTask(task);
    }
}