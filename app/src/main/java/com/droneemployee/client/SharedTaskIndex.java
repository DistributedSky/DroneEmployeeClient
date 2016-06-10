package com.droneemployee.client;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by simon on 10.06.16.
 */
public class SharedTaskIndex {
    public final static int NOTSET = -1;

    public interface Observer{
        void setSharedCurrentTask(SharedTaskIndex sharedTaskIndex);
        void updateCurrentTask(int taskIndex);
    }

    private int taskIndex;
    private List<Observer> observers;

    public SharedTaskIndex(){
        taskIndex = NOTSET;
        this.observers = new LinkedList<>();
    }

    public void addObserver(Observer observer){
        observer.setSharedCurrentTask(this);
        observers.add(observer);
    }

    public void updateCurrentTask(int taskIndex){
        this.taskIndex = taskIndex;
        for (Observer observer : observers) {
            observer.updateCurrentTask(taskIndex);
        }
    }
}
