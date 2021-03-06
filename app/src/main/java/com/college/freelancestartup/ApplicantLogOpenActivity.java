package com.college.freelancestartup;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ApplicantLogOpenActivity extends AppCompatActivity {

    private TextView name1, personDepartment1, personSemester1, numberCollabs1, numberProjects1, skills1, personPhone1, applicationArticleTV, pickedStatus;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private int internalReqFlag, checkedItem, flagLog, numberPicked;;
    private String userEmail, posterName, selectedProjectID, pickedStatusString, joinDate;
    // private String projectID, pickedStatusString;
    private Calendar cObj;
    private MaterialButton previousCollabsButton, messageButton, requestButton, rejectButton;
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
        applicationArticleTV = findViewById(R.id.log_applicationArticle);
        pickedStatus = findViewById(R.id.log_personPickedStatus);

        previousCollabsButton = findViewById(R.id.log_seeCollabsButton);
        messageButton = findViewById(R.id.log_messageBtn);
        requestButton = findViewById(R.id.log_selectBtn);
        rejectButton = findViewById(R.id.log_rejectBtn);

        userEmail = getIntent().getExtras().get("userEmail").toString();
        selectedProjectID = getIntent().getExtras().get("projectID").toString();

        db.collection("Users").document("User " + userEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name1.setText(documentSnapshot.get("name").toString());
                personDepartment1.setText(documentSnapshot.get("department").toString());
                personSemester1.setText(documentSnapshot.get("studentSemester").toString());
                personPhone1.setText(documentSnapshot.get("phoneNumber").toString());
                numberCollabs1.setText(documentSnapshot.get("numberCollabs").toString());
                numberProjects1.setText(documentSnapshot.get("numberProjects").toString());
                skills1.setText(documentSnapshot.get("studentSkills").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ApplicantLogOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        db.collection("CollabProjects").document(selectedProjectID).collection("Collaborators")
                .document("User " + userEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                applicationArticleTV.setText(documentSnapshot.get("applicationArticle").toString());
                pickedStatus.setText(documentSnapshot.get("picked").toString());
            }
        });

        previousCollabsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousCollabsPressed();
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestButtonPressed();
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectButtonPressed();
            }
        });
    }

    private void rejectButtonPressed() {
        AlertDialog selectApplicant = new MaterialAlertDialogBuilder(this)
                .setTitle("Reject Applicant?")
                .setMessage("The candidate will be rejected. However, re-application from the candidate will be allowed.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pickedStatusString = "Rejected";
                        pickedStatus.setText(pickedStatusString);
                        pickedStatus.setTextColor(Color.parseColor("#800000"));
                        db.collection("CollabProjects").document(selectedProjectID).collection("Collaborators")
                                .document("User " + userEmail).update("picked", "Rejected").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Send notification and e-mail to applicant
                                Toast.makeText(ApplicantLogOpenActivity.this, "The applicant has been rejected.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ApplicantLogOpenActivity.this, ApplicantLogActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ApplicantLogOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Cancel", null)
                .create();
        selectApplicant.show();
    }

    private void previousCollabsPressed() {
        Intent intent = new Intent(ApplicantLogOpenActivity.this, PreviousCollabsCollabFiveActivity.class);
        intent.putExtra("userEmail", userEmail);
        startActivity(intent);
    }

    private void requestButtonPressed() {
        AlertDialog selectApplicant = new MaterialAlertDialogBuilder(this)
                .setTitle("Select Applicant?")
                .setMessage("The candidate will be added to the project as a contributor.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pickedStatusString = "Accepted";
                        pickedStatus.setText(pickedStatusString);
                        pickedStatus.setTextColor(Color.parseColor("#228B22"));
                        joinDate = cObj.getTime().toString();
                        db.collection("CollabProjects").document(selectedProjectID).collection("Collaborators")
                                .document("User " + userEmail).update("picked", "Selected", "joinDate", joinDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                db.collection("CollabProjects").document(selectedProjectID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        numberPicked = Integer.parseInt(documentSnapshot.get("numberPicked").toString());
                                        numberPicked = numberPicked + 1;
                                        db.collection("CollabProjects").document(selectedProjectID).update("numberPicked", numberPicked);
                                        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                                                .collection("MyCollabs").document(selectedProjectID).update("numberPicked", numberPicked);
                                        Intent intent = new Intent(ApplicantLogOpenActivity.this, ApplicantLogActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Toast.makeText(ApplicantLogOpenActivity.this, "Get excited! You have a new member in your team!", Toast.LENGTH_SHORT).show();

                                        // Notification to the applicant, and also an e-mail
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ApplicantLogOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ApplicantLogOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        selectApplicant.show();
    }
}
