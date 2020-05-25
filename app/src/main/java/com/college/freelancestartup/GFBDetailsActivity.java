package com.college.freelancestartup;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GFBDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button signupButton;
    private EditText phoneNumberET, universityET, nameET, dateOfBirthET;
    private RadioGroup userType;
    private FirebaseAuth firebaseAuth;
    private Spinner deptEngg, studentSem;
    private String KEY_NAME = "name";
    private String KEY_PH_NO = "phoneNumber";
    private String KEY_UNI = "university";
    private String KEY_DEPT = "department";
    private String KEY_STUD_SEM = "studentSemester";
    private String KEY_DOB = "dateOfBirth";
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private String DOB;

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
        dateOfBirthET = findViewById(R.id.dateOfBirthET);

        firebaseAuth = FirebaseAuth.getInstance();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dateOfBirthET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        GFBDetailsActivity.this,
                        android.R.style.Theme_Light,
                        dateSetListener,
                        day, month, year);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year) {
                String day_date = Integer.toString(day);
                String month_date = Integer.toString(month);
                String year_date = Integer.toString(year);
                DOB = day_date + '/' + month_date + '/' + year_date;
                dateOfBirthET.setText(DOB);
            }
        };

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(signupButton.getWindowToken(), 0);
                registerUser();
            }
        });
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
        } else if (deptEngg.getSelectedItem() == "Select your branch of study/ department"){
            Toast.makeText(this, "Please select your department.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(DOB)){
            dateOfBirthET.setError("Please select your date of birth.");
        } else if (studentSem.getSelectedItem() == "Select your current semester"){
            Toast.makeText(this, "Select your current semester of study.", Toast.LENGTH_SHORT).show();
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
                            userDetails.put(KEY_NAME, name);
                            userDetails.put(KEY_PH_NO, phoneNumber);
                            userDetails.put(KEY_UNI, university);
                            userDetails.put(KEY_DOB, DOB);

                            db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getUid()).set(userDetails, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(GFBDetailsActivity.this, "Details added successfully, proceeding...", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(GFBDetailsActivity.this, WelcomeTestScreen.class);
                                            finish();
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(GFBDetailsActivity.this, "Could not store details in database. Please check your Internet connection.", Toast.LENGTH_SHORT).show();
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
