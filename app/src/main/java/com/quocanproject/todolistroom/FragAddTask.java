package com.quocanproject.todolistroom;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragAddTask extends Fragment {

    TaskRepo taskRepo;
    LiveData<List<Task>> taskListLiveData;

    DatePickerDialog datePickerDialog;
    RadioButton rbSetDueDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_add_task, container, false);

        EditText edtAddTask = view.findViewById(R.id.edt_nameTask);
        RadioGroup rgType = view.findViewById(R.id.radioGrType);
        Button btnAddTask = view.findViewById(R.id.btnAddTask);

        RadioGroup rgDueDate = view.findViewById(R.id.radioGrSetDueDate);
        rgDueDate.check(R.id.rbNoDueDate);

        rbSetDueDate = view.findViewById(R.id.rbSetDueDate);
        rbSetDueDate.setText(getDateToday());
        initDatePicker();

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
                    int selectedRadioButtonDueDateId = rgDueDate.getCheckedRadioButtonId();
                    Toast.makeText(getActivity(), selectedRadioButtonDueDateId+"", Toast.LENGTH_SHORT).show();
                    if (selectedRadioButtonId != -1) {
                        RadioButton rbSelected = getActivity().findViewById(selectedRadioButtonId); // phai get id tu view cua onCreate
                        type = rbSelected.getText().toString().trim();

                        Task task = new Task(name, type, false, null);

                        if (selectedRadioButtonDueDateId == R.id.rbSetDueDate){
                            Toast.makeText(getActivity(), rbSetDueDate.getText().toString(), Toast.LENGTH_SHORT).show();
                            rbSetDueDate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openDatePicker(view);
                                }
                            });

                        }

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

    private String getDateToday() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                rbSetDueDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day +" "+ year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "Invalid";
        }
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


    void openDatePicker(View view){
        datePickerDialog.show();
    }
}
