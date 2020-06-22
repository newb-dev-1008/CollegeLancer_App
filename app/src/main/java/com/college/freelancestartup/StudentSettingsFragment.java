package com.college.freelancestartup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentSettingsFragment extends Fragment {

    private String[] studentSettingsArray;
    private ListView studentSettingsListView;
    private View root;
    private FirebaseFirestore statusDB;
    private int checkedStatus = 1;
    private String studentStatus;
    private String UIDEmailID;

    private static final String KEY_STUDSTATUS = "studentStatus";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        studentSettingsArray = getResources().getStringArray(R.array.studentSettingsArray);
        root = inflater.inflate(R.layout.student_settings_frag, container, false);
        studentSettingsListView = root.findViewById(R.id.student_settings_listview);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        statusDB = FirebaseFirestore.getInstance();

        UIDEmailID = firebaseAuth.getCurrentUser().getEmail();

        ArrayAdapter<String> studentSettingsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, studentSettingsArray);
        studentSettingsListView.setAdapter(studentSettingsAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        studentSettingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                }
            }
        });
    }
}
