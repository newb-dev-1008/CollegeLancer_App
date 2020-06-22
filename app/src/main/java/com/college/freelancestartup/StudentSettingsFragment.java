package com.college.freelancestartup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
                    case 0:
                        String[] status = {"Available for research (with colleagues)", "Available for research (with colleagues)", "Looking for students", "Busy, but can provide projects", "Unavailable for a while"};
                        AlertDialog statusSetting = new MaterialAlertDialogBuilder(getContext())
                                .setTitle("Set your current status")
                                .setMessage("Please note that your status determines your availability for providing and receiving projects.")
                                .setSingleChoiceItems(status, checkedStatus, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int statusNo) {
                                        checkedStatus = statusNo;
                                        studentStatus = status[checkedStatus];
                                        switch (checkedStatus){
                                            case 0:
                                                Toast.makeText(getContext(), "Setting status. Your colleagues will now see you're available for research collaboration.", Toast.LENGTH_LONG).show();
                                                break;
                                            case 1:
                                                Toast.makeText(getContext(), "Setting status. Students will now see you're available for research.", Toast.LENGTH_LONG).show();
                                                break;
                                            case 2:
                                                Toast.makeText(getContext(), "Setting status. Students will now be able to contact you for projects.", Toast.LENGTH_LONG).show();
                                                break;
                                            case 3:
                                                Toast.makeText(getContext(), "Setting status. Students can still contact you for research or projects if necessary.", Toast.LENGTH_LONG).show();
                                                break;
                                            case 4:
                                                Toast.makeText(getContext(), "Setting status. You will not be contacted for projects or research until you change your status.", Toast.LENGTH_LONG).show();
                                                break;
                                        }
                                    }
                                }).setPositiveButton("Set Status", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Map<String, Object> profStatusMap = new HashMap<>();
                                        profStatusMap.put(KEY_STUDSTATUS, studentStatus);

                                        statusDB.collection("Users").document("User " + UIDEmailID).set(profStatusMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getContext(), "Your status has been updated.", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                                }).setNegativeButton("Go back", null)
                                .create();
                        statusSetting.setCanceledOnTouchOutside(false);
                        statusSetting.show();
                }
            }
        });
    }
}
