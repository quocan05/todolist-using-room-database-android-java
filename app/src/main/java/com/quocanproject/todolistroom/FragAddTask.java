package com.quocanproject.todolistroom;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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

import com.quocanproject.todolistroom.database.TaskDatabase;

import java.util.List;

public class FragAddTask extends Fragment {

    TaskRepo taskRepo;
    LiveData<List<Task>> taskListLiveData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_add_task, container, false);

        EditText edtAddTask = view.findViewById(R.id.edt_nameTask);
        RadioGroup rgType = view.findViewById(R.id.radioGrType);
        Button btnAddTask = view.findViewById(R.id.btnAddTask);


        edtAddTask.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    // Ẩn bàn phím
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String name = edtAddTask.getText().toString().trim();

                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(getActivity(), "Enter name task !", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String type;
                    int selectedRadioButtonId = rgType.getCheckedRadioButtonId();
                    if (selectedRadioButtonId != -1) {
                        RadioButton rbSelected = getActivity().findViewById(selectedRadioButtonId); // phai get id tu view cua onCreate
                        type = rbSelected.getText().toString().trim();
                        Task task = new Task(name, type, false);
                        taskRepo.insertData(task, new TaskRepo.TaskCallback() {
                            @Override
                            public void onTaskInserted() {
                                hideKeyboard(view);
                                rgType.clearCheck();
                                edtAddTask.setText("");
                                Toast.makeText(getActivity(), "add " + task.toString() + " success!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onTaskExists() {
                                Toast.makeText(getActivity(), "Task already exist !", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), "Please chose type of task", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    String stackTrace = Log.getStackTraceString(e);
                    Toast.makeText(getActivity(), stackTrace, Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskRepo = new TaskRepo(getActivity().getApplication());
    }

    void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
