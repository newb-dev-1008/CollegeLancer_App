package com.college.freelancestartup;

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
    }

    @NonNull
    @Override
    public StudentProjectsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
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
