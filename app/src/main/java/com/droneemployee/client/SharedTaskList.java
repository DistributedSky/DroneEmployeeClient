package com.droneemployee.client;

import com.droneemployee.client.common.Coordinate;
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
        void updateAddWaypoint(int taskIndex, Coordinate newCoordinate);
        void updateWaypoint(int taskIndex, int waypointIndex, Coordinate newCoordinate);
        void updateLoadTask(Task task);
        void updateUploadTasks();
    }

    private List<Task> tasks;
    private List<Observer> observers;

    public SharedTaskList(){
        this.tasks = new ArrayList<>();
        this.observers = new LinkedList<>();
    }

    public void addObserver(Observer observer){
        observer.setSharedTaskList(this);
        observers.add(observer);
    }

    public void addWaypoint(int taskIndex, Coordinate waypoint){
        for (Observer observer : observers) {
            observer.updateAddWaypoint(taskIndex, waypoint);
        }
    }
    public void changeWaypoint(int taskIndex, int waypointIndex, Coordinate newCoordinate){
        tasks.get(taskIndex).setWaypoint(waypointIndex, newCoordinate);

        for (Observer observer : observers) {
            observer.updateWaypoint(taskIndex, waypointIndex, newCoordinate);
        }
    }
    public void loadTask(Task task){
        tasks.add(task);

        for (Observer observer : observers) {
            observer.updateLoadTask(task);
        }
    }
    public void uploadTasks(){
        tasks.clear();

        for (Observer observer : observers) {
            observer.updateUploadTasks();
        }
    }
}