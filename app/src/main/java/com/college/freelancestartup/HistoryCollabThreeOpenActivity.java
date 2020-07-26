package com.college.freelancestartup;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

class HistoryCollabThreeOpenActivity extends AppCompatActivity {

    private TextView posterName1, projectTitle1, collabDate1, projectDesc1, collabStatus1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_collab3_opencard);

        posterName1 = findViewById(R.id.collab3_name);
        projectTitle1 = findViewById(R.id.collab3_projectTitle);
        collabDate1 = findViewById(R.id.collab3_date);
        projectDesc1 = findViewById(R.id.collab3_projectDesc);
        collabStatus1 = findViewById(R.id.collab3_status);

        posterName1.setText(getIntent().getExtras().get("posterName1").toString());
        projectTitle1.setText(getIntent().getExtras().get("projectTitle1").toString());
        collabDate1.setText(getIntent().getExtras().get("collabDate1").toString());
        projectDesc1.setText(getIntent().getExtras().get("projectDesc1").toString());
        collabStatus1.setText(getIntent().getExtras().get("collabStatus1").toString());
    }
}
