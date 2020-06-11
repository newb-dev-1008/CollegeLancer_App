package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class StudentMainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private FirebaseAuth firebaseAuth;
    private LoginManager fbLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);

        Toolbar studentToolbar = findViewById(R.id.studentToolbar);
        setSupportActionBar(studentToolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        fbLoggedIn = LoginManager.getInstance();

        drawer = findViewById(R.id.student_drawer_layout);
        NavigationView studentNavigationView = findViewById(R.id.student_navigation_drawer);

        studentNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.studentFindCollabs:
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new StudentFindCollabsFragment()).commit();
                    case R.id.studentSettings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new StudentSettingsFragment()).commit();
                        break;
                    case R.id.studentReportBug:
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new ReportBugFragment()).commit();
                        break;
                    case R.id.studentLogOut:
                        AlertDialog studentLogOutConfirm = new MaterialAlertDialogBuilder(StudentMainActivity.this)
                                .setTitle("Logout")
                                .setMessage("Are you sure you want to log out?")
                                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        firebaseAuth.signOut();
                                        fbLoggedIn.logOut();
                                        Intent intent = new Intent(StudentMainActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Cancel", null).create();
                        studentLogOutConfirm.setCanceledOnTouchOutside(false);
                        studentLogOutConfirm.setCancelable(true);
                        studentLogOutConfirm.show();
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, studentToolbar,
                R.string.open_nav_bar, R.string.close_nav_bar);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                    new StudentHomeFragment()).commit();
        }

        BottomNavigationView studentNavBottom = findViewById(R.id.studentBottomNav);

        getSupportFragmentManager().beginTransaction().replace(R.id.studentHomeFragContainer,
                new StudentHomeFragment()).commit();

        studentNavBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.studentHome:
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new StudentHomeFragment()).commit();
                        break;
                    case R.id.studentAddProj:
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new StudentAddProjFragment()).commit();
                        break;
                    case R.id.studentOrgs:
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new StudentOrgsFragment()).commit();
                        break;
                    case R.id.studentProf:
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new StudentProfFragment()).commit();
                        break;
                }

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
