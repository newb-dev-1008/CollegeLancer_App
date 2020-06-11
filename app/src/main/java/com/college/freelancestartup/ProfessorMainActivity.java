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

public class ProfessorMainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private FirebaseAuth firebaseAuth;
    private LoginManager fbLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prof_home);

        Toolbar profToolbar = findViewById(R.id.profToolbar);
        setSupportActionBar(profToolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        fbLoggedIn = LoginManager.getInstance();

        drawer = findViewById(R.id.prof_drawer_layout);
        NavigationView profNavigationView = findViewById(R.id.prof_navigation_drawer);
        profNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profSettings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.prof_fragment_container,
                                new ProfessorSettingsFragment()).commit();
                        break;
                    case R.id.profReportBug:
                        getSupportFragmentManager().beginTransaction().replace(R.id.prof_fragment_container,
                                new ReportBugFragment()).commit();
                        break;
                    case R.id.profLogOut:
                        AlertDialog profLogOutConfirm = new MaterialAlertDialogBuilder(ProfessorMainActivity.this)
                                .setTitle("Logout")
                                .setMessage("Are you sure you want to log out?")
                                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        firebaseAuth.signOut();
                                        fbLoggedIn.logOut();
                                        Intent intent = new Intent(ProfessorMainActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Cancel", null).create();
                        profLogOutConfirm.setCanceledOnTouchOutside(false);
                        profLogOutConfirm.setCancelable(true);
                        profLogOutConfirm.show();
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, profToolbar,
                R.string.open_nav_bar, R.string.close_nav_bar);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.prof_fragment_container,
                    new ProfessorHomeFragment()).commit();
        }

        BottomNavigationView profNavBottom = findViewById(R.id.profBottomNav);

        getSupportFragmentManager().beginTransaction().replace(R.id.prof_fragment_container,
                new ProfessorHomeFragment()).commit();

        profNavBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profHome:
                        getSupportFragmentManager().beginTransaction().replace(R.id.prof_fragment_container,
                                new ProfessorHomeFragment()).commit();
                        break;
                    case R.id.profAddProj:
                        getSupportFragmentManager().beginTransaction().replace(R.id.prof_fragment_container,
                                new ProfessorAddProjFragment()).commit();
                        break;
                    case R.id.profStudent:
                        getSupportFragmentManager().beginTransaction().replace(R.id.prof_fragment_container,
                                new ProfessorStudentFragment()).commit();
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
