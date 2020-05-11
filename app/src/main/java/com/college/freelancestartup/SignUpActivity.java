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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private Button signUp;
    private SignInButton signUpGoogle;
    private LoginButton signUpFB;
    private EditText signUpEmailET, signUpPwET, signUpConfPwET, signUpPhoneNoET, signUpNameET;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private GoogleSignInClient googleSignInClient;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int RC_SIGN_IN = 1;

    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHNO = "phoneNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        firebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());

        signUpNameET = findViewById(R.id.signupNameEditText);
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

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

        signUpGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent googleSignInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(googleSignInIntent, RC_SIGN_IN);
                Toast.makeText(SignUpActivity.this, "Select your Google account.", Toast.LENGTH_SHORT).show();;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RC_SIGN_IN){
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        if (task.isSuccessful()){
            handleSignInResult(task);
        }
        else{
            Toast.makeText(SignUpActivity.this, "Cancelled Google Login.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Toast.makeText(LoginActivity.this, "Entered onActivityResult's if block.", Toast.LENGTH_SHORT).show();
    }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(this, "Signed in via Google.", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
            String nameGoogle = acc.getDisplayName();
            String emailIDGoogle = acc.getEmail();
            // String phoneNoGoogle = firebaseAuth.getCurrentUser().getPhoneNumber()

            Map<String, Object> usersMap = new HashMap<>();
            // Toast.makeText(SignUpActivity.this, "EmailAuth Success", Toast.LENGTH_SHORT).show();
            usersMap.put(KEY_NAME, nameGoogle);
            usersMap.put(KEY_EMAIL, emailIDGoogle);
            // usersMap.put(KEY_PHNO, phoneNoGoogle);

            db.collection("Users").document("User " + emailIDGoogle).set(usersMap)
                    .addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SignUpActivity.this, "Your details have been saved.", Toast.LENGTH_SHORT).show();
                            // Toast.makeText(SignUpActivity.this, "Created account, please wait...", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (ApiException e){
            Toast.makeText(this, "Couldn't sign in using Google. Try again.", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "Signed In. (Google)", Toast.LENGTH_SHORT).show();

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Sign in failed. (Google)", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
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
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user){
        // Update UI after login
        if (user != null){
            Toast.makeText(SignUpActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), WelcomeTestScreen.class);
            finish();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    // Checking if a user is currently signed in
    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        if (firebaseAuth.getCurrentUser() != null){
            updateUI(firebaseAuth.getCurrentUser());
        }
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
        final String confPassword = signUpConfPwET.getText().toString().trim();
        final String phoneNo = signUpPhoneNoET.getText().toString().trim();
        final String name = signUpNameET.getText().toString().trim();

        // Checking if the fields are empty
        if (TextUtils.isEmpty(emailID)) {
            signUpEmailET.setError("You've not entered your Email ID.");
        }
        else if (TextUtils.isEmpty(phoneNo)) {
            signUpPhoneNoET.setError("Enter your phone number.");
        }
        else if (TextUtils.isEmpty(name)){
            signUpNameET.setError("You need to enter your full name.");
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
                        public void onClick(final DialogInterface dialogInterface, int i) {
                            // Check if Internet connection is established
                            // --
                            firebaseAuth.createUserWithEmailAndPassword(emailID, password)
                                    .addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Map<String, Object> usersMap = new HashMap<>();
                                            // Toast.makeText(SignUpActivity.this, "EmailAuth Success", Toast.LENGTH_SHORT).show();
                                            usersMap.put(KEY_NAME, name);
                                            usersMap.put(KEY_EMAIL, emailID);
                                            usersMap.put(KEY_PHNO, phoneNo);

                                            firebaseAuth.signInWithEmailAndPassword(emailID, password)
                                                    .addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<AuthResult>() {
                                                        @Override
                                                        public void onSuccess(AuthResult authResult) {
                                                            Toast.makeText(SignUpActivity.this, "Created Account Successfully, signing you in...", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(SignUpActivity.this, WelcomeTestScreen.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        }
                                                    })
                                                    .addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                            db.collection("Users").document("User " + emailID).set(usersMap)
                                                    .addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(SignUpActivity.this, "Your details have been saved.", Toast.LENGTH_SHORT).show();
                                                            // Toast.makeText(SignUpActivity.this, "Created account, please wait...", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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