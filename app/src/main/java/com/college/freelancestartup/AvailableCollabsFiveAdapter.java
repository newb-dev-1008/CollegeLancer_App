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

class AvailableCollabsFiveAdapter extends RecyclerView.Adapter<AvailableCollabsFiveAdapter.AvailableCollabsFiveViewHolder> {
    private ArrayList<AvailableCollabsFive> AvailableCollabsList;
    private String name, semester, department, skills, noCollabs, noProjects, userEmail;
    private int flag;

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

                intent.putExtra("name", name);
                intent.putExtra("department", department);
                intent.putExtra("skills", skills);
                intent.putExtra("semester", semester);
                intent.putExtra("numberCollabs", noCollabs);
                intent.putExtra("numberProjects", noProjects);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("flagLog", flag);

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
        department = availableCollabsFive.getDepartment();
        skills = availableCollabsFive.getSkills();
        noProjects = availableCollabsFive.getNoProjects();
        noCollabs = availableCollabsFive.getNoCollabs();
        userEmail = availableCollabsFive.getUserEmail();
        flag = availableCollabsFive.getFlag();

        holder.name1.setText(name);
        holder.semester1.setText(semester);
        holder.department1.setText(department);
        holder.skills1.setText(skills);
        holder.noProjects1.setText(noProjects);
        holder.noCollabs1.setText(noCollabs);

    }

    public static class AvailableCollabsFiveViewHolder extends RecyclerView.ViewHolder {
        public TextView name1, semester1, department1, skills1, noProjects1, noCollabs1;

        public AvailableCollabsFiveViewHolder(@NonNull View itemView) {
            super(itemView);
            name1 = itemView.findViewById(R.id.collab5_name);
            semester1 = itemView.findViewById(R.id.collab5_personSemester);
            department1 = itemView.findViewById(R.id.collab5_personDepartment);
            skills1 = itemView.findViewById(R.id.collab5_skills);
            noProjects1 = itemView.findViewById(R.id.collab5_projectsCompleted);
            noCollabs1 = itemView.findViewById(R.id.collab5_collaborations);
        }
    }

    @Override
    public int getItemCount() {
        return AvailableCollabsList.size();
    }
}
