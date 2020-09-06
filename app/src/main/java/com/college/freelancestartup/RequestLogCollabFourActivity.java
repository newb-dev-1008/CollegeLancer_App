package com.college.freelancestartup;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class RequestLogCollabFourActivity extends AppCompatActivity {

    private RecyclerView requestLogRecyclerView;
    private TextView emptyTV, progressTV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String projectID;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> userEmails;
    private ProgressBar progressBar;

    private RecyclerView.LayoutManager requestLogLayoutManager;
    private RecyclerView.Adapter requestLogCollabAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_collab_requestlog);

        requestLogRecyclerView = findViewById(R.id.log_collabsRequestsLog_recyclerView5);
        requestLogRecyclerView.setVisibility(View.GONE);
        emptyTV = findViewById(R.id.log_collab5RequestsLog_emptyTV);
        emptyTV.setVisibility(View.GONE);
        progressBar = findViewById(R.id.requestLog_progressBar);
        progressTV = findViewById(R.id.requestLog_progressTV);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutCollab5RequestsLog);
        projectID = getIntent().getExtras().get("projectID").toString();

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        showRequestsLog();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showRequestsLog();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showRequestsLog() {
        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail()).collection("MyCollabs")
                .document(projectID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userEmails = (ArrayList<String>) documentSnapshot.get("requestsMade");

            }
        }

        new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    emptyTV.setVisibility(View.GONE);
                    requestLogRecyclerView.setVisibility(View.VISIBLE);
                    ArrayList<AllColabsOne> prevCollabs = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String posterTitle = documentSnapshot.get("posterTitle").toString();
                        String projectTitle = documentSnapshot.get("projectTitle").toString();
                        String posterDate = documentSnapshot.get("postDate").toString();
                        String projectSkills = documentSnapshot.get("projectSkills").toString();
                        String projectOpenFor = documentSnapshot.get("projectOpenFor").toString();
                        String projectDesc = documentSnapshot.get("projectDesc").toString();
                        String projectID = documentSnapshot.get("projectID").toString();
                        int flag = 1;
                        prevCollabs.add(new AllColabsOne(posterTitle, projectTitle, projectDesc, posterDate, projectSkills, projectOpenFor, projectID, flag));

                        requestLogLayoutManager = new LinearLayoutManager(RequestLogCollabFourActivity.this);
                        requestLogCollabAdapter = new AllColabsOneAdapter(prevCollabs);
                        requestLogRecyclerView.setHasFixedSize(true);
                        requestLogRecyclerView.setLayoutManager(requestLogLayoutManager);
                        requestLogRecyclerView.setAdapter(requestLogCollabAdapter);
                    }
                } else {
                    requestLogRecyclerView.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RequestLogCollabFourActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performBackgroundTask(ArrayList<String> emails) {
        // Finish this
        for (String s : emails) {
            db.collection("Users").document("User " + s).collection("CollabRequests")
                    .document(projectID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String posterTitle = documentSnapshot.get("posterTitle").toString();
                    String projectTitle = documentSnapshot.get("projectTitle").toString();
                    String posterDate = documentSnapshot.get("postDate").toString();
                    String projectSkills = documentSnapshot.get("projectSkills").toString();
                    String projectOpenFor = documentSnapshot.get("projectOpenFor").toString();
                    String projectDesc = documentSnapshot.get("projectDesc").toString();
                    String projectID = documentSnapshot.get("projectID").toString();
                    int flag = 2;
                }
            })
        }
    }

    private static class RequestLogAsync extends AsyncTask<ArrayList<String>, Void, String> {
        private WeakReference<RequestLogCollabFourActivity> activityWeakReference;

        RequestLogAsync(RequestLogCollabFourActivity activity) {
            activityWeakReference = new WeakReference<RequestLogCollabFourActivity>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RequestLogCollabFourActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.progressBar.setVisibility(View.VISIBLE);
            activity.progressTV.setVisibility(View.VISIBLE);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected String doInBackground(ArrayList<String>... params) {
            RequestLogCollabFourActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return "";
            }
            ArrayList<String> IDs = params[0];
            activity.performBackgroundTask(IDs);
            return "Finished";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            RequestLogCollabFourActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.progressBar.setVisibility(View.GONE);
            activity.progressTV.setVisibility(View.GONE);
            activity.requestLogRecyclerView.setVisibility(View.VISIBLE);
        }

    }
}