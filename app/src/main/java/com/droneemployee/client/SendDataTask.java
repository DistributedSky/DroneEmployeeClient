package com.droneemployee.client;

import com.droneemployee.client.common.Task;

import java.util.List;

/**
 * Created by simon on 15.07.16.
 */
public class SendDataTask extends LockUiAsyncTask <List<Task>, Void> {
    public SendDataTask(final MainActivity activity) {
        super(activity, "Sending task",
                new LockUiAsyncTask.Execute<List<Task>, Void>() {
                    @Override
                    public Void execute(List<Task> tasks) {
                        activity.getAtcCommunicator().sendTasks(tasks);
                        return null;
                    }
                });
    }
}
