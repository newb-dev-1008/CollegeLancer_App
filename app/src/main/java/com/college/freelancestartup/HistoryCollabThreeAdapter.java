package com.college.freelancestartup;

import android.app.Activity;
import android.content.Intent;
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
    private String posterName, projectTitle, projectDesc, skills, openFor, collabDate, collabStatus;

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
                intent.putExtra(openFor, "openFor1");
                intent.putExtra(skills, "skills1");
                intent.putExtra(collabDate, "collabDate1");
                intent.putExtra(collabStatus, "collabStatus1");

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), viewStart, transitionName);

                ActivityCompat.startActivity(view.getContext(), intent, options.toBundle());
            }
        });
        return HistoryCollabsThreeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryCollabThreeAdapter.HistoryCollabsThreeViewHolder holder, int position) {
        HistoryCollabsThree historyCollabsThree = HistoryCollabsList.get(position);

        posterName = historyCollabsThree.getPosterTitle();
        projectTitle = historyCollabsThree.getProjectTitle();
        projectDesc = historyCollabsThree.getProjectDesc();
        skills = historyCollabsThree.getSkills();
        openFor = historyCollabsThree.getOpenFor();
        collabDate = historyCollabsThree.getCollabDate();
        collabStatus = historyCollabsThree.getCollabStatus();

        holder.posterName1.setText(posterName);
        holder.projectTitle1.setText(projectTitle);
        holder.projectDesc1.setText(projectDesc);
        holder.skills1.setText(skills);
        holder.openFor1.setText(openFor);
        holder.postedDate1.setText(collabDate);
        holder.collabStatus1.setText(collabStatus);

    }

    public static class HistoryCollabsThreeViewHolder extends RecyclerView.ViewHolder {
        public TextView posterName1, projectTitle1, postedDate1, projectDesc1, openFor1, skills1, collabStatus1;

        public HistoryCollabsThreeViewHolder(@NonNull View itemView) {
            super(itemView);
            posterName1 = itemView.findViewById(R.id.collab1_name);
            projectTitle1 = itemView.findViewById(R.id.collab1_projectTitle);
            postedDate1 = itemView.findViewById(R.id.collab1_date);
            projectDesc1 = itemView.findViewById(R.id.collab1_projectDesc);
            openFor1 = itemView.findViewById(R.id.collab1_openFor);
            skills1 = itemView.findViewById(R.id.collab1_skills);
            collabStatus1 = itemView.findViewById(R.id.collab3_status);
        }
    }

    @Override
    public int getItemCount() {
        return HistoryCollabsList.size();
    }
}
