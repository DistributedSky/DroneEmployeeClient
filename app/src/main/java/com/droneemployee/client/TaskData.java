package com.droneemployee.client;

import android.annotation.SuppressLint;
import android.util.Log;

import com.droneemployee.client.droneemployee.DroneEmployeeBase;
import com.droneemployee.client.droneemployee.LatLngAlt;
import com.droneemployee.client.droneemployee.Task;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by simon on 09.06.16.
 */
public class TaskData implements
        TaskDataMediator.AddRouteWaypointObserver,
        TaskDataMediator.ChangeRouteWaypointObserver,
        TaskDataMediator.LoadNewTaskObserver,
        TaskDataMediator.UploadTasksObserver
{
    static final private String LOGNAME = "TaskData";

    private List<Task> taskList;
    private DroneEmployeeBase droneEmployeeBase;

    TaskData(DroneEmployeeBase droneEmployeeBase){
        taskList = new LinkedList<>();
        this.droneEmployeeBase = droneEmployeeBase;
    }

    @Override
    public void updateAddRouteWaypoint(Task task, LatLngAlt newLatLngAlt) {
        task.addWaypoint(newLatLngAlt);
    }

    @SuppressLint("Assert")
    @Override
    public void updateRouteWaypoint(Task task, int waypointIndex, LatLngAlt newLatLngAlt) {
        int taskSize = task.size();

        if(taskSize > waypointIndex) {
            task.setWaypoint(waypointIndex, newLatLngAlt);
        } else {
            Log.w(LOGNAME, "ERROR in updateRouteWaypoint");
        }
    }

    @Override
    public void updateLoadNewTask(Task task) {
        taskList.add(task);
    }

    @Override
    public void updateUploadTasks() {
        for (Task task : taskList) {
            droneEmployeeBase.sendTask(task);
        }
    }

    @Override
    public void setTaskDataMediator(TaskDataMediator taskDataMediator) {}
}
