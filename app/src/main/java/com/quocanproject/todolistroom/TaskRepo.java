package com.quocanproject.todolistroom;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.quocanproject.todolistroom.database.TaskDAO;
import com.quocanproject.todolistroom.database.TaskDatabase;

import java.util.ArrayList;
import java.util.List;

public class TaskRepo {
    private TaskDAO taskDAO;
    private LiveData<List<Task>> taskList;

    public TaskRepo(Application application) {
        TaskDatabase taskDatabase = TaskDatabase.getInstance(application);
        taskDAO = taskDatabase.taskDAO();
        taskList = taskDAO.getListTask();
    }

    public void insertData(Task task, TaskCallback taskCallback) {
        String nameTask = task.getNameTask();
        new CheckTaskExist(taskDAO, nameTask, new CheckTaskExistCallback() {
            @Override
            public void onTaskExist(boolean exist) {
                if (exist){
                    taskCallback.onTaskExists();
                } else {
                    new InsertTask(taskDAO, new TaskInsertCallback() {
                        @Override
                        public void onTaskInserted() {
                            taskCallback.onTaskInserted();
                        }
                    }).execute(task);
                }
            }
        }).execute();
    }

    public void deleteData(Task task) {
        new DeleteTask(taskDAO).execute(task);
    }

    public void updateData(Task task) {
        new UpdateTask(taskDAO).execute(task);
    }

    public void clearData() {
        new CLearAll(taskDAO).execute();
    }

    public LiveData<List<Task>> getAllData() {
        return taskList;
    }

    public LiveData<List<Task>> searchTask(String name) {
        return taskDAO.listFromSearch(name);
    }




    private static class InsertTask extends AsyncTask<Task, Void, Void> {
        private TaskDAO taskDAO;
        private TaskInsertCallback callback;

        public InsertTask(TaskDAO taskDAO, TaskInsertCallback callback) {
            this.taskDAO = taskDAO;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDAO.insertTask(tasks[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            callback.onTaskInserted();
        }
    }

    private static class CLearAll extends AsyncTask<Void, Void, Void> {
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

    private static class DeleteTask extends AsyncTask<Task, Void, Void> {
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

    private static class UpdateTask extends AsyncTask<Task, Void, Void> {
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

    private static class SearchTask extends AsyncTask<Task, Void, Void> {
        private TaskDAO taskDAO;

        public SearchTask(TaskDAO taskDAO) {
            this.taskDAO = taskDAO;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDAO.listFromSearch(tasks[0].getNameTask());
            return null;
        }
    }

    private static class CheckTaskExist extends AsyncTask<Void, Void, Boolean>{
        private TaskDAO taskDAO;
        private String taskName;
        private CheckTaskExistCallback callback;

        public CheckTaskExist(TaskDAO taskDAO, String taskName, CheckTaskExistCallback callback) {
            this.taskDAO = taskDAO;
            this.taskName = taskName;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            int count = taskDAO.checkTaskExist(taskName);
            return count > 0;
        }

        @Override
        protected void onPostExecute(Boolean exist) {
            super.onPostExecute(exist);
            callback.onTaskExist(exist);
        }
    }


    private interface CheckTaskExistCallback {
        void onTaskExist(boolean exist);
    }
    public interface TaskCallback {
        void onTaskExists();
        void onTaskInserted();
    }


    public interface TaskInsertCallback{
        void onTaskInserted();
    }


}
