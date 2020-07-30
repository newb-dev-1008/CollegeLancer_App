package com.college.freelancestartup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class HistoryCollabThreeAdapter extends RecyclerView.Adapter<HistoryCollabThreeAdapter.HistoryCollabsThreeViewHolder> {
    private ArrayList<HistoryCollabsThree> HistoryCollabsList;
    private String posterName, projectTitle, projectDesc, collabDate, collabStatus, projectID;

    public HistoryCollabThreeAdapter(ArrayList<HistoryCollabsThree> historyCollabsExampleList){
        HistoryCollabsList = historyCollabsExampleList;
    }

    @NonNull
    @Override
    public HistoryCollabThreeAdapter.HistoryCollabsThreeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_collab3_cardview, parent, false);
        HistoryCollabThreeAdapter.HistoryCollabsThreeViewHolder historyCollabThreeViewHolder = new HistoryCollabThreeAdapter.HistoryCollabsThreeViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HistoryCollabThreeOpenActivity.class);
                String transitionName = view.getResources().getString(R.string.transitionAnimation);
                View viewStart = view.findViewById(R.id.find_collab3_cardview);

                intent.putExtra(posterName, "posterName1");
                intent.putExtra(projectTitle, "projectTitle1");
                intent.putExtra(projectDesc, "projectDesc1");
                intent.putExtra(collabDate, "collabDate1");
                intent.putExtra(collabStatus, "collabStatus1");
                intent.putExtra(projectID, "projectID1");

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), viewStart, transitionName);

                ActivityCompat.startActivity(view.getContext(), intent, options.toBundle());
            }
        });
        return historyCollabThreeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryCollabThreeAdapter.HistoryCollabsThreeViewHolder holder, int position) {
        HistoryCollabsThree historyCollabsThree = HistoryCollabsList.get(position);

        posterName = historyCollabsThree.getPosterTitle();
        projectTitle = historyCollabsThree.getProjectTitle();
        projectDesc = historyCollabsThree.getProjectDesc();
        collabDate = historyCollabsThree.getCollabDate();
        collabStatus = historyCollabsThree.getCollabStatus();
        projectID = historyCollabsThree.getProjectID();

        holder.posterName1.setText(posterName);
        holder.projectTitle1.setText(projectTitle);
        holder.projectDesc1.setText(projectDesc);
        holder.postedDate1.setText(collabDate);
        holder.collabStatus1.setText(collabStatus);

        if (collabStatus.equals("Completed")) {
            holder.collabStatus1.setTextColor(Color.parseColor("#228B22"));
        } else {
            holder.collabStatus1.setTextColor(Color.parseColor("#800000"));
        }
    }

    public static class HistoryCollabsThreeViewHolder extends RecyclerView.ViewHolder {
        public TextView posterName1, projectTitle1, postedDate1, projectDesc1, openFor1, skills1, collabStatus1;

        public HistoryCollabsThreeViewHolder(@NonNull View itemView) {
            super(itemView);
            posterName1 = itemView.findViewById(R.id.collab3_name);
            projectTitle1 = itemView.findViewById(R.id.collab3_projectTitle);
            postedDate1 = itemView.findViewById(R.id.collab3_name);
            projectDesc1 = itemView.findViewById(R.id.collab3_projectDesc);
            collabStatus1 = itemView.findViewById(R.id.collab3_status);
        }
    }

    @Override
    public int getItemCount() {
        return HistoryCollabsList.size();
    }
}
