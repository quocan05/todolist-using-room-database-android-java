package com.quocanproject.todolistroom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {


    private List<Task> listTask;
    private IClickItem iClickItem;

    private ItemClickListenerRCV itemClickListenerRCV;



    public TaskListAdapter(IClickItem iClickItem, ItemClickListenerRCV itemClickListenerRCV) {
        this.iClickItem = iClickItem;
        this.itemClickListenerRCV = itemClickListenerRCV;
    }

    public interface IClickItem {
        void updateTask(Task task);
        void deleteTask(Task task);

        void updateStatusTask(Task task);
    }

    public interface ItemClickListenerRCV {
        void onItemClick(Task task);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);

        return new TaskViewHolder(view, itemClickListenerRCV);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if(listTask != null) {
            Task task = listTask.get(position);
            holder.tvNameTask.setText(task.getNameTask());
            holder.tvTypeTask.setText(task.getTypeTask());

            holder.tvNameTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListenerRCV.onItemClick(task);
                }
            });

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


            holder.cbDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickItem.updateStatusTask(task);

                }
            });
            holder.cbDone.setChecked(task.isStatusTask());


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

    public class TaskViewHolder extends RecyclerView.ViewHolder{


        TextView tvNameTask, tvTypeTask;
        ImageButton btnEdit, btnDelete;
        CheckBox cbDone;
        public TaskViewHolder(@NonNull View itemView, ItemClickListenerRCV iclick) {
            super(itemView);
            tvNameTask = itemView.findViewById(R.id.tvNameTask);
            tvTypeTask = itemView.findViewById(R.id.tvType);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEditTask);
            cbDone = itemView.findViewById(R.id.cbDone);
            itemClickListenerRCV = iclick;
        }

    }
}
