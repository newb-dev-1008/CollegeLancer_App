package com.college.freelancestartup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authStateListener;
    private String UIDEmailID;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private boolean running, wasrunning;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        running = true;
        if (savedInstanceState != null){
            running = savedInstanceState.getBoolean("running");
            wasrunning = savedInstanceState.getBoolean("wasrunning");
        }

        setContentView(R.layout.splash_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth1) {
                FirebaseUser user = firebaseAuth1.getCurrentUser();
                if (user != null){
                    UIDEmailID = user.getEmail();
                    updateUI(user);
                } else {
                    updateUI(null);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        if (firebaseAuth.getCurrentUser() != null) {
            UIDEmailID = firebaseAuth.getCurrentUser().getEmail();
            updateUI(firebaseAuth.getCurrentUser());
        } else {
            updateUI(null);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth1) {
                FirebaseUser user = firebaseAuth1.getCurrentUser();
                if (user != null) {
                    UIDEmailID = user.getEmail();
                    updateUI(user);
                } else {
                    updateUI(null);
                }
            }
        };
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
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void updateUI(FirebaseUser firebaseUser){
        if (firebaseUser != null){
            Toast.makeText(this, "User " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
            db.collection("Users").document("User " + UIDEmailID).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.get("userType") != null) {
                                if (documentSnapshot.get("userType").equals("Lecturer/ Professor")){
                                    Intent intent = new Intent(SplashScreenActivity.this, ProfessorMainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    finish();
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(SplashScreenActivity.this, StudentMainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    finish();
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(SplashScreenActivity.this, "We need some additional details before we go ahead.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SplashScreenActivity.this, GFBDetailsActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                                startActivity(intent);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SplashScreenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        else{
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
        }
    }
}