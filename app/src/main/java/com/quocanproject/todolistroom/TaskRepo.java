package com.quocanproject.todolistroom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.quocanproject.todolistroom.database.TaskDAO;
import com.quocanproject.todolistroom.database.TaskDatabase;

import java.util.List;

public class TaskRepo {
    private TaskDAO taskDAO;
    private LiveData<List<Task>> taskList;

    public TaskRepo(Application application){
        TaskDatabase taskDatabase = TaskDatabase.getInstance(application);
        taskDAO = taskDatabase.taskDAO();
        taskList = taskDAO.getListTask();
    }

    public void insertData(Task task){ new InsertTask(taskDAO).execute(task);}
    public void deleteData(Task task){ new DeleteTask(taskDAO).execute(task);}
    public void updateData(Task task){ new UpdateTask(taskDAO).execute(task);}

    public void clearData(){ new CLearAll(taskDAO).execute();}
    public LiveData<List<Task>> getAllData(){
        return taskList;
    }


    private static class InsertTask extends AsyncTask<Task, Void, Void>{
        private TaskDAO taskDAO;

        public InsertTask(TaskDAO taskDAO) {
            this.taskDAO = taskDAO;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDAO.insertTask(tasks[0]);
            return null;
        }
    }private static class CLearAll extends AsyncTask<Void, Void, Void>{
        private TaskDAO taskDAO;

        public CLearAll(TaskDAO taskDAO) {
            this.taskDAO = taskDAO;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            taskDAO.deleteAll();
            return null;
        }
    }
    private static class DeleteTask extends AsyncTask<Task, Void, Void>{
        private TaskDAO taskDAO;

        public DeleteTask(TaskDAO taskDAO) {
            this.taskDAO = taskDAO;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDAO.deleteTask(tasks[0]);
            return null;
        }
    }
    private static class UpdateTask extends AsyncTask<Task, Void, Void>{
        private TaskDAO taskDAO;

        public UpdateTask(TaskDAO taskDAO) {
            this.taskDAO = taskDAO;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDAO.updateTask(tasks[0]);
            return null;
        }
    }
}
