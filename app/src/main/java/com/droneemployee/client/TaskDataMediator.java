package com.droneemployee.client;

import com.droneemployee.client.droneemployee.LatLngAlt;
import com.droneemployee.client.droneemployee.Task;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by simon on 09.06.16.
 */
public class TaskDataMediator {
    interface Observer{
        void setTaskDataMediator(TaskDataMediator taskDataMediator);
    }

    public interface ChangeCurrentTaskObserver extends Observer {
        void updateCurrentTask(Task task);
    }
    public interface AddRouteWaypointObserver extends Observer{
        void updateAddRouteWaypoint(Task task, LatLngAlt newLatLngAlt);
    }
    public interface ChangeRouteWaypointObserver extends Observer {
        void updateRouteWaypoint(Task task, int waypointIndex, LatLngAlt newLatLngAlt);
    }
    public interface LoadNewTaskObserver extends Observer {
        void updateLoadNewTask(Task task);
    }
    public interface UploadTasksObserver extends Observer {
        void updateUploadTasks();
    }

    private List<ChangeCurrentTaskObserver> changeCurrentTaskObservers;
    private List<AddRouteWaypointObserver> addRouteWaypointObservers;
    private List<ChangeRouteWaypointObserver> changeRouteWaypointObservers;
    private List<LoadNewTaskObserver> loadNewTaskObservers;
    private List<UploadTasksObserver> uploadTasksObservers;

    public TaskDataMediator(){
        changeCurrentTaskObservers = new LinkedList<>();
        addRouteWaypointObservers = new LinkedList<>();
        changeRouteWaypointObservers = new LinkedList<>();
        uploadTasksObservers = new LinkedList<>();
        loadNewTaskObservers = new LinkedList<>();
    }

    public void registerObserver(ChangeCurrentTaskObserver observer){
        changeCurrentTaskObservers.add(observer);
        observer.setTaskDataMediator(this);
    }
    public void registerObserver(AddRouteWaypointObserver observer){
        addRouteWaypointObservers.add(observer);
        observer.setTaskDataMediator(this);
    }
    public void registerObserver(ChangeRouteWaypointObserver observer){
        changeRouteWaypointObservers.add(observer);
        observer.setTaskDataMediator(this);
    }
    public void registerObserver(LoadNewTaskObserver observer){
        loadNewTaskObservers.add(observer);
        observer.setTaskDataMediator(this);
    }
    public void registerObserver(UploadTasksObserver observer){
        uploadTasksObservers.add(observer);
        observer.setTaskDataMediator(this);
    }

    public void changeCurrentTask(Task task){
        for(ChangeCurrentTaskObserver observer: changeCurrentTaskObservers){
            observer.updateCurrentTask(task);
        }
    }
    public void addRouteWaypoint(Task task, LatLngAlt waypoint){
        for (AddRouteWaypointObserver observer : addRouteWaypointObservers) {
            observer.updateAddRouteWaypoint(task, waypoint);
        }
    }
    public void changeRouteWaypoint(Task task, int waypointIndex, LatLngAlt newLatLngAlt){
        for (ChangeRouteWaypointObserver observer : changeRouteWaypointObservers) {
            observer.updateRouteWaypoint(task, waypointIndex, newLatLngAlt);
        }
    }
    public void loadNewTask(Task task){
        for (LoadNewTaskObserver observer : loadNewTaskObservers) {
            observer.updateLoadNewTask(task);
        }
    }
    public void uploadTasks(){
        for (UploadTasksObserver observer : uploadTasksObservers) {
            observer.updateUploadTasks();
        }
    }
}