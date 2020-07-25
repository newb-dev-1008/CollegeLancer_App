package com.college.freelancestartup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AllColabsOneAdapter extends RecyclerView.Adapter<AllColabsOneAdapter.AllColabsOneViewHolder> {

    private ArrayList<AllColabsOne> AllColabsList;

    public static class AllColabsOneViewHolder extends RecyclerView.ViewHolder {

        public TextView posterName1, projectTitle1, postedDate1, projectDesc1, openFor1, skills1;

        public AllColabsOneViewHolder(@NonNull View itemView) {
            posterName1 = itemView.findViewById(R.id.collab1_name);
            projectTitle1 = itemView.findViewById(R.id.collab1_projectTitle);
            postedDate1 = itemView.findViewById(R.id.collab1_date);
            projectDesc1 = itemView.findViewById(R.id.collab1_projectDesc);
            openFor1 = itemView.findViewById(R.id.collab1_openFor);
            skills1 = itemView.findViewById(R.id.collab1_skills);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    public AllColabsOneAdapter(ArrayList<AllColabsOne> allColabsExampleList){
        AllColabsList = allColabsExampleList;
    }

    @NonNull
    @Override
    public AllColabsOneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_collab1_cardview, parent, false);
        AllColabsOneAdapter.AllColabsOneViewHolder allColabsOneViewHolder = new AllColabsOneAdapter.AllColabsOneViewHolder(v);
        return allColabsOneViewHolder;
    }
}
