package com.college.freelancestartup;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

class MyCollabsFourOpenActivity extends AppCompatActivity {

    private TextView posterName, projectTitle, postedDate, collabStatus, projectDesc, numApplicants, numSelectedApplicants;
    private MaterialButton applicantsLogButton;
    private Switch collab4VisibleSwitch;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_collab4_opencard);

        posterName = findViewById(R.id.collab3_name);
        projectTitle = findViewById(R.id.collab3_projectTitle);
        postedDate = findViewById(R.id.collab3_date);
        projectDesc = findViewById(R.id.collab3_projectDesc);
        collabStatus = findViewById(R.id.collab3_status);
        numApplicants = findViewById(R.id.collab4_noApplicants);
        numSelectedApplicants = findViewById(R.id.collab3_noAcceptedApplicants);
        applicantsLogButton = findViewById(R.id.collab4_noApplicantsBtn);
        collab4VisibleSwitch = findViewById(R.id.collab4_visibleSwitch);

        posterName.setText(getIntent().getExtras().get("posterName").toString());
        projectTitle.setText(getIntent().getExtras().get("projectTitle").toString());
        postedDate.setText(getIntent().getExtras().get("postedDate").toString());
        projectDesc.setText(getIntent().getExtras().get("projectDesc").toString());
        collabStatus.setText(getIntent().getExtras().get("projectStatus").toString());
        numApplicants.setText(getIntent().getExtras().get("numApplicants").toString());
        numSelectedApplicants.setText(getIntent().getExtras().get("numSelectedApplicants").toString());


        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail()).collection("MyCollabs")
                .get()
    }
}
