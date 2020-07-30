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

public class RequestsCollabsFragment extends Fragment {

    private View root;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private RecyclerView collab2RecyclerView;
    private TextView swipeDownRefreshTV, emptyTV;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView.LayoutManager requestCollab2LayoutManager;
    private RecyclerView.Adapter requestCollab2Adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.student_collab_requests_frag, container, false);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        collab2RecyclerView = root.findViewById(R.id.request_collabs_recyclerView2);
        swipeDownRefreshTV = root.findViewById(R.id.swipeRefreshTVCollab2);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayoutCollab2);

        showCollab2();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showCollab2();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return root;
    }

    private void showCollab2(){
        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                .collection("CollabRequests").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0){
                    emptyTV.setVisibility(View.GONE);
                    ArrayList<RequestCollabTwo> requestCollab = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String posterTitle = documentSnapshot.get("posterTitle").toString();
                        String projectTitle = documentSnapshot.get("projectTitle").toString();
                        String posterDate = documentSnapshot.get("postDate").toString();
                        String projectSkills = documentSnapshot.get("projectSkills").toString();
                        String projectOpenFor = documentSnapshot.get("projectOpenFor").toString();
                        String projectDesc = documentSnapshot.get("projectDesc").toString();
                        String projectStatus = documentSnapshot.get("projectStatus").toString();
                        requestCollab.add(new RequestCollabTwo(posterTitle, projectTitle, projectDesc, posterDate, projectSkills, projectOpenFor, projectStatus));

                        requestCollab2LayoutManager = new LinearLayoutManager(getContext());
                        requestCollab2Adapter = new RequestCollabTwoAdapter(requestCollab);
                        collab2RecyclerView.setHasFixedSize(true);
                        collab2RecyclerView.setLayoutManager(requestCollab2LayoutManager);
                        collab2RecyclerView.setAdapter(requestCollab2Adapter);
                    }
                } else {
                    collab2RecyclerView.setVisibility(View.GONE);
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
