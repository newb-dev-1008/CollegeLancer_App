package com.college.freelancestartup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ComplaintsFragment extends Fragment {

    private View root;
    private MaterialButton lodgeComplaintButton, writeComplaintEmailButton;
    private EditText complaintET;
    private FirebaseAuth firebaseAuth;
    private TabLayout tabLayout;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.help_complaints_frag, container, false);

        lodgeComplaintButton = root.findViewById(R.id.lodgeComplaintButton);
        writeComplaintEmailButton = root.findViewById(R.id.writeToUsButton);
        complaintET = root.findViewById(R.id.complaintsET);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        complaintET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lodgeComplaintButton.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(complaintET.getText().toString())){
                    lodgeComplaintButton.setVisibility(View.GONE);
                } else {
                    lodgeComplaintButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(complaintET.getText().toString())){
                    lodgeComplaintButton.setVisibility(View.GONE);
                } else {
                    lodgeComplaintButton.setVisibility(View.VISIBLE);
                }
            }
        });

        return root;
    }
}
