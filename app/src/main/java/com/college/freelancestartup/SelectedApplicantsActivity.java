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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SelectedApplicantsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyTV, swipeDownRefreshTV;
    private String name;
    private String semester;
    private String skills;
    private String department;
    private String previousCollabs;
    private String previousProjects;

    private RecyclerView.LayoutManager showApplicantsLayoutManager;
    private RecyclerView.Adapter showApplicantsAdapter;
    private FirebaseFirestore db;
    private String projectID;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_applicants_collab4);

        if (savedInstanceState != null) {
            projectID = getIntent().getExtras().get("projectID").toString();
        }

        recyclerView = findViewById(R.id.find_recyclerViewSelectedApplicant);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutSelectedApplicants);
        swipeDownRefreshTV = findViewById(R.id.swipeRefreshTVSelectedApplicants);

        emptyTV = findViewById(R.id.find__emptyTVSelectedApplicants);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showSelectedApplicants();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showSelectedApplicants() {
        db.collection("CollabProjects").document(projectID).collection("Collaborators")
                .whereEqualTo("picked", "Selected").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0){
                    emptyTV.setVisibility(View.GONE);
                    ArrayList<MyCollabsFour> selectedApp = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String userEmail = documentSnapshot.get("emailID").toString();
                        db.collection("Users").document("User " + userEmail)
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                        selectedApp.add(new AvailableCollabsFive(name, department, skills, previousCollabs, previousProjects, semester, userEmail, flag, projectID));

                        showApplicantsLayoutManager = new LinearLayoutManager(SelectedApplicantsActivity.this);
                        showApplicantsAdapter = new MyCollabFourAdapter(selectedApp);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(showApplicantsLayoutManager);
                        recyclerView.setAdapter(showApplicantsAdapter);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    swipeDownRefreshTV.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SelectedApplicantsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
