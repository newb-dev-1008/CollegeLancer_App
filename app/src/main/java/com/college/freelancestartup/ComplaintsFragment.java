package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ComplaintsFragment extends Fragment {

    private View root;
    private TextView complaintsTitleTV, lodgeComplaintTV, writeTV, faqTV;
    private View line, line2;
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

        complaintsTitleTV = root.findViewById(R.id.complaintsTitleTV);
        lodgeComplaintTV = root.findViewById(R.id.lodgeComplaintTV);
        writeTV = root.findViewById(R.id.writeToUsTV);
        line = root.findViewById(R.id.line);
        line2 = root.findViewById(R.id.line2);
        faqTV = root.findViewById(R.id.FAQTitleTV);

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

        /*
        lodgeComplaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                        .collection("Complaints").document(complaintET.getText().toString());
            }
        });
         */

        writeComplaintEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog writeEmailComplaint = new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Redirecting to Email")
                        .setMessage("You will now be redirected to your Email service.\n" +
                                "For a faster response, kindly keep the auto-generated subject unchanged.")
                        .setPositiveButton("Okay, Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text");
                                intent.putExtra(Intent.EXTRA_EMAIL, "collegelancer9669@gmail.com");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Complaint: " + firebaseAuth.getCurrentUser().getDisplayName());
                            }
                        }).setNegativeButton("Cancel", null)
                        .create();
                writeEmailComplaint.show();
                writeEmailComplaint.setCancelable(true);
                writeEmailComplaint.setCanceledOnTouchOutside(false);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.equals(R.id.new_tab)){
                    registerNewComplaint();
                } else {
                    showRegisteredComplaints();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return root;
    }

    private void registerNewComplaint(){
        complaintsTitleTV.setVisibility(View.VISIBLE);
        faqTV.setVisibility(View.VISIBLE);
        writeTV.setVisibility(View.VISIBLE);
        writeComplaintEmailButton.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
        complaintET.setVisibility(View.VISIBLE);
        line2.setVisibility(View.VISIBLE);
        lodgeComplaintTV.setVisibility(View.VISIBLE);
    }
}
