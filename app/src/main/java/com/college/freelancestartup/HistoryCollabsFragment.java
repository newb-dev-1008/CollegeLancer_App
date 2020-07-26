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

import com.google.firebase.firestore.FirebaseFirestore;

class HistoryCollabsFragment extends Fragment {
    private View root;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView collabRecyclerView;
    private TextView swipeDownRefreshTV, emptyTV;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.student_collab_log_frag, container, false);
        db = FirebaseFirestore.getInstance();

        collabRecyclerView = root.findViewById(R.id.log_collabs_recyclerView);
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
}
