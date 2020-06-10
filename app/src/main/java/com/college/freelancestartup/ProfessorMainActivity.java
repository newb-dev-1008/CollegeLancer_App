package com.college.freelancestartup;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfessorMainActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prof_home);

        Toolbar profToolbar = findViewById(R.id.profToolbar);
        setSupportActionBar(profToolbar);

        drawer = findViewById(R.id.prof_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, profToolbar,
                R.string.open_nav_bar, R.string.close_nav_bar);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
