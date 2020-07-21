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
    private TextView emptyRegComplaintsTV;
    private RecyclerView.Adapter regComplaintsAdapter;
    private RecyclerView.LayoutManager regComplaintsLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.reg_complaints_frag, container, false);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        regComplaintsRecyclerView = root.findViewById(R.id.reg_complaints_recyclerView);
        emptyRegComplaintsTV = root.findViewById(R.id.noComplaintsTV);

        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                .collection("Complaints").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0){
                    // Also, figure out how opening the card will work
                    ArrayList<RegisteredComplaints> registeredComplaints = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String complaintTitle1 = documentSnapshot.get("complaintTitle").toString();
                        String complaintDate1 = documentSnapshot.get("complaintDate").toString();
                        String complaintStatus1 = documentSnapshot.get("complaintStatus").toString();
                        registeredComplaints.add(new RegisteredComplaints(complaintTitle1, complaintDate1, complaintStatus1));
                    }
                    emptyRegComplaintsTV.setVisibility(View.GONE);
                    regComplaintsRecyclerView.setVisibility(View.VISIBLE);

                    regComplaintsLayoutManager = new LinearLayoutManager(getContext());
                    regComplaintsAdapter = new RegComplaintsAdapter(registeredComplaints);
                    regComplaintsRecyclerView.setHasFixedSize(true);
                    regComplaintsRecyclerView.setLayoutManager(regComplaintsLayoutManager);
                    regComplaintsRecyclerView.setAdapter(regComplaintsAdapter);
                } else {
                    emptyRegComplaintsTV.setVisibility(View.VISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}
