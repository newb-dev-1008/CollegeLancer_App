package com.college.freelancestartup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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
import java.util.Random;

public class StudentAddProjectForCollab extends AppCompatActivity {

    EditText projectTitle, projectOpenFor, projectDesc;
    AutoCompleteTextView projectSkills;
    String posterTitle, posterEmail, postDate, projectID, projectStatus;
    Calendar calendar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    int numberPicked, numberApps, endVotes;
    MaterialButton addExistingButton, addNewButton;

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
        addExistingButton = findViewById(R.id.addExistingProjectButton);
        addNewButton = findViewById(R.id.addNewProjectButton);
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

        addExistingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // finish this
            }
        });

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewProjectFunction();
            }
        });

    }

    private void addNewProjectFunction() {
        AlertDialog.Builder addProjectDialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Add project?")
                .setMessage("Your project will be out in the open for developers to see and apply for collaboration.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        postDate = calendar.getTime().toString();
                        projectStatus = "Open";
                    }
                })
    }

    private String generateProjectID() {
        StringBuilder sb = new StringBuilder(30);
        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new Random();
        for (int i = 0; i < 30; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }

}
