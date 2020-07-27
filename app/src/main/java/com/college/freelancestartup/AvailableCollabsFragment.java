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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

class AvailableCollabsFragment extends Fragment {

    private View root;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView collab4RecyclerView;
    private TextView swipeDownRefreshTV, emptyTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.student_collab_available_frag, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayoutCollab5);
        swipeDownRefreshTV = root.findViewById(R.id.swipeRefreshTVCollab5);
        emptyTV = root.findViewById(R.id.log_collab5_emptyTV);
        collab4RecyclerView = root.findViewById(R.id.log_collabs_recyclerView5);

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

    }
}
