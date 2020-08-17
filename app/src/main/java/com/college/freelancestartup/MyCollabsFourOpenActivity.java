package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
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

public class MyCollabsFourOpenActivity extends AppCompatActivity {

    private TextView posterName, projectTitle, postedDate, collabStatus, projectDesc, numApplicants, numSelectedApplicants, skills, openFor, noVotes, noVotesTV;
    private MaterialButton applicantsLogButton, finishProjectButton;
    private Switch collab4VisibleSwitch, endProjectSwitch;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private String projectID;
    private int numberVotes, switchFlag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_collab4_opencard);

        posterName = findViewById(R.id.collab4_name);
        projectTitle = findViewById(R.id.collab4_projectTitle);
        postedDate = findViewById(R.id.collab4_date);
        projectDesc = findViewById(R.id.collab4_projectDesc);
        collabStatus = findViewById(R.id.collab4_status);
        numApplicants = findViewById(R.id.collab4_noApplicants);
        numSelectedApplicants = findViewById(R.id.collab4_noAcceptedApplicants);
        applicantsLogButton = findViewById(R.id.collab4_noApplicantsBtn);
        finishProjectButton = findViewById(R.id.collab4_finishProjectBtn);
        collab4VisibleSwitch = findViewById(R.id.collab4_visibleSwitch);
        skills = findViewById(R.id.collab4_skills);
        openFor = findViewById(R.id.collab4_openFor);
        endProjectSwitch = findViewById(R.id.collab4_endProjectSwitch);
        noVotes = findViewById(R.id.collab4_noVotes);
        noVotesTV = findViewById(R.id.collab4_noVotesTV);
        switchFlag = 0;

        projectID = getIntent().getExtras().get("projectID").toString();

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                .collection("MyCollabs").document(projectID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                posterName.setText(documentSnapshot.get("posterTitle").toString());
                projectTitle.setText(documentSnapshot.get("projectTitle").toString());
                postedDate.setText(documentSnapshot.get("postDate").toString());
                projectDesc.setText(documentSnapshot.get("projectDesc").toString());
                collabStatus.setText(documentSnapshot.get("projectStatus").toString());
                numApplicants.setText(documentSnapshot.get("numberApps").toString());
                numSelectedApplicants.setText(documentSnapshot.get("numberPicked").toString());
                skills.setText(documentSnapshot.get("projectSkills").toString());
                openFor.setText(documentSnapshot.get("projectOpenFor").toString());
            }
        });

        if (collabStatus.getText().toString().equals("Open") || collabStatus.getText().toString().equals("Completed") || collabStatus.getText().toString().equals("Ongoing")) {
            collabStatus.setTextColor(Color.parseColor("#228B22"));
            collab4VisibleSwitch.setChecked(true);
        } else {
            collabStatus.setTextColor(Color.parseColor("#800000"));
            collab4VisibleSwitch.setChecked(false);
        }

        if (collabStatus.getText().toString().equals("Ending")) {
            endProjectSwitch.setVisibility(View.VISIBLE);
            endProjectSwitch.setChecked(true);
            finishProjectButton.setVisibility(View.GONE);
            noVotes.setVisibility(View.VISIBLE);
            noVotesTV.setVisibility(View.VISIBLE);

            db.collection("CollabProjects").document(projectID).collection("Collaborators")
                    .document("General").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String sVotes = documentSnapshot.get("endVotes").toString() + " / " + documentSnapshot.get("numberPicked").toString();
                    noVotes.setText(sVotes);
                }
            });
        }

        collab4VisibleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (switchFlag != 1) {
                        AlertDialog confirmReCollab = new MaterialAlertDialogBuilder(MyCollabsFourOpenActivity.this)
                                .setTitle("Are you sure you want to put this project up for collaboration again?")
                                .setMessage("Your project will be visible to users looking for collaboration.\n" +
                                        "Please note that this will only add more contributors to your project.\n" +
                                        "Your current collaborators (if any) are still going to be a part of your project.\n\n" +
                                        "Do you want to continue?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                                                .collection("MyCollabs").document(projectID).update("projectStatus", "Open")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        collabStatus.setText("Open");
                                                        collabStatus.setTextColor(Color.parseColor("#228B22"));
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MyCollabsFourOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        collab4VisibleSwitch.setChecked(false);
                                    }
                                }).create();
                        confirmReCollab.show();
                        confirmReCollab.setCancelable(false);
                        confirmReCollab.setCanceledOnTouchOutside(false);
                    } else {
                        switchFlag = 0;
                    }
                } else {
                    AlertDialog confirmUnCollab = new MaterialAlertDialogBuilder(MyCollabsFourOpenActivity.this)
                            .setTitle("Are you sure you want to take the project off the grid?")
                            .setMessage("Your project will no longer be visible to users looking for collaboration.\n" +
                                    "This means that users will no longer be able to apply for the role of contributor for your project.\n\n" +
                                    "Do you want to continue?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                                            .collection("MyCollabs").document(projectID).update("projectStatus", "Closed")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    collabStatus.setText("Closed");
                                                    collabStatus.setTextColor(Color.parseColor("#800000"));
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MyCollabsFourOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switchFlag = 1;
                                    collab4VisibleSwitch.setChecked(true);
                                }
                            }).create();
                    confirmUnCollab.show();
                    confirmUnCollab.setCancelable(false);
                    confirmUnCollab.setCanceledOnTouchOutside(false);
                }
            }
        });

        applicantsLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applicantsLog();
            }
        });

        finishProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishButtonPressed();
            }
        });

        endProjectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    AlertDialog endEndProjectDialog = new MaterialAlertDialogBuilder(MyCollabsFourOpenActivity.this)
                            .setTitle("Are you sure you want to continue with the project?")
                            .setMessage("All your peer's votes to end the project will be set to zero and the project will continue like before.")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    numberVotes = 0;
                                    endProjectSwitch.setVisibility(View.GONE);
                                    finishProjectButton.setVisibility(View.VISIBLE);
                                    noVotes.setVisibility(View.GONE);
                                    noVotesTV.setVisibility(View.GONE);

                                    db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                                            .collection("MyCollabs").document(projectID).update("projectStatus", "Ongoing")
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(MyCollabsFourOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    db.collection("CollabProjects").document(projectID).collection("Collaborators")
                                            .document("General").update("endVotes", numberVotes)
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(MyCollabsFourOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    db.collection("CollabProjects").document(projectID).collection("Collaborators")
                                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (queryDocumentSnapshots.size() > 0) {
                                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                    String userEmail = documentSnapshot.get("emailID").toString();
                                                    db.collection("Users").document("User " + userEmail).collection("PreviousCollabs")
                                                            .document(projectID).update("projectStatus", "Ongoing").addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(MyCollabsFourOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MyCollabsFourOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).setNegativeButton("Cancel", null)
                            .create();
                    endEndProjectDialog.show();
                    endEndProjectDialog.setCancelable(false);
                    endEndProjectDialog.setCanceledOnTouchOutside(false);
                }
            }
        });
    }

    private void applicantsLog() {
        Intent intent = new Intent(MyCollabsFourOpenActivity.this, ApplicantLogActivity.class);
        intent.putExtra("projectID", projectID);
        startActivity(intent);
    }

    private void finishButtonPressed() {
        AlertDialog endProjectDialog = new MaterialAlertDialogBuilder(MyCollabsFourOpenActivity.this)
                .setTitle("Are you sure you want to end the project?")
                .setMessage("Finishing the project will initiate a voting procedure. All your fellow collaborators will have to vote to finish the project." +
                        "\n\nBy doing this, you agree to the following:\n" +
                        "1. You can restart the project only as long as all votes haven't been turned in.\n" +
                        "2. The project isn't considered finished as long as all votes have been turned in.\n" +
                        "3. All collaborators, including yourself, will receive their due credits only after the project has been officially finished.\n\n" +
                        "Once all votes have been turned in, the project will automatically end, credits will be transferred to respective users and everyone will " +
                        "be notified by the app and e-mail.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        numberVotes = 1;
                        endProjectSwitch.setVisibility(View.VISIBLE);
                        endProjectSwitch.setChecked(true);
                        finishProjectButton.setVisibility(View.GONE);
                        noVotes.setVisibility(View.VISIBLE);
                        noVotesTV.setVisibility(View.VISIBLE);

                        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                                .collection("MyCollabs").document(projectID).update("projectStatus", "Ending")
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MyCollabsFourOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        db.collection("CollabProjects").document(projectID).collection("Collaborators")
                                .document("General").update("endVotes", numberVotes)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MyCollabsFourOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        db.collection("CollabProjects").document(projectID).collection("Collaborators")
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.size() > 0) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        String userEmail = documentSnapshot.get("emailID").toString();
                                        db.collection("Users").document("User " + userEmail).collection("PreviousCollabs")
                                                .document(projectID).update("projectStatus", "Ending").addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MyCollabsFourOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MyCollabsFourOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Cancel", null)
                .create();
        endProjectDialog.show();
        endProjectDialog.setCancelable(false);
        endProjectDialog.setCanceledOnTouchOutside(false);
    }
}
