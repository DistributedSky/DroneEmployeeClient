package com.droneemployee.client;

import com.droneemployee.client.common.LatLngAlt;
import com.droneemployee.client.common.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by simon on 10.06.16.
 */
public class SharedTaskList {
    public interface Observer{
        void setSharedTaskList(SharedTaskList sharedTaskList);
        void updateAddWaypoint(int taskIndex, LatLngAlt newLatLngAlt);
        void updateWaypoint(int taskIndex, int waypointIndex, LatLngAlt newLatLngAlt);
        void updateLoadTask(Task task);
        void updateUploadTasks();
    }

    private TaskList tasks;
    private List<Observer> observers;

    public SharedTaskList(){
        this.tasks = new TaskList();
        this.observers = new LinkedList<>();
    }

    public void addObserver(Observer observer){
        observer.setSharedTaskList(this);
        observers.add(observer);
    }

    public void addWaypoint(int taskIndex, LatLngAlt waypoint){
        for (Observer observer : observers) {
            observer.updateAddWaypoint(taskIndex, waypoint);
        }
    }
    public void changeWaypoint(int taskIndex, int waypointIndex, LatLngAlt newLatLngAlt){
        for (Observer observer : observers) {
            tasks.get(taskIndex).setWaypoint(waypointIndex, newLatLngAlt);
            observer.updateWaypoint(taskIndex, waypointIndex, newLatLngAlt);
        }
    }
    public void loadTask(Task task){
        for (Observer observer : observers) {
            tasks.add(task);
            observer.updateLoadTask(task);
        }
    }
    public void uploadTasks(){
        for (Observer observer : observers) {
            tasks.clear();
            observer.updateUploadTasks();
        }
    }
}

class TaskList extends ArrayList<Task> {}