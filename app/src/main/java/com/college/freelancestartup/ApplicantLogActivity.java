package com.college.freelancestartup;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ApplicantLogActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView swipeDownRefreshTV, emptyTV;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private int flag, flagLog;
    private String projectID, name, semester, skills, department, previousCollabs, previousProjects;
    private RecyclerView.LayoutManager applicantLog5LayoutManager;
    private RecyclerView.Adapter applicantLog5Adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_collab4_applicantslog);

        swipeDownRefreshTV = findViewById(R.id.swipeRefreshTVCollab4log);
        emptyTV = findViewById(R.id.log_collab4log_emptyTV);
        recyclerView = findViewById(R.id.log_collabs_recyclerView4log);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutCollab4log);

        if (savedInstanceState == null) {
            projectID = getIntent().getExtras().get("projectID").toString();
        }
        // flagLog = Integer.parseInt(getIntent().getExtras().get("flagLog").toString());

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        flag = 1;
        applicantLog();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                applicantLog();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void applicantLog() {
        db.collection("CollabProjects").document(projectID).collection("Collaborators").whereEqualTo("picked", "Pending").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() > 0) {
                            emptyTV.setVisibility(View.GONE);
                            ArrayList<AvailableCollabsFive> availableCollabs = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String userEmail = documentSnapshot.get("emailID").toString();
                                db.collection("Users").document("User " + userEmail).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                name = documentSnapshot.get("name").toString();
                                                semester = documentSnapshot.get("studentSemester").toString();
                                                skills = documentSnapshot.get("studentSkills").toString();
                                                department = documentSnapshot.get("department").toString();
                                                previousCollabs = documentSnapshot.get("numberCollabs").toString();
                                                previousProjects = documentSnapshot.get("numberProjects").toString();
                                            }
                                        });
                                availableCollabs.add(new AvailableCollabsFive(name, department, skills, previousCollabs, previousProjects, semester, userEmail, flag, projectID));

                                applicantLog5LayoutManager = new LinearLayoutManager(ApplicantLogActivity.this);
                                applicantLog5Adapter = new ApplicantLogAdapter(availableCollabs);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(applicantLog5LayoutManager);
                                recyclerView.setAdapter(applicantLog5Adapter);
                            }
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            swipeDownRefreshTV.setVisibility(View.GONE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ApplicantLogActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
