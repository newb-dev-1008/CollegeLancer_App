package com.college.freelancestartup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

class AllColabsOneOpenActivity extends AppCompatActivity {

    private TextView posterName1, projectTitle1, postedDate1, projectDesc1, openFor1, skills1;
    private MaterialButton messageButton, applyButton;
    private View view;
    private EditText applyEditText;
    private String projectID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_collab1_opencard);

        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.apply_collab1_dialogedittext, null);

        applyEditText = view.findViewById(R.id.applyEditText);

        posterName1 = findViewById(R.id.collab1_name);
        projectTitle1 = findViewById(R.id.collab1_projectTitle);
        postedDate1 = findViewById(R.id.collab1_date);
        projectDesc1 = findViewById(R.id.collab1_projectDesc);
        openFor1 = findViewById(R.id.collab1_openFor);
        skills1 = findViewById(R.id.collab1_skills);
        applyButton = findViewById(R.id.collab1_applyBtn);
        messageButton = findViewById(R.id.collab1_messageBtn);

        posterName1.setText(getIntent().getExtras().get("posterName1").toString());
        projectTitle1.setText(getIntent().getExtras().get("projectTitle1").toString());
        postedDate1.setText(getIntent().getExtras().get("postedDate1").toString());
        projectDesc1.setText(getIntent().getExtras().get("projectDesc1").toString());
        openFor1.setText(getIntent().getExtras().get("openFor1").toString());
        skills1.setText(getIntent().getExtras().get("skills1").toString());
        projectID = getIntent().getExtras().get("projectID1").toString();

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog applyCollab1 = new MaterialAlertDialogBuilder(AllColabsOneOpenActivity.this)
                        .setView(view)
                        .setTitle("Apply for collaboration")
                        .setMessage("Write a brief description about yourself and how you'd be contributing to the project.")
                        .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (TextUtils.isEmpty(applyEditText.getText().toString()) {
                                    Toast.makeText(AllColabsOneOpenActivity.this, "Please enter a short description.", Toast.LENGTH_SHORT).show();
                                } else {

                                }
                            }
                        })
            }
        });
    }
}
