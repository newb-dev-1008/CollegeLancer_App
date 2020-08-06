package com.college.freelancestartup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class ReportBugFragment extends Fragment {

    private EditText bugLocationET, bugDescriptionET;
    private RadioGroup bugFrequencyRadioGroup, bugStallRadioGroup;
    private View root;
    private MaterialButton addScreenshots, reportBug;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.report_bug_frag, container, false);
        bugLocationET = root.findViewById(R.id.bugLocationEditText);
        bugDescriptionET = root.findViewById(R.id.bugDescriptionET);

        bugFrequencyRadioGroup = root.findViewById(R.id.bugFrequencyRadioGroup);
        bugStallRadioGroup = root.findViewById(R.id.bugStallRadioGroup);

        addScreenshots = root.findViewById(R.id.addSSButton);
        reportBug = root.findViewById(R.id.reportBugButton);

        return root;
    }
}
