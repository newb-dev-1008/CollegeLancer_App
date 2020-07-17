package com.college.freelancestartup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class ComplaintsFragment extends Fragment {

    private View root;
    private MaterialButton lodgeComplaintButton, writeComplaintEmailButton;
    private EditText complaintET;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.help_complaints_frag, container, false);

        lodgeComplaintButton = root.findViewById(R.id.lodgeComplaintButton);
        writeComplaintEmailButton = root.findViewById(R.id.writeToUsButton);
        complaintET = root.findViewById(R.id.complaintsET);

        return root;
    }
}
