package com.college.freelancestartup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeTestScreen extends AppCompatActivity {

    private Button signOutTest;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private LoginManager fbLoggedIn;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_test);

        signOutTest = findViewById(R.id.signOutTest);
        firebaseAuth = FirebaseAuth.getInstance();
        fbLoggedIn = LoginManager.getInstance();

        //GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        //        .requestIdToken(getString(R.string.default_web_client_id))
        //        .requestEmail()
        //        .build();

        //googleSignInClient = GoogleSignIn.getClient(this, gso);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Toast.makeText(WelcomeTestScreen.this, "AuthStateListener Notified.", Toast.LENGTH_SHORT).show();
                if (firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(WelcomeTestScreen.this, "Signed Out Successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WelcomeTestScreen.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                    startActivity(intent);
                }
            }
        };

        signOutTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                fbLoggedIn.logOut();
                // googleSignInClient.signOut();
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    firebaseAuth.signOut();
                    Intent intent = new Intent(WelcomeTestScreen.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                    startActivity(intent);
                    Toast.makeText(WelcomeTestScreen.this, "Signed Out of Facebook Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}