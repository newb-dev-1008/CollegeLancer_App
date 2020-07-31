package com.college.freelancestartup;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

class PreviousCollabsCollabFiveActivity extends AppCompatActivity {

    private RecyclerView previousCollabRecyclerView;
    private TextView emptyTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentcollab5_previouscollabs);

        previousCollabRecyclerView = findViewById(R.id.find_collabs_recyclerView51);
        emptyTV = findViewById(R.id.find_collab51_emptyTV);


    }
}
