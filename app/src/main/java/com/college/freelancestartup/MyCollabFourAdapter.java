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

class MyCollabFourAdapter extends RecyclerView.Adapter<MyCollabFourAdapter.MyCollabsFourViewHolder> {
    private ArrayList<MyCollabsFour> MyCollabsList;
    private String posterName, projectTitle, projectDesc, postedDate, projectStatus, numApplicants, numSelectedApplicants, projectSkills, projectOpenFor, projectID;

    public MyCollabFourAdapter(ArrayList<MyCollabsFour> myCollabsExampleList){
        MyCollabsList = myCollabsExampleList;
    }

    @NonNull
    @Override
    public MyCollabFourAdapter.MyCollabsFourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_collab4_cardview, parent, false);
        MyCollabFourAdapter.MyCollabsFourViewHolder myCollabsFourViewHolder = new MyCollabFourAdapter.MyCollabsFourViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MyCollabsFourOpenActivity.class);
                String transitionName = view.getResources().getString(R.string.transitionAnimation);
                View viewStart = view.findViewById(R.id.find_collab3_cardview);

                intent.putExtra("projectID", projectID);

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), viewStart, transitionName);

                ActivityCompat.startActivity(view.getContext(), intent, options.toBundle());
            }
        });
        return myCollabsFourViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCollabFourAdapter.MyCollabsFourViewHolder holder, int position) {
        MyCollabsFour myCollabsFour = MyCollabsList.get(position);

        posterName = myCollabsFour.getPosterTitle();
        projectTitle = myCollabsFour.getProjectTitle();
        projectDesc = myCollabsFour.getProjectDesc();
        postedDate = myCollabsFour.getPostedDate();
        numApplicants = myCollabsFour.getNumberApplicants();
        numSelectedApplicants = myCollabsFour.getNumberSelectedApplicants();
        projectStatus = myCollabsFour.getProjectStatus();
        projectSkills = myCollabsFour.getProjectSkills();
        projectOpenFor = myCollabsFour.getProjectOpenFor();
        projectID = myCollabsFour.getProjectID();

        holder.posterName.setText(posterName);
        holder.projectTitle.setText(projectTitle);
        holder.projectDesc.setText(projectDesc);
        holder.postedDate.setText(postedDate);
        holder.projectStatus.setText(projectStatus);
        holder.numApplicants.setText(numApplicants);
        holder.numSelectedApplicants.setText(numSelectedApplicants);
        holder.projectSkills.setText(projectSkills);
        holder.projectOpenFor.setText(projectOpenFor);
    }

    public static class MyCollabsFourViewHolder extends RecyclerView.ViewHolder {
        public TextView posterName, projectTitle, postedDate, projectDesc, projectStatus, numApplicants, numSelectedApplicants, projectSkills, projectOpenFor, projectID;

        public MyCollabsFourViewHolder(@NonNull View itemView) {
            super(itemView);
            posterName = itemView.findViewById(R.id.collab4_name);
            projectTitle = itemView.findViewById(R.id.collab4_projectTitle);
            postedDate = itemView.findViewById(R.id.collab4_date);
            projectDesc = itemView.findViewById(R.id.collab4_projectDesc);
            projectStatus = itemView.findViewById(R.id.collab4_status);
            numApplicants = itemView.findViewById(R.id.collab4_noApplicants);
            numSelectedApplicants = itemView.findViewById(R.id.collab4_noAcceptedApplicants);
            projectSkills = itemView.findViewById(R.id.collab4_skills);
            projectOpenFor = itemView.findViewById(R.id.collab4_openFor);
        }
    }

    @Override
    public int getItemCount() {
        return MyCollabsList.size();
    }
}
