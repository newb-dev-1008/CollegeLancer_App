package com.college.freelancestartup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RequestCollabTwoOpenActivity extends AppCompatActivity {

    private TextView posterName1, projectTitle1, postedDate1, projectDesc1, openFor1, skills1;
    private MaterialButton messageButton, applyButton;
    private EditText applyEditText;
    private String projectID;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private int numberApps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_collab2_opencard);

        posterName1 = findViewById(R.id.collab2_name);
        projectTitle1 = findViewById(R.id.collab2_projectTitle);
        postedDate1 = findViewById(R.id.collab2_date);
        projectDesc1 = findViewById(R.id.collab2_projectDesc);
        openFor1 = findViewById(R.id.collab2_openFor);
        skills1 = findViewById(R.id.collab2_skills);

        posterName1.setText(getIntent().getExtras().get("posterName1").toString());
        projectTitle1.setText(getIntent().getExtras().get("projectTitle1").toString());
        postedDate1.setText(getIntent().getExtras().get("postedDate1").toString());
        projectDesc1.setText(getIntent().getExtras().get("projectDesc1").toString());
        openFor1.setText(getIntent().getExtras().get("openFor1").toString());
        skills1.setText(getIntent().getExtras().get("skills1").toString());

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog applyCollab2 = new MaterialAlertDialogBuilder(RequestCollabTwoOpenActivity.this)
                        .setTitle("Are you sure you want to contribute to this project?")
                        .setMessage("We will be notifying the project head.\n" +
                                "Clicking \"I agree\" means that you consent to being added as a collaborator for the project, effective immediately.\n" +
                                "Please note that the Project Head holds complete authority over your collaborative status.\n\n" +
                                "If you leave the project mid-way for any reason, you will not receive credit and the project will not contribute towards" +
                                "the number of collaborations section in your profile, unless the Project Head deems it fit.")
                        .setPositiveButton("I agree", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                applyButtonPressed();
                            }
                        }).setNegativeButton("Cancel", null)
                        .create();
                applyCollab2.show();
                applyCollab2.setCancelable(false);
                applyCollab2.setCanceledOnTouchOutside(false);
            }
        });
    }

    private void applyButtonPressed() {
        // finish this
    }

    private void rejectButtonPressed() {

    }
}
