package com.college.freelancestartup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authStateListener;
    private String UIDEmailID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth1) {
                FirebaseUser user = firebaseAuth1.getCurrentUser();
                if (user != null) {
                    UIDEmailID = user.getEmail();
                    updateUI(user);
                }
            }
        };
    }
}
