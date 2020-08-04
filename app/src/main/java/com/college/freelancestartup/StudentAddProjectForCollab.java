package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import java.util.Random;

public class StudentAddProjectForCollab extends AppCompatActivity {

    private EditText projectTitle, projectOpenFor, projectDesc;
    private AutoCompleteTextView projectSkills;
    private String posterTitle, posterEmail, postDate, projectID, projectStatus, skillset;
    private Calendar calendar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private int numberPicked, numberApps, endVotes;
    private MaterialButton addExistingButton, addNewButton;

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
                        addNewProjectRealFunction();
                    }
                })
    }

    private void addNewProjectRealFunction() {
        postDate = calendar.getTime().toString();
        projectStatus = "Open";
        String projeccID = generateProjectID();
        db.collection("CollabProjects").document(projeccID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            addNewProjectRealFunction();
                        } else {
                            projectID = projeccID;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentAddProjectForCollab.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Map<String, Object> addProjectMap = new HashMap<>();
        addProjectMap.put("numberApps", numberApps);
        addProjectMap.put("numberPicked", numberPicked);
        addProjectMap.put("endVotes", endVotes);
        addProjectMap.put("projectID", projectID);
        addProjectMap.put("posterTitle", posterTitle);
        addProjectMap.put("posterEmail", posterEmail);
        addProjectMap.put("postDate", postDate);
        addProjectMap.put("projectOpenFor", projectOpenFor.getText().toString());
        addProjectMap.put("projectSkills", skillset);
        addProjectMap.put("projectDesc", projectDesc);
        addProjectMap.put("projectTitle", projectTitle);
        addProjectMap.put("projectStatus", projectStatus);


        db.collection("CollabProjects").document(projectID).set(addProjectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(StudentAddProjectForCollab.this, "Your project has been added. Expect some calls and applications!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StudentAddProjectForCollab.this, StudentMainActivity.class);
                intent.putExtra("Go_to_fragment_addProj", "Go to fragment addProj");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
