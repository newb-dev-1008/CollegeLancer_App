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

public class AvailableCollabsFiveAdapter extends RecyclerView.Adapter<AvailableCollabsFiveAdapter.AvailableCollabsFiveViewHolder> {
    private ArrayList<AvailableCollabsFive> AvailableCollabsList;
    private String name, semester, department, skills, noCollabs, noProjects, userEmail, projectID, pickedStatusT;
    private int flag;
    private Context context;

    public AvailableCollabsFiveAdapter(ArrayList<AvailableCollabsFive> availableCollabsExampleList){
        AvailableCollabsList = availableCollabsExampleList;
    }

    @NonNull
    @Override
    public AvailableCollabsFiveAdapter.AvailableCollabsFiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_collab5_cardview, parent, false);
        context = v.getContext();
        AvailableCollabsFiveAdapter.AvailableCollabsFiveViewHolder availableCollabsFiveViewHolder = new AvailableCollabsFiveAdapter.AvailableCollabsFiveViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AvailableCollabsFiveOpenActivity.class);
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

                intent.putExtra("flagLog", flag);
                intent.putExtra("userEmail", userEmail);

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), viewStart, transitionName);

                ActivityCompat.startActivity(view.getContext(), intent, options.toBundle());
            }
        });
        return availableCollabsFiveViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableCollabsFiveAdapter.AvailableCollabsFiveViewHolder holder, int position) {
        AvailableCollabsFive availableCollabsFive = AvailableCollabsList.get(position);

        name = availableCollabsFive.getName();
        semester = availableCollabsFive.getSemester();
        // department = availableCollabsFive.getDepartment();
        // skills = availableCollabsFive.getSkills();
        // noProjects = availableCollabsFive.getNoProjects();
        // noCollabs = availableCollabsFive.getNoCollabs();
        userEmail = availableCollabsFive.getUserEmail();
        flag = availableCollabsFive.getFlag();
        // projectID = availableCollabsFive.getProjectID();

        holder.name1.setText(name);
        holder.semester1.setText(semester);

        /*
        holder.department12.setText(department);
        holder.skills1.setText(skills);
        holder.noProjects1.setText(noProjects);
        holder.noCollabs1.setText(noCollabs);

        if (flag == 1) {
            holder.pickedStatus.setVisibility(View.VISIBLE);
            // holder.applicationArticleTV.setVisibility(View.VISIBLE);
            // holder.selectButton.setText("Select");

            FirebaseFirestore.getInstance().collection("CollabProjects").document(projectID)
                    .collection("Collaborators").document("User " + userEmail).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            pickedStatusT = documentSnapshot.get("picked").toString();
                            // applicationArticle = documentSnapshot.get("appArticle").toString();
                            if (pickedStatusT.equals("Accepted")) {
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
        */
    }

    public static class AvailableCollabsFiveViewHolder extends RecyclerView.ViewHolder {
        public TextView name1, semester1, pickedStatus;
        // public TextView department12, skills1, noProjects1, noCollabs1, applicationArticleTV;
        // private MaterialButton selectButton;

        public AvailableCollabsFiveViewHolder(@NonNull View itemView) {
            super(itemView);
            name1 = itemView.findViewById(R.id.collab5_name);
            semester1 = itemView.findViewById(R.id.collab5_personSemester);
            // department12 = itemView.findViewById(R.id.collab5_personDepartments);
            // skills1 = itemView.findViewById(R.id.collab5_skills);
            // noProjects1 = itemView.findViewById(R.id.collab5_projectsCompleted);
            // noCollabs1 = itemView.findViewById(R.id.collab5_collaborations);
            pickedStatus = itemView.findViewById(R.id.collab5_personPickedStatusClosed);
            // applicationArticleTV = itemView.findViewById(R.id.collab5_applicationArticle);
            // selectButton = itemView.findViewById(R.id.collab5_selectBtn);

        }
    }

    @Override
    public int getItemCount() {
        return AvailableCollabsList.size();
    }
}
