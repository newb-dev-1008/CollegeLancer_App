package com.college.freelancestartup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class StudentProjectsListAdapter extends FirestoreRecyclerAdapter<StudentProjectsListClass, StudentProjectsListAdapter.StudentProjectsHolder>{

    public StudentProjectsListAdapter(@NonNull FirestoreRecyclerOptions<StudentProjectsListClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull StudentProjectsHolder studentProjectsHolder, int position, @NonNull StudentProjectsListClass studentProjectsListClass) {
        studentProjectsHolder.studentProjectTitle.setText(studentProjectsListClass.getProjectTitle());
        studentProjectsHolder.studentProjectDescription.setText(studentProjectsListClass.getProjectDescription());
        studentProjectsHolder.studentProjectPriority.setText(studentProjectsListClass.getPriority());
    }

    @NonNull
    @Override
    public StudentProjectsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_home_frag, parent, false);
        return new StudentProjectsHolder(v);
    }

    class StudentProjectsHolder extends RecyclerView.ViewHolder{

        TextView studentProjectTitle;
        TextView studentProjectDescription;
        TextView studentProjectPriority;

        public StudentProjectsHolder(@NonNull View itemView) {
            super(itemView);

            studentProjectTitle = itemView.findViewById(R.id.student_home_cardtitle1);
            studentProjectDescription = itemView.findViewById(R.id.student_home_cardtitle1_description);
            studentProjectPriority = itemView.findViewById(R.id.student_home_cardtitle1_priority);
        }
    }
}
