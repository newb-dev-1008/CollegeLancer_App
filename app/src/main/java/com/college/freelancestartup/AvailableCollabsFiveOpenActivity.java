package com.college.freelancestartup;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

class AvailableCollabsFiveOpenActivity extends AppCompatActivity {

    private TextView name1, personDepartment1, personSemester1, numberCollabs1, numberProjects1, skills1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_collab5_opencard);

        name1 = findViewById(R.id.collab5_name);
        personDepartment1 = findViewById(R.id.collab5_personDepartment);
        personSemester1 = findViewById(R.id.collab5_personSemester);
        numberCollabs1 = findViewById(R.id.collab5_collaborations);
        numberProjects1 = findViewById(R.id.collab5_projectsCompleted);
        skills1 = findViewById(R.id.collab5_skills);

        name1.setText(getIntent().getExtras().get("name").toString());
        personDepartment1.setText(getIntent().getExtras().get("department").toString());
        personSemester1.setText(getIntent().getExtras().get("semester").toString());
        numberCollabs1.setText(getIntent().getExtras().get("numberCollabs").toString());
        numberProjects1.setText(getIntent().getExtras().get("numberProjects").toString());
        skills1.setText(getIntent().getExtras().get("skills").toString());
    }
}
