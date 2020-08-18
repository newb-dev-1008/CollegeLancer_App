package com.college.freelancestartup;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

class ApplicantLogOpenActivity extends AppCompatActivity {

    private TextView name1, personDepartment1, personSemester1, numberCollabs1, numberProjects1, skills1, personPhone1, applicationArticleTV;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private int internalReqFlag, checkedItem, flagLog;
    private String userEmail, posterName, selectedProjectID;
    // private String projectID;
    private Calendar cObj;
    private MaterialButton previousCollabsButton, messageButton, requestButton;
    private String[] projNames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applicant_log_opencard);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        cObj = Calendar.getInstance();

        name1 = findViewById(R.id.log_name);
        personDepartment1 = findViewById(R.id.log_personDepartments);
        personSemester1 = findViewById(R.id.log_personSemester);
        personPhone1 = findViewById(R.id.log_personPhoneNo);
        numberCollabs1 = findViewById(R.id.log_collaborations);
        numberProjects1 = findViewById(R.id.log_projectsCompleted);
        skills1 = findViewById(R.id.log_skills);
        applicationArticleTV
    }
}
