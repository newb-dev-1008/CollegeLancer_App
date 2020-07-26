package com.college.freelancestartup;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

class AllColabsOneOpenActivity extends AppCompatActivity {

    private TextView posterName1, projectTitle1, postedDate1, projectDesc1, openFor1, skills1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_collab1_opencard);

        posterName1 = findViewById(R.id.collab1_name);
        projectTitle1 = findViewById(R.id.collab1_projectTitle);
        postedDate1 = findViewById(R.id.collab1_date);
        projectDesc1 = findViewById(R.id.collab1_projectDesc);
        openFor1 = findViewById(R.id.collab1_openFor);
        skills1 = findViewById(R.id.collab1_skills);

        posterName1.setText(getIntent().getExtras().get("posterName1").toString());

    }
}
