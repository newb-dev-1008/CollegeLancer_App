package com.college.freelancestartup;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

class AvailableCollabsFiveOpenActivity extends AppCompatActivity {

    private TextView name1, personDepartment1, personSemester1, numberCollabs1, numberProjects1, skills1;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private String userEmail;
    private MaterialButton previousCollabsButton, messageButton, viewProfileButton, requestButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_collab5_opencard);

        db = FirebaseFirestore.getInstance();

        name1 = findViewById(R.id.collab5_name);
        personDepartment1 = findViewById(R.id.collab5_personDepartment);
        personSemester1 = findViewById(R.id.collab5_personSemester);
        numberCollabs1 = findViewById(R.id.collab5_collaborations);
        numberProjects1 = findViewById(R.id.collab5_projectsCompleted);
        skills1 = findViewById(R.id.collab5_skills);

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
    }

    private void previousCollabsPressed() {

    }
}
