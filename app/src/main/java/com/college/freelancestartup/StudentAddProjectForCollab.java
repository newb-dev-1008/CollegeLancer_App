package com.college.freelancestartup;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class StudentAddProjectForCollab extends AppCompatActivity {

    EditText projectTitle, projectOpenFor, projectDesc;
    AutoCompleteTextView projectSkills;
    String posterTitle, posterEmail, postDate, projectID, projectStatus;
    Calendar calendar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    int numberPicked, numberApps, endVotes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_addnew_project_forcollab);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        projectTitle = findViewById(R.id.projectTitleEditText);
        projectOpenFor = findViewById(R.id.projectOpenForEditText);
        projectDesc = findViewById(R.id.projDescEditText);
        projectSkills = findViewById(R.id.proj_Skills);
        numberApps = 0;
        numberPicked = 0;
        endVotes = 0;
        calendar = Calendar.getInstance();

        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                posterTitle = documentSnapshot.get("name").toString();
                posterEmail = documentSnapshot.get("email").toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentAddProjectForCollab.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
