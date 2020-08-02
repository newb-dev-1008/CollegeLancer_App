package com.college.freelancestartup;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class GFBDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button signupButton;
    private EditText phoneNumberET, universityET, nameET, bioET;
    private TextView dateOfBirthET, studentSemTV;
    private RadioGroup userType;
    private RadioButton userTypeSelectedRadioButton;
    private AutoCompleteTextView skills;
    private ChipGroup chipGroup;
    private FirebaseAuth firebaseAuth;
    private Spinner deptEngg, studentSem;
    private String userTypeSelected;
    private String KEY_NAME = "name";
    private String KEY_PH_NO = "phoneNumber";
    private String KEY_UNI = "university";
    private String KEY_DEPT = "department";
    private String KEY_STUD_SEM = "studentSemester";
    private String KEY_DOB = "dateOfBirth";
    private String KEY_USER_TYPE = "userType";
    private String KEY_BIO = "userBio";
    private String KEY_STUDSTATUS = "studentStatus";
    private String KEY_NOCOLLABS = "numberCollabs";
    private String KEY_NOPROJECTS = "numberProjects";
    private String KEY_STUDENTSKILLS = "studentSkills";
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private String DOB;
    private Date DOBDate, currentDate;
    private Calendar selectedDate;
    private String[] skillArray;

    private boolean running, wasrunning;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extradetails);
        running = true;
        if (savedInstanceState != null){
            running = savedInstanceState.getBoolean("running");
            wasrunning = savedInstanceState.getBoolean("wasrunning");
        }

        signupButton = findViewById(R.id.signupButtonGFB);
        phoneNumberET = findViewById(R.id.phoneNumberEditText);
        universityET = findViewById(R.id.universityEditText);
        userType = findViewById(R.id.userTypeRadioGroup);
        nameET = findViewById(R.id.nameEditText);
        deptEngg = findViewById(R.id.deptEnggSpinner);
        studentSemTV = findViewById(R.id.studentSemTV);
        studentSem = findViewById(R.id.studentSemSpinner);
        dateOfBirthET = findViewById(R.id.dateOfBirthET);
        bioET = findViewById(R.id.student_profileBio);

        chipGroup = findViewById(R.id.mainTagChipGroup);
        skills = findViewById(R.id.student_Skills);
        skillArray = getResources().getStringArray(R.array.skills);
        currentDate = Calendar.getInstance().getTime();
        selectedDate = Calendar.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ArrayAdapter<String> skillsAdapter = new ArrayAdapter<String>(GFBDetailsActivity.this, android.R.layout.simple_dropdown_item_1line, skillArray);

        skills.setAdapter(skillsAdapter);
        skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skills.showDropDown();
            }
        });

        skills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                skills.setText(null);
                String skillName = adapterView.getItemAtPosition(i).toString();
                addSkills(skillName);
            }
        });

        dateOfBirthET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "DatePicker");
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(signupButton.getWindowToken(), 0);
                registerUser();
            }
        });

        userType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedButton = findViewById(i);
                String selectedType = checkedButton.getText().toString();
                if (!selectedType.equals("Lecturer/ Professor")){
                    studentSem.setVisibility(View.VISIBLE);
                    studentSemTV.setVisibility(View.VISIBLE);
                }
                else{
                    studentSem.setVisibility(View.GONE);
                    studentSemTV.setVisibility(View.GONE);
                }
            }
        });
    }

    private String checkUserType(){
        int userTypeSelectedRadioID = userType.getCheckedRadioButtonId();
        userTypeSelectedRadioButton = findViewById(userTypeSelectedRadioID);
        userTypeSelected = userTypeSelectedRadioButton.getText().toString();

        return userTypeSelected;
    }

    private void registerUser() {
        final String name = nameET.getText().toString().trim();
        final String phoneNumber = phoneNumberET.getText().toString().trim();
        final String university = universityET.getText().toString().trim();
        final String bio = bioET.getText().toString().trim();
        final String status = "Available for projects/ research";
        // Checking if the fields are empty
        if (TextUtils.isEmpty(phoneNumber) || (phoneNumber.length() < 10)) {
            phoneNumberET.setError("You've not entered a valid phone number.");
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
        } else if (((checkDOBValidity(currentDate, DOBDate) > 0) && (checkDOBValidity(currentDate, DOBDate) < 18))){
            Toast.makeText(this, "Your age: " + Integer.toString((checkDOBValidity(currentDate, DOBDate))), Toast.LENGTH_SHORT).show();
            dateOfBirthET.setError("You need to be at least 18 years old to join.");
            // Toast.makeText(this, "You need to be at least 18 years old to join.", Toast.LENGTH_SHORT).show();
        } else if (checkDOBValidity(currentDate, DOBDate) <= 0) {
            Toast.makeText(this, "Enter your actual date of birth. You're too young to be able to even to use this app.", Toast.LENGTH_LONG).show();
            dateOfBirthET.setError("Enter your actual date of birth.");
        } else if (checkDOBValidity(currentDate, DOBDate) >= 27){
            Toast.makeText(this, "The maximum permissible age for using this app is 26 years. You need to be a college student for signing up with us.", Toast.LENGTH_LONG).show();
            dateOfBirthET.setError("You do not seem to be a college undergraduate.");
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
                            String proStud = checkUserType();
                            Toast.makeText(GFBDetailsActivity.this, checkUserType(), Toast.LENGTH_SHORT).show();
                            Map<String, Object> userDetails = new HashMap<>();
                            userDetails.put(KEY_NAME, name);
                            userDetails.put(KEY_PH_NO, phoneNumber);
                            userDetails.put(KEY_UNI, university);
                            userDetails.put(KEY_DOB, DOB);
                            userDetails.put(KEY_USER_TYPE, checkUserType());
                            userDetails.put(KEY_DEPT, deptEngg.getSelectedItem());
                            userDetails.put(KEY_STUD_SEM, studentSem.getSelectedItem());
                            userDetails.put(KEY_BIO, bio);
                            userDetails.put(KEY_STUDSTATUS, status);
                            userDetails.put(KEY_NOCOLLABS, 0);
                            userDetails.put(KEY_NOPROJECTS, 0);

                            db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail()).set(userDetails, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(GFBDetailsActivity.this, "Details added successfully, proceeding...", Toast.LENGTH_LONG).show();
                                            if (proStud.equals("Lecturer/ Professor")){
                                                Intent intent = new Intent(GFBDetailsActivity.this, ProfessorMainActivity.class);
                                                finish();
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                            else{
                                                Intent intent = new Intent(GFBDetailsActivity.this, StudentMainActivity.class);
                                                finish();
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(YEAR, year);
        c.set(MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        DOBDate = c.getTime();
        selectedDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        DOB = DateFormat.getDateInstance(DateFormat.FULL).format(DOBDate);
        dateOfBirthET.setText(DOB);
    }

    private int checkDOBValidity(Date cDate, Date bDate){
        Calendar a = getCalendar(bDate);
        Calendar b = getCalendar(cDate);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(Calendar.DAY_OF_MONTH) > b.get(Calendar.DAY_OF_MONTH))) {
            diff--;
        }

        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTime(date);
        return cal;
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasrunning = running;
        running = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasrunning){
            running = true;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // check if this is necessary
    }
}