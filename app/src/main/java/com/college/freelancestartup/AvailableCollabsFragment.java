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

public class AvailableCollabsFragment extends Fragment {

    private View root;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView collab5RecyclerView;
    private TextView swipeDownRefreshTV, emptyTV;

    private RecyclerView.LayoutManager availableCollab5LayoutManager;
    private RecyclerView.Adapter availableCollab5Adapter;
    private int flag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.student_collab_available_frag, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        flag = 0;
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayoutCollab5);
        swipeDownRefreshTV = root.findViewById(R.id.swipeRefreshTVCollab5);
        emptyTV = root.findViewById(R.id.log_collab5_emptyTV);
        collab5RecyclerView = root.findViewById(R.id.log_collabs_recyclerView5);

        showCollabs5();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showCollabs5();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }

    private void showCollabs5(){
        // Work on the Querying part for picking Users with their set status
        db.collection("Users").whereEqualTo("studentStatus", "Looking for collaborators").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() > 0) {
                            emptyTV.setVisibility(View.GONE);
                            ArrayList<AvailableCollabsFive> availableCollabs = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String name = documentSnapshot.get("name").toString();
                                String semester = documentSnapshot.get("studentSemester").toString();
                                String skills = documentSnapshot.get("studentSkills").toString();
                                String department = documentSnapshot.get("department").toString();
                                String previousCollabs = documentSnapshot.get("numberCollabs").toString();
                                String previousProjects = documentSnapshot.get("numberProjects").toString();
                                String userEmail = documentSnapshot.get("email").toString();
                                availableCollabs.add(new AvailableCollabsFive(name, department, skills, previousCollabs, previousProjects, semester, userEmail, flag, "Zero"));

                                availableCollab5LayoutManager = new LinearLayoutManager(getContext());
                                availableCollab5Adapter = new AvailableCollabsFiveAdapter(availableCollabs);
                                collab5RecyclerView.setHasFixedSize(true);
                                collab5RecyclerView.setLayoutManager(availableCollab5LayoutManager);
                                collab5RecyclerView.setAdapter(availableCollab5Adapter);
                            }
                        } else {
                            collab5RecyclerView.setVisibility(View.GONE);
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
