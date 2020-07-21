package com.college.freelancestartup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RegComplaintsFragment extends Fragment {

    private View root;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private RecyclerView regComplaintsRecyclerView;
    private TextView emptyRegComplaintsTV;

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

                }
            }
        })
        ArrayList<RegisteredComplaints> registeredComplaints = new ArrayList<>();
        return root;
    }
}
