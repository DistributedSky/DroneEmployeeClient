package com.droneemployee.client;


import com.droneemployee.client.common.Coordinate;
import com.droneemployee.client.common.Drone;
import com.droneemployee.client.common.FakeATCCommunicator;
import com.droneemployee.client.common.DroneATC;
import com.droneemployee.client.common.Task;

import java.util.ArrayList;
import java.util.List;


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
        FakeATCCommunicator deb = new FakeATCCommunicator();
        DroneATC droneATC = deb.fetchDroneAtc(new Coordinate(123, 987));
        System.out.println(droneATC);

        Drone drone = droneATC.getDrones().get(2);
        Task task = new Task(deb.buyTicket(drone));
        task.addWaypoint(new Coordinate(59.905653, 30.259567, 10));
        task.addWaypoint(new Coordinate(59.901743, 30.258366, 10));

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task);

        deb.sendTasks(tasks);
    }

    @org.junit.Test
    public void taskDataTest() throws Exception {
        FakeATCCommunicator deb = new FakeATCCommunicator();
        DroneATC droneBase = deb.fetchDroneAtc(new Coordinate(123, 987));
        System.out.println(droneBase);

        final List<Task> taskList = new ArrayList<>();

        for (Drone drone : droneBase.getDrones()) {
            Task task = new Task(deb.buyTicket(drone));
            task.addWaypoint(new Coordinate(59.905653, 30.259567, 10));
            task.addWaypoint(new Coordinate(59.901743, 30.258366, 10));
            taskList.add(task);
        }
    }
}