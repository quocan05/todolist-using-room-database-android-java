package com.quocanproject.todolistroom.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.quocanproject.todolistroom.Task;

import java.util.List;

@Database(
        entities = Task.class,
        version =2
)
public abstract class TaskDatabase extends RoomDatabase {

    private static final String DB_NAME = "task_db";

    public abstract TaskDAO taskDAO();

    public static TaskDatabase instance;

    public static synchronized TaskDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),TaskDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
