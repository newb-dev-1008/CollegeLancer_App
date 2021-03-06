package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HistoryCollabThreeOpenActivity extends AppCompatActivity {

    private TextView posterName1, projectTitle1, collabDate1, projectDesc1, collabStatus1, fellowCollabsTV, noVotes, noVotesTV;
    private String projectID, noPicked;
    private FirebaseFirestore db;
    private String fellowCollabNames;
    private Switch addVote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_collab3_opencard);

        posterName1 = findViewById(R.id.collab3_name);
        projectTitle1 = findViewById(R.id.collab3_projectTitle);
        collabDate1 = findViewById(R.id.collab3_date);
        projectDesc1 = findViewById(R.id.collab3_projectDesc);
        collabStatus1 = findViewById(R.id.collab3_status);
        fellowCollabsTV = findViewById(R.id.collab3_fellowCollabs);
        noVotes = findViewById(R.id.collab3_noVotes);
        noVotesTV = findViewById(R.id.collab3_noVotesTV);
        addVote = findViewById(R.id.collab3_endProjectSwitch);

        db = FirebaseFirestore.getInstance();

        posterName1.setText(getIntent().getExtras().get("posterName1").toString());
        projectTitle1.setText(getIntent().getExtras().get("projectTitle1").toString());
        collabDate1.setText(getIntent().getExtras().get("collabDate1").toString());
        projectDesc1.setText(getIntent().getExtras().get("projectDesc1").toString());
        collabStatus1.setText(getIntent().getExtras().get("collabStatus1").toString());

        fellowCollabNames = "";

        projectID = getIntent().getExtras().get("projectID1").toString();

        if (collabStatus1.getText().toString().equals("Completed") || collabStatus1.getText().toString().equals("Ongoing")) {
            collabStatus1.setTextColor(Color.parseColor("#228B22"));
        } else {
            collabStatus1.setTextColor(Color.parseColor("#800000"));
        }

        if (collabStatus1.getText().toString().equals("Ending")) {
            noVotesTV.setVisibility(View.VISIBLE);
            noVotes.setVisibility(View.VISIBLE);
            addVote.setVisibility(View.VISIBLE);
            db.collection("CollabProjects").document(projectID).collection("Collaborators").document("General")
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String numVotes = documentSnapshot.get("endVotes").toString();
                    noPicked = documentSnapshot.get("numberPicked").toString();
                    String votes = numVotes + " / " + noPicked;
                    noVotes.setText(votes);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HistoryCollabThreeOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        ArrayList<String> fellowCollabsEmail = new ArrayList<>();
        // ArrayList<String> fellowCollabs = new ArrayList<>();
        db.collection("CollabProjects").document(projectID).collection("Collaborators")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    fellowCollabsEmail.add("User " + documentSnapshot.get("emailID").toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HistoryCollabThreeOpenActivity.this, "Fellow Collab Email not loading. " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        for (String email : fellowCollabsEmail) {
            db.collection("Users").document("User " + email).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            // fellowCollabs.add(documentSnapshot.get("name").toString());
                            fellowCollabNames = fellowCollabNames + documentSnapshot.get("name").toString() + "\n";
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HistoryCollabThreeOpenActivity.this, "Fellow Collab Names not loading. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        fellowCollabsTV.setText(fellowCollabNames);

        addVote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AlertDialog endVoteCheck = new MaterialAlertDialogBuilder(HistoryCollabThreeOpenActivity.this)
                            .setTitle("Are you sure you want to end the project?")
                            .setMessage("The project will end only after all collaborates have voted for finishing the project.\n" +
                                    "Please note that all due credits will be provided automatically after the project has been officially finished.")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int nVotes = Integer.parseInt(noVotes.getText().toString());
                                    nVotes++;
                                    String vote = nVotes + " / " + noPicked;
                                    noVotes.setText(vote);

                                    db.collection("CollabProjects").document(projectID).collection("Collaborators")
                                            .document("General").update("endVotes", noVotes.getText().toString())
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(HistoryCollabThreeOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    dialogInterface.dismiss();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    addVote.setChecked(false);
                                    dialogInterface.dismiss();
                                }
                            }).create();
                    endVoteCheck.show();
                    endVoteCheck.setCancelable(false);
                    endVoteCheck.setCanceledOnTouchOutside(false);

                } else {
                    AlertDialog pullVoteCheck = new MaterialAlertDialogBuilder(HistoryCollabThreeOpenActivity.this)
                            .setTitle("Are you sure you want to remove your vote?")
                            .setMessage("The project will end only after all collaborates have voted for finishing the project.\n" +
                                    "Please note that all due credits will be provided automatically after the project has been officially finished.")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int nVotes = Integer.parseInt(noVotes.getText().toString());
                                    nVotes--;
                                    String vote = nVotes + " / " + noPicked;
                                    noVotes.setText(vote);

                                    db.collection("CollabProjects").document(projectID).collection("Collaborators")
                                            .document("General").update("endVotes", noVotes.getText().toString())
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(HistoryCollabThreeOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    dialogInterface.dismiss();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    addVote.setChecked(true);
                                    dialogInterface.dismiss();
                                }
                            }).create();
                    pullVoteCheck.show();
                    pullVoteCheck.setCancelable(false);
                    pullVoteCheck.setCanceledOnTouchOutside(false);
                }
            }
        });
    }
}
