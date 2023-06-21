package com.quocanproject.todolistroom;


import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "tasklist_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nameTask;
    private String typeTask;
    private boolean statusTask;

    private String dueDate;


    public Task(String nameTask, String typeTask, boolean statusTask, String dueDate) {
        this.nameTask = nameTask;
        this.typeTask = typeTask;
        this.statusTask = statusTask;
        this.dueDate = dueDate;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", nameTask='" + nameTask + '\'' +
                ", typeTask='" + typeTask + '\'' +
                ", statusTask=" + statusTask +
                ", dueDate='" + dueDate + '\'' +
                '}';
    }
}
