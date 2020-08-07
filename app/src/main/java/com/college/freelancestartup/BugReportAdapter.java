package com.college.freelancestartup;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import java.util.ArrayList;

public class BugReportAdapter extends RecyclerView.Adapter<BugReportAdapter.BugReportViewHolder> {
    private ArrayList<BugReport> BugReportList;

    public static class BugReportViewHolder extends RecyclerView.ViewHolder {

        public TextView filePath;
        public ImageView imgBitmap, deleteImg;
        public BugReportViewHolder(@NonNull View itemView) {
            super(itemView);
            filePath = itemView.findViewById(R.id.bug_ss_name);
            imgBitmap = itemView.findViewById(R.id.bugImage);
            deleteImg = itemView.findViewById(R.id.bug_remove_ss);

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
    public BugReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reg_complaints_cardview, parent, false);
        BugReportViewHolder bugReportViewHolder = new BugReportViewHolder(v);
        return bugReportViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BugReportViewHolder holder, int position) {
        BugReport bugRep = BugReportList.get(position);

        holder.filePath.setText(bugRep.getFilePath());
        holder.imgBitmap.setImageBitmap(bugRep.getBitmap());
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog deleteImg
            }
        });
    }

    @Override
    public int getItemCount() {
        return BugReportList.size();
    }

}
