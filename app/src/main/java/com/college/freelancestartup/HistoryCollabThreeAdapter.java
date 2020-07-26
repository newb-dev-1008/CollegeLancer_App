package com.college.freelancestartup;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class HistoryCollabThreeAdapter extends RecyclerView.Adapter<HistoryCollabThreeAdapter.HistoryCollabThreeViewHolder> {
    private ArrayList<HistoryCollabsThree> HistoryCollabsList;
    private String posterName, projectTitle, projectDesc, skills, openFor, collabDate, collabStatus;

    public HistoryCollabThreeAdapter(ArrayList<HistoryCollabsThree> historyCollabsExampleList){
        HistoryCollabsList = historyCollabsExampleList;
    }

    @NonNull
    @Override
    public HistoryCollabThreeAdapter.HistoryCollabThreeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_collab3_cardview, parent, false);
        HistoryCollabThreeAdapter.HistoryCollabThreeViewHolder historyCollabThreeViewHolder = new HistoryCollabThreeAdapter.HistoryCollabThreeViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RequestCollabTwoOpenActivity.class);
                String transitionName = view.getResources().getString(R.string.transitionAnimation);
                View viewStart = view.findViewById(R.id.find_collab1_cardview);

                intent.putExtra(posterName, "posterName1");
                intent.putExtra(projectTitle, "projectTitle1");
                intent.putExtra(projectDesc, "projectDesc1");
                intent.putExtra(openFor, "openFor1");
                intent.putExtra(skills, "skills1");
                intent.putExtra(postedDate, "postedDate1");

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), viewStart, transitionName);

                ActivityCompat.startActivity(view.getContext(), intent, options.toBundle());
            }
        });
        return requestCollabTwoViewHolder;
    }
}
