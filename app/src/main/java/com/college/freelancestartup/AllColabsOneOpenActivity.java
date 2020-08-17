package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class AllColabsOneOpenActivity extends AppCompatActivity {

    private TextView posterName1, projectTitle1, postedDate1, projectDesc1, openFor1, skills1;
    private MaterialButton messageButton, applyButton;
    private View view;
    // private EditText applyEditText;
    private String projectID, posterEmail, posterName, projectTitle, postedDate, projectDesc, openFor, skills, applicationArticle;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private int numberApps, flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_collab1_opencard);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.apply_collab1_dialogedittext, null);

        // applyEditText = view.findViewById(R.id.applyEditText);

        posterName1 = findViewById(R.id.collab1_name);
        projectTitle1 = findViewById(R.id.collab1_projectTitle);
        postedDate1 = findViewById(R.id.collab1_date);
        projectDesc1 = findViewById(R.id.collab1_projectDesc);
        openFor1 = findViewById(R.id.collab1_openFor);
        skills1 = findViewById(R.id.collab1_skills);
        applyButton = findViewById(R.id.collab1_applyBtn);
        messageButton = findViewById(R.id.collab1_messageBtn);

        // posterName1.setText(getIntent().getExtras().get("posterName1").toString());
        // projectTitle1.setText(getIntent().getExtras().get("projectTitle1").toString());
        // postedDate1.setText(getIntent().getExtras().get("postedDate1").toString());
        // projectDesc1.setText(getIntent().getExtras().get("projectDesc1").toString());
        // openFor1.setText(getIntent().getExtras().get("openFor1").toString());
        // skills1.setText(getIntent().getExtras().get("skills1").toString());
        projectID = getIntent().getExtras().get("projectID1").toString();
        flag = Integer.parseInt(getIntent().getExtras().get("flag1").toString());

        if (flag == 0) {
            applyButton.setVisibility(View.VISIBLE);
            messageButton.setVisibility(View.VISIBLE);
        } else {
            applyButton.setVisibility(View.GONE);
            messageButton.setVisibility(View.GONE);
        }

        // Check if the project is his own; if it is, then don't show
        db.collection("CollabProjects").document(projectID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                posterEmail = documentSnapshot.get("posterEmail").toString();
                posterName = documentSnapshot.get("posterTitle").toString();
                projectTitle = documentSnapshot.get("projectTitle").toString();
                postedDate = documentSnapshot.get("postDate").toString();
                projectDesc = documentSnapshot.get("projectDesc").toString();
                openFor = documentSnapshot.get("projectOpenFor").toString();
                skills = documentSnapshot.get("projectSkills").toString();

                posterName1.setText(posterName);
                postedDate1.setText(postedDate);
                projectTitle1.setText(projectTitle);
                projectDesc1.setText(projectDesc);
                openFor1.setText(openFor);
                skills1.setText(skills);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AllColabsOneOpenActivity.this, "Error: Couldn't access database.\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText articleEditText = new EditText(AllColabsOneOpenActivity.this);
                articleEditText.setHint("Enter your application here");
                articleEditText.setEnabled(true);
                AlertDialog applyCollab1 = new MaterialAlertDialogBuilder(AllColabsOneOpenActivity.this)
                        .setView(articleEditText)
                        .setTitle("Apply for collaboration")
                        .setMessage("Write a brief description about yourself and how you'd be contributing to the project.")
                        .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                applicationArticle = articleEditText.getText().toString();
                                if (TextUtils.isEmpty(applicationArticle)) {
                                    Toast.makeText(AllColabsOneOpenActivity.this, "Please enter a short description.", Toast.LENGTH_SHORT).show();
                                } else {
                                    completeApplicationCollab1();
                                }
                            }
                        }).setNegativeButton("Cancel", null)
                        .create();
                applyCollab1.show();
                applyCollab1.setCancelable(false);
                applyCollab1.setCanceledOnTouchOutside(false);
            }
        });
    }

    private void completeApplicationCollab1(){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("emailID", firebaseAuth.getCurrentUser().getEmail());
        userMap.put("applicationArticle", applicationArticle);
        userMap.put("picked", null);
        db.collection("CollabProjects").document(projectID).collection("Collaborators")
                .document("User " + firebaseAuth.getCurrentUser().getEmail()).set(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AllColabsOneOpenActivity.this, "Get excited! The project head has received your application and will review it soon.", Toast.LENGTH_LONG).show();
                        // Send notification to project head
                        // Remove project from the list of projects (for the applicant)
                        Intent intent = new Intent(AllColabsOneOpenActivity.this, StudentMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AllColabsOneOpenActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        db.collection("CollabProjects").document(projectID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                numberApps = Integer.parseInt(documentSnapshot.get("numberApps").toString());
                numberApps = numberApps + 1;
                db.collection("CollabProjects").document(projectID).update("numberApps", numberApps)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AllColabsOneOpenActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                db.collection("Users").document("User " + posterEmail).collection("MyCollabs")
                        .document(projectID).update("numberApps", numberApps);

                db.collection("Users").document("User " + posterEmail).collection("Projects")
                        .document(projectID).update("numberApps", numberApps);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AllColabsOneOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        /*
        db.collection("CollabProjects").document(projectID).collection("Collaborators")
                .document("General").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                numberApps = Integer.parseInt(documentSnapshot.get("numberApps").toString());
                numberApps = numberApps + 1;
                db.collection("CollabProjects").document(projectID).collection("Collaborators")
                        .document("General").update("numberApps", numberApps).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AllColabsOneOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                db.collection("Users").document("User " + posterEmail).collection("MyCollabs")
                        .document(projectID).update("numberApps", numberApps);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AllColabsOneOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        */
    }
}
