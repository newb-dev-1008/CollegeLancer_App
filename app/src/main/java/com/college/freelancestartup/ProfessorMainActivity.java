package com.college.freelancestartup;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfessorMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);

        BottomNavigationView studentNavBottom = findViewById(R.id.studentHomeNav);

        getSupportFragmentManager().beginTransaction().replace(R.id.studentHomeFragContainer,
                new StudentHomeFragment()).commit();

        studentNavBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.profHome:
                        selectedFragment = new ProfessorHomeFragment();
                        break;
                    case R.id.profAddProj:
                        selectedFragment = new ProfessorAddProjFragment();
                        break;
                    case R.id.profStudent:
                        selectedFragment = new ProfessorStudentFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.studentHomeFragContainer,
                        selectedFragment).commit();

                return true;
            }
        });
    }
}
