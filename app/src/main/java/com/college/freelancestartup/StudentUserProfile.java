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

import com.google.android.material.button.MaterialButton;

class StudentUserProfile extends AppCompatActivity {

    private EditText phoneNumberET, departmentET, semesterET, emailET, DOBET, universityET, bioET;
    private MaterialButton applyChanges, cancelChanges;
    private Toolbar studentProfileToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_user_profile_settings);

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

            departmentET.setFocusable(true);
            departmentET.setFocusableInTouchMode(true);
            departmentET.setClickable(true);

            semesterET.setFocusable(true);
            semesterET.setFocusableInTouchMode(true);
            semesterET.setClickable(true);

            emailET.setFocusable(true);
            emailET.setFocusableInTouchMode(true);
            emailET.setClickable(true);

            DOBET.setFocusable(true);
            DOBET.setFocusableInTouchMode(true);
            DOBET.setClickable(true);

            universityET.setFocusable(true);
            universityET.setFocusableInTouchMode(true);
            universityET.setClickable(true);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
