package com.college.freelancestartup;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import java.util.ArrayList;

public class BugReportAdapter extends RecyclerView.Adapter<BugReportAdapter.BugReportViewHolder> {
    private ArrayList<BugReport> BugReportList;

    public static class BugReportViewHolder extends RecyclerView.ViewHolder {

        public TextView filePath;
        public ImageView imgBitmap;
        public BugReportViewHolder(@NonNull View itemView) {
            super(itemView);
            filePath = itemView.findViewById(R.id.bug_ss_name);
            imgBitmap = itemView.findViewById(R.id.bugImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imgBitmap.getVisibility() == View.GONE){
                        TransitionManager.beginDelayedTransition((ViewGroup) itemView, new AutoTransition());
                        imgBitmap.setVisibility(View.VISIBLE);
                    } else {
                        TransitionManager.beginDelayedTransition((ViewGroup) itemView, new AutoTransition());
                        imgBitmap.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public BugReportAdapter(ArrayList<BugReport> bugExampleList) {
        BugReportList = bugExampleList;
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
