package com.college.freelancestartup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class ReportBugFragment extends Fragment {

    private EditText bugLocationET, bugDescriptionET;
    private RadioGroup bugFrequencyRadioGroup, bugStallRadioGroup;
    private View root;
    private MaterialButton addScreenshots, reportBugButton;
    private String bugFreq, bugStalled;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.report_bug_frag, container, false);
        bugLocationET = root.findViewById(R.id.bugLocationEditText);
        bugDescriptionET = root.findViewById(R.id.bugDescriptionET);

        bugFrequencyRadioGroup = root.findViewById(R.id.bugFrequencyRadioGroup);
        bugStallRadioGroup = root.findViewById(R.id.bugStallRadioGroup);

        addScreenshots = root.findViewById(R.id.addSSButton);
        reportBugButton = root.findViewById(R.id.reportBugButton);

        bugStallRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedButton = radioGroup.findViewById(i);
                String bugStalled1 = checkedButton.getText().toString();
                if (bugStalled1.equals("Yes, and I want to lodge a complaint and retrieve my lost work as soon as possible.")) {
                    String s = "Report Bug and lodge complaint";
                    bugStalled = bugStalled1;
                    reportBugButton.setText(s);
                } else {
                    String s1 = "Report Bug";
                    bugStalled = bugStalled1;
                    reportBugButton.setText(s1);
                }
            }
        });

        bugFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedButton1 = radioGroup.findViewById(i);
                bugFreq = checkedButton1.getText().toString();
            }
        });

        return root;
    }

    private void

}
