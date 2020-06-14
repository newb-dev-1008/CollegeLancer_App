package com.college.freelancestartup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class StudentHomeFragment extends Fragment {

    private StudentProjectsListAdapter studentProjectsListAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference studentProjectsRef = db.collection("Projects");
    private View root;

    public StudentHomeFragment(){
        // Empty Constructor Required
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.student_home_frag, container, false);

        Query query = studentProjectsRef.orderBy("priority", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<StudentProjectsListClass> studentProjects = new FirestoreRecyclerOptions.Builder<StudentProjectsListClass>()
                .setQuery(query, StudentProjectsListClass.class)
                .build();

        studentProjectsListAdapter = new StudentProjectsListAdapter(studentProjects);

        RecyclerView studentHomeRecyclerView = root.findViewById(R.id.student_home_recyclerview);
        studentHomeRecyclerView.setHasFixedSize(true);
        studentHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        studentHomeRecyclerView.setAdapter(studentProjectsListAdapter);

        return inflater.inflate(R.layout.student_home_frag, container, false);

    }


    @Override
    public void onStart() {
        super.onStart();
        studentProjectsListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        studentProjectsListAdapter.stopListening();
    }
}
