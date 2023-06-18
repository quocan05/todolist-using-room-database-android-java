package com.quocanproject.todolistroom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {


    private List<Task> listTask;
    private IClickItem iClickItem;

    public TaskListAdapter(IClickItem iClickItem) {
        this.iClickItem = iClickItem;
    }

    public interface IClickItem {
        void updateTask(Task task);
        void deleteTask(Task task);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if(listTask != null) {
            Task task = listTask.get(position);
            holder.tvNameTask.setText(task.getNameTask());
            holder.tvTypeTask.setText(task.getTypeTask());


            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickItem.updateTask(task);
                }
            });

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickItem.deleteTask(task);
                }
            });
        } else {
            return;
        }
    }

    public void setData(List<Task> list){
        listTask = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if(listTask != null){
            return listTask.size();
        }
        return 0;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameTask, tvTypeTask;
        ImageButton btnEdit, btnDelete;
        CheckBox cbDone;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameTask = itemView.findViewById(R.id.tvNameTask);
            tvTypeTask = itemView.findViewById(R.id.tvType);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEditTask);
            cbDone = itemView.findViewById(R.id.cbDone);
        }
    }
}
