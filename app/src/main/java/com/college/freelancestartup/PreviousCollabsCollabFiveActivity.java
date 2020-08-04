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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PreviousCollabsCollabFiveActivity extends AppCompatActivity {

    private RecyclerView previousCollabRecyclerView;
    private TextView emptyTV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String userEmail;
    private FirebaseFirestore db;

    private RecyclerView.LayoutManager prevCollabLayoutManager;
    private RecyclerView.Adapter prevCollabAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentcollab5_previouscollabs);

        previousCollabRecyclerView = findViewById(R.id.find_collabs_recyclerView51);
        previousCollabRecyclerView.setVisibility(View.GONE);
        emptyTV = findViewById(R.id.find_collab51_emptyTV);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutCollab51);
        userEmail = getIntent().getExtras().get("userEmail").toString();

        db = FirebaseFirestore.getInstance();

        showCollab51();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showCollab51();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showCollab51() {
        db.collection("Users").document("User " + userEmail).collection("PrevCollabs")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    emptyTV.setVisibility(View.GONE);
                    previousCollabRecyclerView.setVisibility(View.VISIBLE);
                    ArrayList<AllColabsOne> prevCollabs = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String posterTitle = documentSnapshot.get("posterTitle").toString();
                        String projectTitle = documentSnapshot.get("projectTitle").toString();
                        String posterDate = documentSnapshot.get("postDate").toString();
                        String projectSkills = documentSnapshot.get("projectSkills").toString();
                        String projectOpenFor = documentSnapshot.get("projectOpenFor").toString();
                        String projectDesc = documentSnapshot.get("projectDesc").toString();
                        String projectID = documentSnapshot.get("projectID").toString();
                        int flag = 1;
                        prevCollabs.add(new AllColabsOne(posterTitle, projectTitle, projectDesc, posterDate, projectSkills, projectOpenFor, projectID, flag));

                        prevCollabLayoutManager = new LinearLayoutManager(PreviousCollabsCollabFiveActivity.this);
                        prevCollabAdapter = new AllColabsOneAdapter(prevCollabs);
                        previousCollabRecyclerView.setHasFixedSize(true);
                        previousCollabRecyclerView.setLayoutManager(prevCollabLayoutManager);
                        previousCollabRecyclerView.setAdapter(prevCollabAdapter);
                    }
                } else {
                    previousCollabRecyclerView.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PreviousCollabsCollabFiveActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
