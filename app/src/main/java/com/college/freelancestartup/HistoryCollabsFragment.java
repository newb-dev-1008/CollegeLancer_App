package com.college.freelancestartup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class HistoryCollabsFragment extends Fragment {
    private View root;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView collab3RecyclerView;
    private TextView swipeDownRefreshTV, emptyTV;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    private RecyclerView.LayoutManager historyCollab3LayoutManager;
    private RecyclerView.Adapter historyCollab3Adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.student_collab_log_frag, container, false);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        collab3RecyclerView = root.findViewById(R.id.log_collabs_recyclerView);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayoutCollab3);
        emptyTV = root.findViewById(R.id.log_collab3_emptyTV);
        swipeDownRefreshTV = root.findViewById(R.id.swipeRefreshTVCollab3);

        showCollab3();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showCollab3();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return root;
    }

    private void showCollab3(){
        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                .collection("PreviousCollabs").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    emptyTV.setVisibility(View.GONE);
                    ArrayList<HistoryCollabsThree> historyCollabs = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String posterTitle = documentSnapshot.get("posterTitle").toString();
                        String projectTitle = documentSnapshot.get("projectTitle").toString();
                        String collabDate = documentSnapshot.get("collabDate").toString();
                        String projectSkills = documentSnapshot.get("projectSkills").toString();
                        String collabStatus = documentSnapshot.get("collabStatus").toString();
                        String projectOpenFor = documentSnapshot.get("projectOpenFor").toString();
                        String projectDesc = documentSnapshot.get("projectDesc").toString();
                        String projectID = documentSnapshot.get("projectID").toString();
                        historyCollabs.add(new HistoryCollabsThree(posterTitle, projectTitle, projectDesc, collabDate, projectSkills, projectOpenFor, collabStatus, projectID));

                        historyCollab3LayoutManager = new LinearLayoutManager(getContext());
                        historyCollab3Adapter = new HistoryCollabThreeAdapter(historyCollabs);
                        collab3RecyclerView.setHasFixedSize(true);
                        collab3RecyclerView.setLayoutManager(historyCollab3LayoutManager);
                        collab3RecyclerView.setAdapter(historyCollab3Adapter);
                    }
                } else {
                    collab3RecyclerView.setVisibility(View.GONE);
                    swipeDownRefreshTV.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
