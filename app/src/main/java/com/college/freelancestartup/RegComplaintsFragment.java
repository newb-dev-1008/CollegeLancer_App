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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RegComplaintsFragment extends Fragment {

    private View root;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private RecyclerView regComplaintsRecyclerView;
    private TextView complaintIDTV, complaintDescTV, complaintID, complaintDesc;
    private RecyclerView.Adapter regComplaintsAdapter;
    private RecyclerView.LayoutManager regComplaintsLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.reg_complaints_frag, container, false);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        regComplaintsRecyclerView = root.findViewById(R.id.reg_complaints_recyclerView);
        complaintIDTV = root.findViewById(R.id.complaintIDTV);
        complaintDescTV = root.findViewById(R.id.complaintDescTV);
        complaintDesc = root.findViewById(R.id.complaint);
        complaintID = root.findViewById(R.id.complaintID);

        checkRegisteredComplaints();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkRegisteredComplaints();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }

    private void checkRegisteredComplaints() {
        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                .collection("Complaints").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0){
                    ArrayList<RegisteredComplaints> registeredComplaints = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String complaintTitle1 = documentSnapshot.get("complaintTitle").toString();
                        String complaintDate1 = documentSnapshot.get("complaintDate").toString();
                        String complaintStatus1 = documentSnapshot.get("complaintStatus").toString();
                        String complaint1 = documentSnapshot.get("complaint").toString();
                        String complaintID1 = documentSnapshot.get("complaintID").toString();
                        registeredComplaints.add(new RegisteredComplaints(complaintTitle1, complaintDate1, complaintStatus1, complaint1, complaintID1));
                    }
                    regComplaintsRecyclerView.setVisibility(View.VISIBLE);

                    regComplaintsLayoutManager = new LinearLayoutManager(getContext());
                    regComplaintsAdapter = new RegComplaintsAdapter(registeredComplaints);
                    regComplaintsRecyclerView.setHasFixedSize(true);
                    regComplaintsRecyclerView.setLayoutManager(regComplaintsLayoutManager);
                    regComplaintsRecyclerView.setAdapter(regComplaintsAdapter);
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
