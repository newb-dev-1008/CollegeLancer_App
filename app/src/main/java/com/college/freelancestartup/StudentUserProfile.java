package com.college.freelancestartup;

import android.os.Bundle;
import android.widget.EditText;

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
    }


}
