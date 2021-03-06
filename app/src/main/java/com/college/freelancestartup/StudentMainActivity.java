package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.login.LoginManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class StudentMainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private FirebaseAuth firebaseAuth;
    private LoginManager fbLoggedIn;
    private boolean running, wasrunning;
    private String goToFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);
        running = true;
        if (savedInstanceState != null){
            running = savedInstanceState.getBoolean("running");
            wasrunning = savedInstanceState.getBoolean("wasrunning");
        }

        Intent recIntent = getIntent();

        if (recIntent.hasExtra("Go_to_fragment_NewComplaint")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                    new NewComplaintsFragment()).addToBackStack(null).commit();
        } else if (recIntent.hasExtra("Go_to_fragment_addProj")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                    new StudentFindCollabsFragment()).addToBackStack(null).commit();
        }

        Toolbar studentToolbar = findViewById(R.id.studentToolbar);
        setSupportActionBar(studentToolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        fbLoggedIn = LoginManager.getInstance();

        drawer = findViewById(R.id.student_drawer_layout);
        NavigationView studentNavigationView = findViewById(R.id.student_navigation_drawer);

        BottomNavigationView studentNavBottom = findViewById(R.id.studentBottomNav);

        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                new StudentHomeFragment()).addToBackStack(null).commit();

        studentNavBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.studentHome:
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new StudentHomeFragment(), "StudentHomeFrag").addToBackStack("StudentHomeFrag").commit();
                        break;
                    case R.id.studentAddProj:
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new StudentAddProjFragment(), "StudentAddProjFragment").addToBackStack("StudentAddProjFragment").commit();
                        break;
                    case R.id.studentOrgs:
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new StudentOrgsFragment(), "StudentOrgsFrag").addToBackStack("StudentOrgsFrag").commit();
                        break;
                    case R.id.studentProf:
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new StudentProfFragment(), "StudentProfFragment").addToBackStack("StudentProfFragment").commit();
                        break;
                }

                return true;
            }
        });

        studentNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.studentFindCollabs:
                        studentNavBottom.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new StudentFindCollabsFragment(), "StudentFindCollabsFrag").addToBackStack("StudentFindCollabsFrag").commit();
                        break;
                    case R.id.studentSettings:
                        studentNavBottom.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new StudentSettingsFragment(), "StudentSettingsFrag").addToBackStack("StudentSettingsFrag").commit();
                        break;
                    case R.id.studentReportBug:
                        studentNavBottom.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
                                new ReportBugFragment(), "StudentReportBugFrag").addToBackStack("StudentReportBugFrag").commit();
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
                    new StudentHomeFragment()).addToBackStack(null).commit();
        }

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
            finish();
        }
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
