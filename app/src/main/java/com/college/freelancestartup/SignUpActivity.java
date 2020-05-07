package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class SignUpActivity extends AppCompatActivity {

    private Button signUp, signUpGoogle;
    private LoginButton signUpFB;
    private EditText signUpEmailET, signUpPwET, signUpConfPwET, signUpPhoneNoET;
    private FirebaseAuth firebaseAuth;
    private  CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        firebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());

        signUp = findViewById(R.id.signupButton);
        signUpGoogle = findViewById(R.id.signupGoogleButton);
        signUpEmailET = findViewById(R.id.signupEmailEditText);
        signUpPwET = findViewById(R.id.signupPasswordEditText);
        signUpPhoneNoET = findViewById(R.id.signupPhoneNoEditText);
        signUpConfPwET = findViewById(R.id.signupConfPasswordEditText);
        signUpFB = findViewById(R.id.signupFBButton);

        signUpFB.setPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();
        signUpFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(SignUpActivity.this, "Cancelled login.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SignUpActivity.this, "Failed to connect to Facebook. Try again.", Toast.LENGTH_SHORT).show();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if Password and Confirmed Password are matching
                // Set the EditTexts to red if they aren't
                // --
                registerUser();
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    updateUI(user);
                }
                else{
                    updateUI(null);
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    firebaseAuth.signOut();
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookToken(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Toast.makeText(SignUpActivity.this, "Welcome.", Toast.LENGTH_SHORT).show();
                    updateUI(user);
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Couldn't connect to Facebook. Try Again.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user){
        // Update UI after login
        if (user != null){
            Toast.makeText(SignUpActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(SignUpActivity.this, "Who the fuck are you, identify yourself nigga", Toast.LENGTH_SHORT).show();
        }
    }

    // Checking if a user is currently signed in
    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        // FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void registerUser() {
        // Receiving Email and other stuff from EditTexts
        final String emailID = signUpEmailET.getText().toString().trim();
        // int phoneNo = Integer.parseInt(signUpPhoneNoET.getText().toString().trim());
        final String password = signUpPwET.getText().toString().trim();
        String confPassword = signUpConfPwET.getText().toString().trim();

        // Checking if the fields are empty
        if (TextUtils.isEmpty(emailID)) {
            signUpEmailET.setError("You've not entered your Email ID.");
        }
        // Checking if Email ID entered is valid using function defined below
        else if (!isEmailValid(emailID)) {
            signUpEmailET.setError("Please enter a valid Email ID.");
        }
        else if (!password.equals(confPassword)){
            signUpConfPwET.setError("Your passwords do not match. Check again.");
        }
        //Submit Details to Firebase and receive OTP
        else {
            // Creating a dialog box for confirmation
            AlertDialog signupConfirm = new MaterialAlertDialogBuilder(this)
                    .setTitle("Sure you're good to go?")
                    .setMessage("Make sure you recheck your details.")
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // Check if Internet connection is established
                            // --
                            firebaseAuth.createUserWithEmailAndPassword(emailID, password)
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(SignUpActivity.this, "Created Account Successfully.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SignUpActivity.this, WelcomeTestScreen.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                Toast.makeText(SignUpActivity.this, "Couldn't register. Please retry.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("Go Back", null)
                    .create();
            signupConfirm.setCanceledOnTouchOutside(false);
            signupConfirm.setCancelable(false);
            signupConfirm.show();
        }
    }

    // Function to check if entered Email IDs are valid
    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}