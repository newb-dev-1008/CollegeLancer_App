package com.college.freelancestartup;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class StudentAddProjectForCollab extends AppCompatActivity {

    EditText projectTitle, projectOpenFor, projectDesc;
    AutoCompleteTextView projectSkills;
    String posterTitle, posterEmail, postDate, projectID, projectStatus;
    Calendar calendar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    int numberPicked, numberApps, endVotes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_addnew_project_forcollab);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        projectTitle = findViewById(R.id.projectTitleEditText);
        projectOpenFor = findViewById(R.id.projectOpenForEditText);
        projectDesc = findViewById(R.id.projDescEditText);
        projectSkills = findViewById(R.id.proj_Skills);
        numberApps = 0;
        numberPicked = 0;
        endVotes = 0;
        calendar = Calendar.getInstance();


    }
}
