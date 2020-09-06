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

public class RequestLogCollabFourActivity extends AppCompatActivity {

    private RecyclerView requestLogRecyclerView;
    private TextView emptyTV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String userEmail;
    private FirebaseFirestore db;

    private RecyclerView.LayoutManager requestLogLayoutManager;
    private RecyclerView.Adapter requestLogCollabAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_collab_requestlog);

        requestLogRecyclerView = findViewById(R.id.log_collabsRequestsLog_recyclerView5);
        requestLogRecyclerView.setVisibility(View.GONE);
        emptyTV = findViewById(R.id.log_collab5RequestsLog_emptyTV);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutCollab5RequestsLog);
        userEmail = getIntent().getExtras().get("userEmail").toString();

        db = FirebaseFirestore.getInstance();

        showRequestsLog();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showRequestsLog();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showRequestsLog() {
        db.collection("Users").document("User " + userEmail).collection("PrevCollabs")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    emptyTV.setVisibility(View.GONE);
                    requestLogRecyclerView.setVisibility(View.VISIBLE);
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

                        requestLogLayoutManager = new LinearLayoutManager(RequestLogCollabFourActivity.this);
                        requestLogCollabAdapter = new AllColabsOneAdapter(prevCollabs);
                        requestLogRecyclerView.setHasFixedSize(true);
                        requestLogRecyclerView.setLayoutManager(requestLogLayoutManager);
                        requestLogRecyclerView.setAdapter(requestLogCollabAdapter);
                    }
                } else {
                    requestLogRecyclerView.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RequestLogCollabFourActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
