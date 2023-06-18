package com.quocanproject.todolistroom;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasklist")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;


    private String nameTask;
    private String typeTask;
    private boolean statusTask;

    public Task(String nameTask, String typeTask, boolean statusTask) {
        this.nameTask = nameTask;
        this.typeTask = typeTask;
        this.statusTask = statusTask;
    }

    public boolean isStatusTask() {
        return statusTask;
    }

    public void setStatusTask(boolean statusTask) {
        this.statusTask = statusTask;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getTypeTask() {
        return typeTask;
    }

    public void setTypeTask(String typeTask) {
        this.typeTask = typeTask;
    }

    @Override
    public String toString() {
        return "Task :" +
                "id=" + id +
                ", Name ='" + nameTask + '\'' +
                ", Type ='" + typeTask + '\'' +
                ", Status =" + statusTask ;
    }
}
