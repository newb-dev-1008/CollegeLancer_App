package com.college.freelancestartup;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.firestore.FirebaseFirestore;

public class RequestLogCollabFourActivity extends AppCompatActivity {

    private RecyclerView requestLogRecyclerView;
    private TextView emptyTV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String userEmail;
    private FirebaseFirestore db;

    private RecyclerView.LayoutManager requestLogLayoutManager;
    private RecyclerView.Adapter requestLogCollabAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_collab_requestlog);

        requestLogRecyclerView = findViewById(R.id.log_collabsRequestsLog_recyclerView5);
        requestLogRecyclerView.setVisibility(View.GONE);
        emptyTV = findViewById(R.id.log_collab5RequestsLog_emptyTV);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutCollab5RequestsLog);
        userEmail = getIntent().getExtras().get("userEmail").toString();

        db = FirebaseFirestore.getInstance();
    }
}
