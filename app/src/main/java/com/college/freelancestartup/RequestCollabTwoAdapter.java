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

public class RequestCollabTwoAdapter extends RecyclerView.Adapter<RequestCollabTwoAdapter.RequestCollabTwoViewHolder> {
    private ArrayList<RequestCollabTwo> RequestCollabsList;
    private String posterName, projectTitle, projectDesc, skills, openFor, postedDate, projectStatus, projectID;

    public RequestCollabTwoAdapter(ArrayList<RequestCollabTwo> requestCollabsExampleList){
        RequestCollabsList = requestCollabsExampleList;
    }

    @NonNull
    @Override
    public RequestCollabTwoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_collab1_cardview, parent, false);
        RequestCollabTwoAdapter.RequestCollabTwoViewHolder requestCollabTwoViewHolder = new RequestCollabTwoAdapter.RequestCollabTwoViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RequestCollabTwoOpenActivity.class);
                String transitionName = view.getResources().getString(R.string.transitionAnimation);
                View viewStart = view.findViewById(R.id.find_collab2_cardview);

                intent.putExtra(posterName, "posterName1");
                intent.putExtra(projectTitle, "projectTitle1");
                intent.putExtra(projectDesc, "projectDesc1");
                intent.putExtra(openFor, "openFor1");
                intent.putExtra(skills, "skills1");
                intent.putExtra(postedDate, "postedDate1");
                intent.putExtra(projectStatus, "projectStatus1");
                intent.putExtra(projectID, "projectID1");

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), viewStart, transitionName);

                ActivityCompat.startActivity(view.getContext(), intent, options.toBundle());
            }
        });
        return requestCollabTwoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestCollabTwoViewHolder holder, int position) {
        RequestCollabTwo requestCollabTwo = RequestCollabsList.get(position);

        posterName = requestCollabTwo.getPosterTitle();
        projectTitle = requestCollabTwo.getProjectTitle();
        projectDesc = requestCollabTwo.getProjectDesc();
        skills = requestCollabTwo.getSkills();
        openFor = requestCollabTwo.getOpenFor();
        postedDate = requestCollabTwo.getCollabDate();
        projectStatus = requestCollabTwo.getProjectStatus();
        projectID = requestCollabTwo.getProjectID();

        holder.posterName1.setText(posterName);
        holder.projectTitle1.setText(projectTitle);
        holder.projectDesc1.setText(projectDesc);
        holder.skills1.setText(skills);
        holder.openFor1.setText(openFor);
        holder.postedDate1.setText(postedDate);
        holder.projectStatus1.setText(projectStatus);

    }

    public static class RequestCollabTwoViewHolder extends RecyclerView.ViewHolder {
        public TextView posterName1, projectTitle1, postedDate1, projectDesc1, openFor1, skills1, projectStatus1;

        public RequestCollabTwoViewHolder(@NonNull View itemView) {
            super(itemView);
            posterName1 = itemView.findViewById(R.id.collab1_name);
            projectTitle1 = itemView.findViewById(R.id.collab1_projectTitle);
            postedDate1 = itemView.findViewById(R.id.collab1_date);
            projectDesc1 = itemView.findViewById(R.id.collab1_projectDesc);
            openFor1 = itemView.findViewById(R.id.collab1_openFor);
            skills1 = itemView.findViewById(R.id.collab1_skills);
            projectStatus1 = itemView.findViewById(R.id.collab2_projectStatus);
        }
    }

    @Override
    public int getItemCount() {
        return RequestCollabsList.size();
    }
}