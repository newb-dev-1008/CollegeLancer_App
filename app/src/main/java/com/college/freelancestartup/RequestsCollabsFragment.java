package com.college.freelancestartup;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

class RequestsCollabsFragment extends Fragment {

    private View root;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private RecyclerView collab2RecyclerView;
    private TextView swipeDownRefreshTV, emptyTV;
    private SwipeRefreshLayout swipeRefreshLayout;


}
