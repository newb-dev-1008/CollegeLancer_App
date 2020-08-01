package com.college.freelancestartup;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
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
import com.google.android.material.dialog.MaterialDialogs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RequestCollabTwoOpenActivity extends AppCompatActivity {

    private TextView posterName1, projectTitle1, postedDate1, projectDesc1, openFor1, skills1, projectStatus1;
    private MaterialButton messageButton, applyButton, rejectButton;
    private EditText rejectEditText;
    private View view;
    private String projectID;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private int numberApps, numberPicked;

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
        projectStatus1 = findViewById(R.id.collab2_projectStatus);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        applyButton = findViewById(R.id.collab2_applyBtn);
        messageButton = findViewById(R.id.collab2_messageBtn);
        rejectButton = findViewById(R.id.collab2_rejectBtn);

        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.apply_collab1_dialogedittext, null);

        rejectEditText = view.findViewById(R.id.applyEditText);
        rejectEditText.setHint("Let them know why you cannot collaborate. (Optional)");

        posterName1.setText(getIntent().getExtras().get("posterName1").toString());
        projectTitle1.setText(getIntent().getExtras().get("projectTitle1").toString());
        postedDate1.setText(getIntent().getExtras().get("postedDate1").toString());
        projectDesc1.setText(getIntent().getExtras().get("projectDesc1").toString());
        openFor1.setText(getIntent().getExtras().get("openFor1").toString());
        skills1.setText(getIntent().getExtras().get("skills1").toString());
        projectStatus1.setText(getIntent().getExtras().get("projectStatus1").toString());

        projectID = getIntent().getExtras().get("projectID1").toString();

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

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog rejectCollab2 = new MaterialAlertDialogBuilder(RequestCollabTwoOpenActivity.this)
                        .setView(view)
                        .setTitle("Are you sure you want to reject this offer?")
                        .setMessage("The Project Head will be notified about your inability to contribute.\n" +
                                "However, he can send you a request again.")
                        .setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                rejectButtonPressed();
                            }
                        }).setNegativeButton("Cancel", null)
                        .create();
                rejectCollab2.show();
                rejectCollab2.setCancelable(true);
                rejectCollab2.setCanceledOnTouchOutside(true);
            }
        });
    }

    private void applyButtonPressed() {
        // Notification to project poster

        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                .collection("CollabRequests").document("projectID").update("projectStatus", "Accepted");
        projectStatus1.setText("Accepted");
        projectStatus1.setTextColor(Color.parseColor("#228B22"));
        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                .collection("CollabRequests").document("projectID").delete();

        Map<String, Object> userReqPicked = new HashMap<>();
        userReqPicked.put("emailID", firebaseAuth.getCurrentUser().getEmail());
        userReqPicked.put("picked", "Selected, currently collaborating");
        userReqPicked.put("applicationArticle", "I was requested to contribute to this project.");

        db.collection("CollabProjects").document(projectID).collection("Collaborators")
                .document("User " + firebaseAuth.getCurrentUser().getEmail()).set(userReqPicked)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RequestCollabTwoOpenActivity.this, "Get excited! The project head has received your application and will review it soon.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RequestCollabTwoOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        db.collection("CollabProjects").document(projectID).collection("Collaborators")
                .document("General").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                numberApps = Integer.parseInt(documentSnapshot.get("numberApps").toString());
                numberPicked = Integer.parseInt(documentSnapshot.get("numberPicked").toString());

                numberPicked = numberPicked + 1;
                db.collection("CollabProjects").document(projectID).collection("Collaborators")
                        .document("General").update("numberPicked", numberPicked).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RequestCollabTwoOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RequestCollabTwoOpenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rejectButtonPressed() {
        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                .collection("CollabRequests").document("projectID").update("projectStatus", "Rejected");

        projectStatus1.setText("Rejected");
        projectStatus1.setTextColor(Color.parseColor("#800000"));
        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                .collection("CollabRequests").document("projectID").delete();
        // Send notification to project poster about the guy rejecting
    }
}
