package com.college.freelancestartup;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SelectedApplicantsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_applicants_collab4);

        recyclerView = findViewById(R.id.find_recyclerViewSelectedApplicant);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutSelectedApplicants);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showSelectedApplicants();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showSelectedApplicants() {

    }
}
