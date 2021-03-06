package com.college.freelancestartup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ApplicantLogAdapter extends RecyclerView.Adapter<ApplicantLogAdapter.ApplicantLogViewHolder> {
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
                intent.putExtra("projectID", projectID);

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
        projectID = applicantLog.getProjectID();

        holder.name1.setText(name);
        holder.semester1.setText(semester);
        holder.pickedStatus.setVisibility(View.VISIBLE);

        FirebaseFirestore.getInstance().collection("CollabProjects").document(projectID)
                .collection("Collaborators").document("User " + userEmail).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        pickedStatusT = documentSnapshot.get("picked").toString();
                        // applicationArticle = documentSnapshot.get("appArticle").toString();
                        if (pickedStatusT.equals("Accepted") || pickedStatusT.equals("Selected")) {
                            holder.pickedStatus.setText(pickedStatusT);
                            holder.pickedStatus.setTextColor(Color.parseColor("#228B22"));
                        } else {
                            holder.pickedStatus.setText(pickedStatusT);
                            holder.pickedStatus.setTextColor(Color.parseColor("#800000"));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class ApplicantLogViewHolder extends RecyclerView.ViewHolder {
        public TextView name1, semester1, pickedStatus;
        // private MaterialButton selectButton;

        public ApplicantLogViewHolder(@NonNull View itemView) {
            super(itemView);
            name1 = itemView.findViewById(R.id.collab5_name);
            semester1 = itemView.findViewById(R.id.collab5_personSemester);
            pickedStatus = itemView.findViewById(R.id.collab5_personPickedStatusClosed);
        }
    }

    @Override
    public int getItemCount() {
        return ApplicantLogList.size();
    }
}
