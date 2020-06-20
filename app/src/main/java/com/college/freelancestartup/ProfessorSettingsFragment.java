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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfessorSettingsFragment extends Fragment {

    String[] profSettingsArray;
    ListView profSettingsListView;
    View root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profSettingsArray = getResources().getStringArray(R.array.profSettingsArray);
        root = inflater.inflate(R.layout.prof_settings_frag, container, false);
        profSettingsListView = root.findViewById(R.id.prof_settings_listview);

        ArrayAdapter<String> studentSettingsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, profSettingsArray);
        profSettingsListView.setAdapter(studentSettingsAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        profSettingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        String[] status = {"Available for research", "Looking for students", "Busy", "Unavailable for a while"};
                        androidx.appcompat.app.AlertDialog.Builder statusSetting = new MaterialAlertDialogBuilder(getContext())
                                .setTitle("Set your current status")
                                .setMessage("Please note that your status determines your availability for providing and receiving projects.")
                                .

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                })
                }
            }
        });
    }
}
