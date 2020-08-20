package com.college.freelancestartup;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SelectedApplicantsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyTV;

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
                    ArrayList<MyCollabsFour> myCollabs = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String name = documentSnapshot.get("name").toString();
                        String semester = documentSnapshot.get("studentSemester").toString();
                        String skills = documentSnapshot.get("studentSkills").toString();
                        String department = documentSnapshot.get("department").toString();
                        String previousCollabs = documentSnapshot.get("numberCollabs").toString();
                        String previousProjects = documentSnapshot.get("numberProjects").toString();
                        myCollabs.add(new MyCollabsFour(posterTitle, projectTitle, projectDesc, postedDate, collabStatus, projectVisible, numberApplicants, numberSelectedApplicants, projectSkills, projectOpenFor, projectID));

                        myCollabs4LayoutManager = new LinearLayoutManager(SelectedApplicantsActivity.this);
                        myCollabs4Adapter = new MyCollabFourAdapter(myCollabs);
                        collab4RecyclerView.setHasFixedSize(true);
                        collab4RecyclerView.setLayoutManager(myCollabs4LayoutManager);
                        collab4RecyclerView.setAdapter(myCollabs4Adapter);
                    }
                } else {
                    collab4RecyclerView.setVisibility(View.GONE);
                    swipeDownRefreshTV.setVisibility(View.GONE);
                }
                }
            }
        })
    }
}
