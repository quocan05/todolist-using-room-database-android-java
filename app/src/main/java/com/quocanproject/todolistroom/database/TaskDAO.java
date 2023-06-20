package com.quocanproject.todolistroom.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.quocanproject.todolistroom.Task;

import java.util.List;

@Dao
public interface TaskDAO {
    @Insert
    void insertTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Update
    void updateTask(Task task);

    @Query("DELETE FROM tasklist")
    void deleteAll();
    @Query("SELECT * FROM tasklist")
    LiveData<List<Task>> getListTask();

    @Query("SELECT * FROM tasklist WHERE nameTask LIKE '%' || :name || '%'")
    LiveData<List<Task>> listFromSearch(String name);
}
