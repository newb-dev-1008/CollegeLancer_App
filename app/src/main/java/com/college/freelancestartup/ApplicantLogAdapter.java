package com.college.freelancestartup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ApplicantLogAdapter extends RecyclerView.Adapter<ApplicantLogAdapter.ApplicantLogViewHolder> {

    private ArrayList<AvailableCollabsFive> ApplicantLogList;
    private String name, semester, userEmail, projectID, pickedStatusT;
    private Context context;

    public ApplicantLogAdapter(ArrayList<AvailableCollabsFive> applicantLogExampleList){
        ApplicantLogList = applicantLogExampleList;
    }

    @NonNull
    @Override
    public ApplicantLogAdapter.ApplicantLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_collab5_cardview, parent, false);
        context = v.getContext();
        ApplicantLogAdapter.ApplicantLogViewHolder applicantLogViewHolder = new ApplicantLogAdapter.ApplicantLogViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ApplicantLogOpenActivity.class);
                String transitionName = view.getResources().getString(R.string.transitionAnimation);
                View viewStart = view.findViewById(R.id.find_collab5_cardview);

                /*
                intent.putExtra("name", name);
                intent.putExtra("department", department);
                intent.putExtra("skills", skills);
                intent.putExtra("semester", semester);
                intent.putExtra("numberCollabs", noCollabs);
                intent.putExtra("numberProjects", noProjects);
                */

                // intent.putExtra("flagLog", flag);
                intent.putExtra("userEmail", userEmail);

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), viewStart, transitionName);

                ActivityCompat.startActivity(view.getContext(), intent, options.toBundle());
            }
        });
        return applicantLogViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicantLogAdapter.ApplicantLogViewHolder holder, int position) {
        AvailableCollabsFive applicantLog = ApplicantLogList.get(position);

        name = applicantLog.getName();
        semester = applicantLog.getSemester();
        // department = availableCollabsFive.getDepartment();
        // skills = availableCollabsFive.getSkills();
        // noProjects = availableCollabsFive.getNoProjects();
        // noCollabs = availableCollabsFive.getNoCollabs();
        userEmail = applicantLog.getUserEmail();
        // flag = applicantLog.getFlag();
        // projectID = availableCollabsFive.getProjectID();

        holder.name1.setText(name);
        holder.semester1.setText(semester);
        holder.pickedStatus.setVis
}
