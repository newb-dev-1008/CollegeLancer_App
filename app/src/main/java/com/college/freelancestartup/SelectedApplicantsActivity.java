package com.college.freelancestartup;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SelectedApplicantsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

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
                .document("User " + )
    }
}
