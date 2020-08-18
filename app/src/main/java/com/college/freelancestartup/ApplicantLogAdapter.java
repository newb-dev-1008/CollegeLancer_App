package com.college.freelancestartup;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ApplicantLogAdapter extends RecyclerView.Adapter<ApplicantLogAdapter.ApplicantLogViewHolder> {

    private ArrayList<AvailableCollabsFive> ApplicantLogList;
    private String name, department, userEmail, projectID, pickedStatusT;
    private Context context;

    public ApplicantLogAdapter(ArrayList<AvailableCollabsFive> applicantLogExampleList){
        ApplicantLogList = applicantLogExampleList;
    }
}
