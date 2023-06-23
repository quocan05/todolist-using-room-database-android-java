package com.quocanproject.todolistroom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragSetting extends Fragment {
    
    TaskRepo taskRepo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_setting, container, false);
        ImageButton btnClearAll = view.findViewById(R.id.btnClearAll);
        ImageButton btnChangeTheme = view.findViewById(R.id.btnChangeTheme);
        
        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm clear")
                        .setMessage("Are you sure about clear all task ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                clickToClear();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
            }
        });
        
        
        btnChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Function is being updated, see you soon....", Toast.LENGTH_SHORT).show();
            }
        });
        
        return view;
    }

    private void clickToClear() {
        taskRepo.clearData();
        Toast.makeText(getActivity(), "Clear all task successfully !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskRepo = new TaskRepo(getActivity().getApplication());
    }
}
