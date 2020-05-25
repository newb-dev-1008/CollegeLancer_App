package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GFBDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button signupButton;
    private EditText phoneNumberET, universityET, nameET;
    private RadioGroup userType;
    private FirebaseAuth firebaseAuth;
    private Spinner deptEngg, studentSem;
    private String KEY_PH_NO = "phoneNumber";
    private String KEY_UNI = "university";
    private String KEY_DEPT = "department";
    private String KEY_STUD_SEM = "studentSemester";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extradetails);

        signupButton = findViewById(R.id.signupButtonGFB);
        phoneNumberET = findViewById(R.id.phoneNumberEditText);
        universityET = findViewById(R.id.universityEditText);
        userType = findViewById(R.id.userTypeRadioGroup);
        nameET = findViewById(R.id.nameEditText);
        deptEngg = findViewById(R.id.deptEnggSpinner);
        studentSem = findViewById(R.id.studentSemSpinner);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void registerUser() {
        final String name = nameET.getText().toString().trim();
        final String phoneNumber = phoneNumberET.getText().toString().trim();
        final String university = universityET.getText().toString().trim();
        // Checking if the fields are empty
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberET.setError("You've not entered your phone number.");
        } else if (TextUtils.isEmpty(name)) {
            nameET.setError("You need to enter your full name.");
        } else if (TextUtils.isEmpty(university)) {
            universityET.setError("Please enter your University name.");
        } else if (userType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Select your account type.", Toast.LENGTH_SHORT).show();
        }
        //Submit Details to Firebase and receive OTP
        else {
            // Creating a dialog box for confirmation
            AlertDialog signupConfirm = new MaterialAlertDialogBuilder(this)
                    .setTitle("Sure you're good to go?")
                    .setMessage("Make sure you recheck your details.")
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {
                            // Check if Internet connection is established
                            // --
                            Map<String, Object> userDetails = new HashMap<>();


                            db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getUid()).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            // Do something
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Do something
                                        }
                                    });
                        }
                    }).setNegativeButton("Go Back", null)
                    .create();
            signupConfirm.setCanceledOnTouchOutside(false);
            signupConfirm.setCancelable(false);
            signupConfirm.show();
        }
    }
}
