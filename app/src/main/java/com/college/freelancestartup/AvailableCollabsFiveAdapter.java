package com.college.freelancestartup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AvailableCollabsFiveAdapter extends RecyclerView.Adapter<AvailableCollabsFiveAdapter.AvailableCollabsFiveViewHolder> {
    private ArrayList<AvailableCollabsFive> AvailableCollabsList;
    private String name, semester, department, skills, noCollabs, noProjects;

    public AvailableCollabsFiveAdapter(ArrayList<AvailableCollabsFive> availableCollabsExampleList){
        AvailableCollabsList = availableCollabsExampleList;
    }
}
