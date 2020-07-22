package com.college.freelancestartup;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import java.util.ArrayList;

public class RegComplaintsAdapter extends RecyclerView.Adapter<RegComplaintsAdapter.RegComplaintsViewHolder> {
    private ArrayList<RegisteredComplaints> RegisteredComplaintsList;

    public static class RegComplaintsViewHolder extends RecyclerView.ViewHolder {

        public TextView regComplaintsTitle, regComplaintsDate, regComplaintsStatus, regComplaintsComplaint, complaintIDTV, complaintDescTV, complaintID;
        public RegComplaintsViewHolder(@NonNull View itemView) {
            super(itemView);
            regComplaintsTitle = itemView.findViewById(R.id.reg_complaintName);
            regComplaintsDate = itemView.findViewById(R.id.reg_complaintdate);
            regComplaintsStatus = itemView.findViewById(R.id.reg_complaintstatus);
            regComplaintsComplaint = itemView.findViewById(R.id.complaint);
            complaintDescTV = itemView.findViewById(R.id.complaintDescTV);
            complaintIDTV = itemView.findViewById(R.id.complaintIDTV);
            complaintID = itemView.findViewById(R.id.complaintID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (regComplaintsComplaint.getVisibility() == View.GONE){
                        TransitionManager.beginDelayedTransition(, new AutoTransition());
                        complaintIDTV.setVisibility(View.VISIBLE);
                        complaintID.setVisibility(View.VISIBLE);
                        complaintDescTV.setVisibility(View.VISIBLE);
                        regComplaintsComplaint.setVisibility(View.VISIBLE);
                    } else {
                        TransitionManager.beginDelayedTransition(, new AutoTransition());
                        complaintIDTV.setVisibility(View.GONE);
                        complaintID.setVisibility(View.GONE);
                        complaintDescTV.setVisibility(View.GONE);
                        regComplaintsComplaint.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public RegComplaintsAdapter(ArrayList<RegisteredComplaints> regExampleList) {
        RegisteredComplaintsList = regExampleList;
    }

    @NonNull
    @Override
    public RegComplaintsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reg_complaints_cardview, parent, false);
        RegComplaintsViewHolder regComplaintsViewHolder = new RegComplaintsViewHolder(v);
        return regComplaintsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RegComplaintsViewHolder holder, int position) {
        RegisteredComplaints currentComplaint = RegisteredComplaintsList.get(position);

        holder.regComplaintsTitle.setText(currentComplaint.getComplaintTitle());
        holder.regComplaintsDate.setText(currentComplaint.getComplaintDate());
        holder.regComplaintsComplaint.setText(currentComplaint.getComplaint());
        holder.complaintID.setText(currentComplaint.getComplaintID());
        if (currentComplaint.getComplaintStatus().equals("Pending")){
            holder.regComplaintsStatus.setText(currentComplaint.getComplaintStatus());
            holder.regComplaintsStatus.setTextColor(Color.parseColor("#800000"));
        } else {
            holder.regComplaintsStatus.setText(currentComplaint.getComplaintStatus());
            holder.regComplaintsStatus.setTextColor(Color.parseColor("#228B22"));
        }
    }

    @Override
    public int getItemCount() {
        return RegisteredComplaintsList.size();
    }
}
