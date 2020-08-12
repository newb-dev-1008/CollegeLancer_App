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

public class AllCollabsFragment extends Fragment {

    private View root;
    private FirebaseFirestore db;
    private RecyclerView collab1RecyclerView;
    private TextView swipeDownRefreshTV, emptyTV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAuth firebaseAuth;

    private RecyclerView.LayoutManager allCollab1LayoutManager;
    private RecyclerView.Adapter allCollab1Adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.student_find_collabs_frag, container, false);

        db = FirebaseFirestore.getInstance();

        collab1RecyclerView = root.findViewById(R.id.find_collabs_recyclerView1);
        swipeDownRefreshTV = root.findViewById(R.id.swipeRefreshTVCollab1);
        emptyTV = root.findViewById(R.id.find_collab1_emptyTV);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayoutCollab1);
        firebaseAuth = FirebaseAuth.getInstance();

        showCollab1();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showCollab1();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return root;
    }

    private void showCollab1() {
        db.collection("CollabProjects").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0){
                    emptyTV.setVisibility(View.GONE);
                    ArrayList<AllColabsOne> allColabs = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.get("posterEmail").equals(firebaseAuth.getCurrentUser().getEmail().toString())) {
                            continue;
                        } else {
                            String posterTitle = documentSnapshot.get("posterTitle").toString();
                            String projectTitle = documentSnapshot.get("projectTitle").toString();
                            String posterDate = documentSnapshot.get("postDate").toString();
                            String projectSkills = documentSnapshot.get("projectSkills").toString();
                            String projectOpenFor = documentSnapshot.get("projectOpenFor").toString();
                            String projectDesc = documentSnapshot.get("projectDesc").toString();
                            String projectID = documentSnapshot.get("projectID").toString();

                            int flag = 0;
                            allColabs.add(new AllColabsOne(posterTitle, projectTitle, projectDesc, posterDate, projectSkills, projectOpenFor, projectID, flag));
                        }


                        allCollab1LayoutManager = new LinearLayoutManager(getContext());
                        allCollab1Adapter = new AllColabsOneAdapter(allColabs);
                        collab1RecyclerView.setHasFixedSize(true);
                        collab1RecyclerView.setLayoutManager(allCollab1LayoutManager);
                        collab1RecyclerView.setAdapter(allCollab1Adapter);
                    }
                } else {
                    collab1RecyclerView.setVisibility(View.GONE);
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


