package com.college.freelancestartup;

import android.app.Activity;
import android.app.ActivityOptions;
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

public class AllColabsOneAdapter extends RecyclerView.Adapter<AllColabsOneAdapter.AllColabsOneViewHolder> {
    private ArrayList<AllColabsOne> AllColabsList;
    private String posterName, projectTitle, projectDesc, skills, openFor, postedDate;

    public AllColabsOneAdapter(ArrayList<AllColabsOne> allColabsExampleList){
        AllColabsList = allColabsExampleList;
    }

    @NonNull
    @Override
    public AllColabsOneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_collab1_cardview, parent, false);
        AllColabsOneAdapter.AllColabsOneViewHolder allColabsOneViewHolder = new AllColabsOneAdapter.AllColabsOneViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AllColabsOneOpenActivity.class);
                String transitionName = view.getResources().getString(R.string.transitionAnimation);
                View viewStart = view.findViewById(R.id.find_collab1_cardview);

                intent.putExtra(posterName, "posterName1");
                intent.putExtra(projectTitle, "projectTitle1");
                intent.putExtra(projectDesc, "projectDesc1");
                intent.putExtra(openFor, "openFor1");
                intent.putExtra(skills, "skills1");
                intent.putExtra(postedDate, "postedDate1");

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), viewStart, transitionName);

                ActivityCompat.startActivity(view.getContext(), intent, options.toBundle());
            }
        });
        return allColabsOneViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllColabsOneViewHolder holder, int position) {
        AllColabsOne allColabsOne = AllColabsList.get(position);

        posterName = allColabsOne.getPosterTitle();
        projectTitle = allColabsOne.getProjectTitle();
        projectDesc = allColabsOne.getProjectDesc();
        skills = allColabsOne.getSkills();
        openFor = allColabsOne.getOpenFor();
        postedDate = allColabsOne.getCollabDate();

        holder.posterName1.setText(posterName);
        holder.projectTitle1.setText(projectTitle);
        holder.projectDesc1.setText(projectDesc);
        holder.skills1.setText(skills);
        holder.openFor1.setText(openFor);
        holder.postedDate1.setText(postedDate);

    }

    public static class AllColabsOneViewHolder extends RecyclerView.ViewHolder {
        public TextView posterName1, projectTitle1, postedDate1, projectDesc1, openFor1, skills1;

        public AllColabsOneViewHolder(@NonNull View itemView) {
            super(itemView);
            posterName1 = itemView.findViewById(R.id.collab1_name);
            projectTitle1 = itemView.findViewById(R.id.collab1_projectTitle);
            postedDate1 = itemView.findViewById(R.id.collab1_date);
            projectDesc1 = itemView.findViewById(R.id.collab1_projectDesc);
            openFor1 = itemView.findViewById(R.id.collab1_openFor);
            skills1 = itemView.findViewById(R.id.collab1_skills);
        }
    }

    @Override
    public int getItemCount() {
        return AllColabsList.size();
    }
}
