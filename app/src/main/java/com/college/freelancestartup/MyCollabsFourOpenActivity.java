package com.college.freelancestartup;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

class MyCollabsFourOpenActivity extends AppCompatActivity {

    private TextView posterName1, projectTitle1, collabDate1, collabStatus1, projectDesc1, numApplicants, numSelectedApplicants;
    private MaterialButton applicantsLogButton;
    private Switch collab4VisibleSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_collab4_opencard);

        posterName1 = findViewById(R.id.collab3_name);
        projectTitle1 = findViewById(R.id.collab3_projectTitle);
        collabDate1 = findViewById(R.id.collab3_date);
        projectDesc1 = findViewById(R.id.collab3_projectDesc);
        collabStatus1 = findViewById(R.id.collab3_status);
        numApplicants = findViewById(R.id.collab4_noApplicants);
        numSelectedApplicants = findViewById(R.id.collab3_noAcceptedApplicants);
        applicantsLogButton = findViewById(R.id.collab4_noApplicantsBtn);
        collab4VisibleSwitch = findViewById(R.id.collab4_visibleSwitch);

        posterName1.setText(getIntent().getExtras().get("posterName").toString());
        projectTitle1.setText(getIntent().getExtras().get("projectTitle").toString());
        collabDate1.setText(getIntent().getExtras().get("collabDate").toString());
        projectDesc1.setText(getIntent().getExtras().get("projectDesc").toString());
        collabStatus1.setText(getIntent().getExtras().get("collabStatus").toString());
        numApplicants.setText(getIntent().getExtras().get("numApplicants").toString();
        numSelectedApplicants.setText(getIntent().getExtras().get("numSelectedApplicants").toString();
    }
}
