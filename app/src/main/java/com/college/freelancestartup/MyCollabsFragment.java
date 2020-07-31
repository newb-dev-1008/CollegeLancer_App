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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

class MyCollabsFragment extends Fragment {

    private View root;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView collab4RecyclerView;
    private TextView swipeDownRefreshTV, emptyTV;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    private RecyclerView.LayoutManager myCollabs4LayoutManager;
    private RecyclerView.Adapter myCollabs4Adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.student_collab_mycollabs_frag, container, false);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        swipeDownRefreshTV = root.findViewById(R.id.swipeRefreshTVCollab4);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayoutCollab4);
        emptyTV = root.findViewById(R.id.log_collab4_emptyTV);
        collab4RecyclerView = root.findViewById(R.id.mycollabs_collabs4_recyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showCollab4();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return root;
    }

    private void showCollab4(){
        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                .collection("MyCollabs").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    emptyTV.setVisibility(View.GONE);
                    ArrayList<MyCollabsFour> myCollabs = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String posterTitle = documentSnapshot.get("posterTitle").toString();
                        String projectTitle = documentSnapshot.get("projectTitle").toString();
                        String postedDate = documentSnapshot.get("postDate").toString();
                        String collabStatus = documentSnapshot.get("collabStatus").toString();
                        String projectDesc = documentSnapshot.get("projectDesc").toString();
                        String projectVisible = documentSnapshot.get("projectVisible").toString();
                        String numberApplicants = documentSnapshot.get("numApplicants").toString();
                        String projectID = documentSnapshot.get("projectID").toString();
                        String projectSkills = documentSnapshot.get("projectSkills").toString();
                        String projectOpenFor = documentSnapshot.get("projectOpenFor").toString();
                        String numberSelectedApplicants = documentSnapshot.get("numSelectedApplicants").toString();
                        myCollabs.add(new MyCollabsFour(posterTitle, projectTitle, projectDesc, postedDate, collabStatus, projectVisible, numberApplicants, numberSelectedApplicants, projectSkills, projectOpenFor, projectID));

                        myCollabs4LayoutManager = new LinearLayoutManager(getContext());
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
