package com.quocanproject.todolistroom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepo taskRepo;
    private LiveData<List<Task>> listTask;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepo = new TaskRepo(application);
        listTask = taskRepo.getAllData();
    }

    LiveData<List<Task>> getAllTask() { return listTask; }

    public void insert(Task task) { taskRepo.insertData(task); }

}
