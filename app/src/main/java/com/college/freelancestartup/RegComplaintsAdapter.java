package com.college.freelancestartup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RegComplaintsAdapter extends RecyclerView.Adapter<RegComplaintsAdapter.RegComplaintsViewHolder> {

    public static class RegComplaintsViewHolder extends RecyclerView.ViewHolder {

        public TextView regComplaintsTV, regComplaintsDate, regComplaintsStatus;
        public RegComplaintsViewHolder(@NonNull View itemView) {
            super(itemView);
            regComplaintsTV = itemView.findViewById(R.id.reg_complaintName);
            regComplaintsDate = itemView.findViewById(R.id.reg_complaintdate);
            regComplaintsStatus = itemView.findViewById(R.id.reg_complaintstatus);
        }
    }

    @NonNull
    @Override
    public RegComplaintsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reg_complaints_cardview, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull RegComplaintsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
