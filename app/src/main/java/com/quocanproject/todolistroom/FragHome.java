package com.quocanproject.todolistroom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FragHome extends Fragment {


    TaskRepo taskRepo;

    String[] itemSortby = {"Day create: Oldest", "Day create: Newest"};

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterSort;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

//        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextSort);
//        adapterSort = new ArrayAdapter<String>(getActivity(), R.layout.list_sort_item, itemSortby);
//        autoCompleteTextView.setAdapter(adapterSort);
//
//        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String name =  adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
//            }
//        });


        TextView tvNoItem = view.findViewById(R.id.tvNothing);
        RecyclerView rcvListTask = view.findViewById(R.id.rcv_list_home);
        TaskViewModel taskViewModel = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
        LiveData<List<Task>> taskLiveData = taskViewModel.getAllTask();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        rcvListTask.setLayoutManager(layoutManager);
        TaskListAdapter taskListAdapter = new TaskListAdapter(new TaskListAdapter.IClickItem() {
            @Override
            public void updateTask(Task task) {
                clickUpdateTask(task);
            }

            @Override
            public void deleteTask(Task task) {
                clickDeleteTask(task);
            }

            @Override
            public void updateStatusTask(Task task) {
                clickUpdateStatus(task);
            }
        }, new TaskListAdapter.ItemClickListenerRCV() {
            @Override
            public void onItemClick(Task task) {
                clickSeeInfo(task);
            }
        });


        taskLiveData.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                taskListAdapter.setData(tasks);
                if (rcvListTask.getAdapter() != null && rcvListTask.getAdapter().getItemCount() > 0) {
                    //Toast.makeText(getActivity(),"Item: "+ rcvListTask.getAdapter().getItemCount(), Toast.LENGTH_SHORT).show();
                    tvNoItem.setVisibility(View.GONE);
                } else {
                    tvNoItem.setVisibility(View.VISIBLE);
                }
            }
        });

        rcvListTask.setAdapter(taskListAdapter);

        return view;
    }

    private void clickSeeInfo(Task task) {
        Toast.makeText(getActivity(), "info task: " + task.toString(), Toast.LENGTH_SHORT).show();
    }

    private void clickUpdateStatus(Task taskUpdateStatus) {
        if (!taskUpdateStatus.isStatusTask()) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Confirm complete task")
                    .setMessage("Are you completed bro: " + taskUpdateStatus.getNameTask() + " ?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            confirmUpdateStatus(taskUpdateStatus);
                        }
                    })
                    .setNegativeButton("NAH BRUH, IM DONE YET", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            unDoneTask(taskUpdateStatus);
                        }
                    })
                    .show();
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle("Undone task")
                    .setMessage("You have done yet bro: " + taskUpdateStatus.getNameTask() + " ?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            unDoneTask(taskUpdateStatus);
                        }
                    })
                    .setNegativeButton("NAH BRUH", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            confirmUpdateStatus(taskUpdateStatus);
                        }
                    })
                    .show();
        }
    }

    private void unDoneTask(Task taskUpdateStatus) {
        taskUpdateStatus.setStatusTask(false);
        taskRepo.updateData(taskUpdateStatus);
        Toast.makeText(getActivity(), "Undone task: " + taskUpdateStatus.getNameTask() + ", " + taskUpdateStatus.isStatusTask(), Toast.LENGTH_SHORT).show();

    }

    private void confirmUpdateStatus(Task taskUpdateStatus) {

        taskUpdateStatus.setStatusTask(true);
        taskRepo.updateData(taskUpdateStatus);
        Toast.makeText(getActivity(), "Complete task: " + taskUpdateStatus.getNameTask() + ", " + taskUpdateStatus.isStatusTask(), Toast.LENGTH_SHORT).show();

    }

    private void clickDeleteTask(Task taskForDelete) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm delete task")
                .setMessage("Are you sure wanna delete task: " + taskForDelete.getNameTask() + " ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        confirmDeleteTask(taskForDelete);
                    }
                })
                .setNegativeButton("NAH BRUH", null)
                .show();
    }

    private void confirmDeleteTask(Task taskForDelete) {
        taskRepo.deleteData(taskForDelete);
        Toast.makeText(getActivity(), "Delete" + taskForDelete.getNameTask() + " success !", Toast.LENGTH_SHORT).show();
    }

    private void clickUpdateTask(Task taskForUpdate) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.update_item_dialog);

        EditText edtUpdateName = dialog.findViewById(R.id.edt_updateName);
        RadioGroup rgTypeUpdate = dialog.findViewById(R.id.radioGrTypeUpdate);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdateTask);

        edtUpdateName.setText(taskForUpdate.getNameTask().toString().trim());
        String typedSeleted = taskForUpdate.getTypeTask().toString().trim();

        int radioButtonCount = rgTypeUpdate.getChildCount();
        for (int i = 0; i < radioButtonCount; i++) {
            View radioButtonView = rgTypeUpdate.getChildAt(i);
            if (radioButtonView instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) radioButtonView;
                if (radioButton.getText().toString().equals(typedSeleted)) {
                    radioButton.setChecked(true);
                    break;
                }
            }
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedType = rgTypeUpdate.getCheckedRadioButtonId();
                RadioButton checkedButton = getActivity().findViewById(selectedType);

                String typeUpdated = checkedButton.getText().toString().trim();

                taskForUpdate.setNameTask(edtUpdateName.getText().toString().trim());
                taskForUpdate.setTypeTask(typeUpdated);

                //Toast.makeText(getActivity(), edtUpdateName.getText()+ " "+ typeUpdated, Toast.LENGTH_SHORT).show();

                taskRepo.updateData(taskForUpdate);
                dialog.dismiss();

                Toast.makeText(getActivity(), "Update " + taskForUpdate.toString() + "success!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskRepo = new TaskRepo(getActivity().getApplication());
    }
}
