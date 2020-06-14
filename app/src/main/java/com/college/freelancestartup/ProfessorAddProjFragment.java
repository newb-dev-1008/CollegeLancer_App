package com.college.freelancestartup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfessorAddProjFragment extends Fragment {

    private EditText profProjectTitle, profProjectDescription, profProjectPriority;
    private MaterialButton profCreateProjectButton;

    private View root;

    public ProfessorAddProjFragment(){
        // Empty Constructor Added
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.prof_addproj_frag, container, false);

        profProjectTitle = root.findViewById(R.id.profProjectTitleET);
        profProjectDescription = root.findViewById(R.id.profProjectDescriptionET);
        profProjectPriority = root.findViewById(R.id.profProjectPriorityET);
        profCreateProjectButton = root.findViewById(R.id.profCreateProjectButton);

        profCreateProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfCreatedProject();
            }
        });

        return root;
    }

    private void saveProfCreatedProject(){
        String projectTitle = profProjectTitle.getText().toString();
        String projectDescription = profProjectDescription.getText().toString();
        int projectPriority = Integer.parseInt(profProjectPriority.getText().toString());

        if (projectTitle.trim().isEmpty() || projectDescription.trim().isEmpty()){
            Toast.makeText(getContext(),"Please enter a valid project title and description.", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference addProjectRef = FirebaseFirestore.getInstance()
                .collection("Projects");
        addProjectRef.add(new StudentProjectsListClass(projectTitle, projectDescription, projectPriority));
        Toast.makeText(getContext(),"Project added.", Toast.LENGTH_SHORT).show();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.prof_fragment_container, new ProfessorHomeFragment());

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}