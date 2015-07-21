package com.app.larissag.epiclist.Model;


import java.io.Serializable;

public class TaskSerializable implements Serializable {
    private Task task;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
