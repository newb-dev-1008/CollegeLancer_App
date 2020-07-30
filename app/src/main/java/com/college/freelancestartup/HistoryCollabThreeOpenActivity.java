package com.college.freelancestartup;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

class HistoryCollabThreeOpenActivity extends AppCompatActivity {

    private TextView posterName1, projectTitle1, collabDate1, projectDesc1, collabStatus1, fellowCollabs;
    private String projectID;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_collab3_opencard);

        posterName1 = findViewById(R.id.collab3_name);
        projectTitle1 = findViewById(R.id.collab3_projectTitle);
        collabDate1 = findViewById(R.id.collab3_date);
        projectDesc1 = findViewById(R.id.collab3_projectDesc);
        collabStatus1 = findViewById(R.id.collab3_status);
        fellowCollabs = findViewById(R.id.collab3_fellowCollabs);

        db = FirebaseFirestore.getInstance();

        posterName1.setText(getIntent().getExtras().get("posterName1").toString());
        projectTitle1.setText(getIntent().getExtras().get("projectTitle1").toString());
        collabDate1.setText(getIntent().getExtras().get("collabDate1").toString());
        projectDesc1.setText(getIntent().getExtras().get("projectDesc1").toString());
        collabStatus1.setText(getIntent().getExtras().get("collabStatus1").toString());

        projectID = getIntent().getExtras().get("projectID1").toString();

        if (collabStatus1.getText().toString().equals("Completed")) {
            collabStatus1.setTextColor(Color.parseColor("#228B22"));
        } else {
            collabStatus1.setTextColor(Color.parseColor("#800000"));
        }

        ArrayList<String> fellowCollabsEmail = new ArrayList<>();
        ArrayList<String> fellowCollabs = new ArrayList<>();
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
                Toast.makeText(HistoryCollabThreeOpenActivity.this, "Fellow Collab Email not loading." + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
    }
}
