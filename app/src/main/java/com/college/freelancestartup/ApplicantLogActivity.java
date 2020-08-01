package com.college.freelancestartup;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

class ApplicantLogActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView swipeDownRefreshTV, emptyTV;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_collab4_applicantslog);

        swipeDownRefreshTV = findViewById(R.id.swipeRefreshTVCollab4log);
        emptyTV = findViewById(R.id.log_collab4log_emptyTV);
        recyclerView = findViewById(R.id.log_collabs_recyclerView4log);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutCollab4log);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        applicantLog();
    }

    private void applicantLog() {

    }
}
