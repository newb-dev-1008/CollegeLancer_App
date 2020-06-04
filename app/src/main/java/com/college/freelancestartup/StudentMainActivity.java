package com.college.freelancestartup;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);

        BottomNavigationView studentNavBottom = findViewById(R.id.studentHomeNav);

        studentNavBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment;

                switch (item.getItemId()){
                    case R.id.studentHome:
                        selectedFragment = new StudentHomeFragment();
                        break;
                    case R.id.studentAddProj:
                        selectedFragment = new StudentAddProjFragment();
                        break;
                    case R.id.studentOrgs:
                        selectedFragment = new StudentOrgsFragment();
                        break;
                    case R.id.studentProf:
                        selectedFragment = new StudentProfFragment();
                        break;
                }
            }
        });
    }

}
