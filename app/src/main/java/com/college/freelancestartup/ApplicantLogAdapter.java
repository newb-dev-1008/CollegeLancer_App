package com.college.freelancestartup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
}
