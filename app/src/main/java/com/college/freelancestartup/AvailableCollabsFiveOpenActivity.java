package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

class AvailableCollabsFiveOpenActivity extends AppCompatActivity {

    private TextView name1, personDepartment1, personSemester1, numberCollabs1, numberProjects1, skills1;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private int internalReqFlag, checkedItem;
    private String userEmail, posterName;
    // private String projectID;
    private Calendar cObj;
    private MaterialButton previousCollabsButton, messageButton, viewProfileButton, requestButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_collab5_opencard);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        cObj = Calendar.getInstance();

        name1 = findViewById(R.id.collab5_name);
        personDepartment1 = findViewById(R.id.collab5_personDepartment);
        personSemester1 = findViewById(R.id.collab5_personSemester);
        numberCollabs1 = findViewById(R.id.collab5_collaborations);
        numberProjects1 = findViewById(R.id.collab5_projectsCompleted);
        skills1 = findViewById(R.id.collab5_skills);

        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                posterName = documentSnapshot.get("name").toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AvailableCollabsFiveOpenActivity.this, "The Firestore Database isn't responding.\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        previousCollabsButton = findViewById(R.id.collab5_seeCollabsButton);
        viewProfileButton = findViewById(R.id.collab5_viewProfileBtn);
        messageButton = findViewById(R.id.collab5_messageBtn);
        requestButton = findViewById(R.id.collab5_selectBtn);
        internalReqFlag = 0;
        checkedItem = 0;

        userEmail = getIntent().getExtras().get("userEmail").toString();

        db.collection("Users").document("User " + userEmail).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        name1.setText(documentSnapshot.get("name").toString());
                        personDepartment1.setText(documentSnapshot.get("department").toString());
                        personSemester1.setText(documentSnapshot.get("studentSemester").toString());
                        numberCollabs1.setText(documentSnapshot.get("numberCollabs").toString());
                        numberProjects1.setText(documentSnapshot.get("numberProjects").toString());
                        skills1.setText(documentSnapshot.get("studentSkills").toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AvailableCollabsFiveOpenActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
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
                requestCollabsPressed();
            }
        });

        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewProfilePressed();
            }
        });
    }

    private void viewProfilePressed() {

    }

    private void previousCollabsPressed() {
        Intent intent = new Intent(AvailableCollabsFiveOpenActivity.this, PreviousCollabsCollabFiveActivity.class);
        intent.putExtra("userEmail", userEmail);
        startActivity(intent);
    }

    private void requestCollabsPressed() {
        ArrayList<String> projectNames = new ArrayList<>();
        ArrayList<String> projectIDs = new ArrayList<>();
        AlertDialog requestFor = new MaterialAlertDialogBuilder(AvailableCollabsFiveOpenActivity.this)
                .setTitle("Are you sure you want to send a collaboration request?")
                .setMessage("The user will be notified. Please note that this does not seal the deal.\n" +
                        "The final decision to contribute to your project lies with the user, about which you will be informed shortly.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                                .collection("Projects").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.size() > 0) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        if (!documentSnapshot.get("projectStatus").toString().equals("Completed")) {
                                            projectNames.add(documentSnapshot.get("projectTitle").toString());
                                            projectIDs.add(documentSnapshot.get("projectID").toString());
                                        }
                                    }
                                    if (projectNames.size() == 0) {
                                        internalReqFlag = 1;
                                    }
                                } else {
                                    internalReqFlag = 1;
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AvailableCollabsFiveOpenActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                internalReqFlag = 1;
                            }
                        });

                        if (internalReqFlag != 1) {
                            AlertDialog.Builder chooseProjectBuilder = new AlertDialog.Builder(AvailableCollabsFiveOpenActivity.this);
                            chooseProjectBuilder.setTitle("Choose the project you want to collaborate on");
                            chooseProjectBuilder.setSingleChoiceItems((CharSequence[]) projectNames.toArray(), checkedItem, null);
                            chooseProjectBuilder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (checkedItem == -1) {
                                        Toast.makeText(AvailableCollabsFiveOpenActivity.this, "Please select a project first.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Map<String, Object> sendRequestCollab5 = new HashMap<>();
                                        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                                                .collection("Projects").document(projectIDs.get(checkedItem)).get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        sendRequestCollab5.put("posterTitle", posterName);
                                                        sendRequestCollab5.put("projectTitle", documentSnapshot.get("projectTitle").toString());
                                                        sendRequestCollab5.put("postDate", cObj.getTime().toString());
                                                        sendRequestCollab5.put("projectSkills", documentSnapshot.get("skills").toString());
                                                        sendRequestCollab5.put("projectOpenFor", documentSnapshot.get("projectOpenFor").toString());
                                                        sendRequestCollab5.put("projectDesc", documentSnapshot.get("projectDesc").toString());
                                                        sendRequestCollab5.put("projectID", documentSnapshot.get("projectID").toString());
                                                        db.collection("User").document("User " + userEmail)
                                                                .collection("CollabRequests").document(documentSnapshot.get("projectID").toString())
                                                                .set(sendRequestCollab5).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(AvailableCollabsFiveOpenActivity.this, "Request sent. Expect a response soon!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(AvailableCollabsFiveOpenActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                dialogInterface.dismiss();
                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AvailableCollabsFiveOpenActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                dialogInterface.dismiss();
                                            }
                                        });
                                    }
                                }
                            }).setNegativeButton("Cancel", null);
                            chooseProjectBuilder.show();
                            chooseProjectBuilder.setCancelable(false);

                        } else {
                            Toast.makeText(AvailableCollabsFiveOpenActivity.this, "You don't have any projects to collaborate on.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel", null)
                .create();
        requestFor.show();
        requestFor.setCancelable(false);
        requestFor.setCanceledOnTouchOutside(false);
    }
}
