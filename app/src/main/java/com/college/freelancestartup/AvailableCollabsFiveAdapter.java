package com.college.freelancestartup;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AvailableCollabsFiveAdapter extends RecyclerView.Adapter<AvailableCollabsFiveAdapter.AvailableCollabsFiveViewHolder> {
    private ArrayList<AvailableCollabsFive> AvailableCollabsList;
    private String name, semester, department, skills, noCollabs, noProjects;

    public AvailableCollabsFiveAdapter(ArrayList<AvailableCollabsFive> availableCollabsExampleList){
        AvailableCollabsList = availableCollabsExampleList;
    }

    @NonNull
    @Override
    public AvailableCollabsFiveAdapter.AvailableCollabsFiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_collab5_cardview, parent, false);
        AvailableCollabsFiveAdapter.AvailableCollabsFiveViewHolder availableCollabsFiveViewHolder = new AvailableCollabsFiveAdapter.AvailableCollabsFiveViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AvailableCollabsFiveOpenActivity.class);
                String transitionName = view.getResources().getString(R.string.transitionAnimation);
                View viewStart = view.findViewById(R.id.find_collab5_cardview);

                intent.putExtra(name, "name");
                intent.putExtra(department, "department");
                intent.putExtra(skills, "skills");
                intent.putExtra(semester, "semester");
                intent.putExtra(noCollabs, "numberCollabs");
                intent.putExtra(noProjects, "numberProjects");

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), viewStart, transitionName);

                ActivityCompat.startActivity(view.getContext(), intent, options.toBundle());
            }
        });
        return allColabsOneViewHolder;
    }

}
