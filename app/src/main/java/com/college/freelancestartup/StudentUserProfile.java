package com.college.freelancestartup;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

class StudentUserProfile extends AppCompatActivity {

    private EditText phoneNumberET, departmentET, semesterET, emailET, DOBET, universityET, bioET;
    private MaterialButton applyChanges, cancelChanges;
    private Toolbar studentProfileToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_user_profile_settings);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        phoneNumberET = findViewById(R.id.student_profilePhNo);
        departmentET = findViewById(R.id.student_profileDepartment);
        semesterET = findViewById(R.id.student_profileSemester);
        emailET = findViewById(R.id.student_profileEmail);
        DOBET = findViewById(R.id.student_profileDOB);
        universityET = findViewById(R.id.student_profileUniversity);
        bioET = findViewById(R.id.student_profileBio);

        applyChanges = findViewById(R.id.student_apply_changes);
        cancelChanges = findViewById(R.id.student_cancel_changes);

        studentProfileToolbar = findViewById(R.id.student_profileToolbar);
        setSupportActionBar(studentProfileToolbar);

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String dbPhoneNumber = documentSnapshot.get("phoneNumber").toString();
                                String dbDepartment = documentSnapshot.get("department").toString();
                                String dbSemester = documentSnapshot.get("studentSemester").toString();
                                String dbEmail = documentSnapshot.get("emailID").toString();
                                String dbDOB = documentSnapshot.get("dateOfBirth").toString();
                                String dbUniversity = documentSnapshot.get("university").toString();
                                String dbBio = documentSnapshot.get("userBio").toString();


                            }
                        })
            }
        });

        cancelChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.editProfile) {
            Toast.makeText(StudentUserProfile.this, "Edit your profile. You can now change the contents.", Toast.LENGTH_LONG).show();

            phoneNumberET.setFocusable(true);
            phoneNumberET.setFocusableInTouchMode(true);
            phoneNumberET.setClickable(true);
            phoneNumberET.setCursorVisible(true);

            departmentET.setFocusable(true);
            departmentET.setFocusableInTouchMode(true);
            departmentET.setClickable(true);
            departmentET.setCursorVisible(true);

            semesterET.setFocusable(true);
            semesterET.setFocusableInTouchMode(true);
            semesterET.setClickable(true);
            semesterET.setCursorVisible(true);

            emailET.setFocusable(true);
            emailET.setFocusableInTouchMode(true);
            emailET.setClickable(true);
            emailET.setCursorVisible(true);

            DOBET.setFocusable(true);
            DOBET.setFocusableInTouchMode(true);
            DOBET.setClickable(true);
            DOBET.setCursorVisible(true);

            universityET.setFocusable(true);
            universityET.setFocusableInTouchMode(true);
            universityET.setClickable(true);
            universityET.setCursorVisible(true);

            bioET.setFocusable(true);
            bioET.setFocusableInTouchMode(true);
            bioET.setClickable(true);
            bioET.setCursorVisible(true);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
