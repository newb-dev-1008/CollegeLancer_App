package com.college.freelancestartup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.rgb;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SignUpActivity extends AppCompatActivity {

    private Button signUp, checkEmailButton;
    private SignInButton signUpGoogle;
    private LoginButton signUpFB;
    private EditText signUpEmailET, signUpPwET, signUpConfPwET, signUpPhoneNoET, signUpNameET;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private GoogleSignInClient googleSignInClient;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RadioGroup userType;
    private RadioButton professor, student;
    private int RC_SIGN_IN = 1;
    public String userTypeDB;
    private String EmailID;

    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERTYPE = "userType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        firebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());

        signUpNameET = findViewById(R.id.signupNameEditText);
        signUp = findViewById(R.id.signupButton);
        checkEmailButton = findViewById(R.id.signupEnterEmailButton);
        signUpGoogle = findViewById(R.id.signupGoogleButton);
        signUpEmailET = findViewById(R.id.signupEmailEditText);
        signUpPwET = findViewById(R.id.signupPasswordEditText);
        signUpConfPwET = findViewById(R.id.signupConfPasswordEditText);
        signUpFB = findViewById(R.id.signupFBButton);
        userType = findViewById(R.id.userTypeRadioGroup);
        professor = findViewById(R.id.professor);
        student = findViewById(R.id.student);
        db.collection("Users");

        signUpFB.setPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        userType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.professor){
                    Toast.makeText(SignUpActivity.this, "Professor chosen.", Toast.LENGTH_SHORT).show();
                    userTypeDB = "Professor";
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Student chosen.", Toast.LENGTH_SHORT).show();
                    userTypeDB = "Student";
                }
            }
        });

        checkEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailID = signUpEmailET.getText().toString().trim();
                if (isEmailValid(EmailID) == true){
                    firebaseAuth.fetchSignInMethodsForEmail(EmailID).addOnCompleteListener(signInMethodsTask -> {
                        if (signInMethodsTask.isSuccessful()) {
                            List<String> signInMethods = signInMethodsTask.getResult().getSignInMethods();
                            if (signInMethods.isEmpty()) {
                                signUpEmailET.setVisibility(GONE);
                                checkEmailButton.setVisibility(GONE);
                                signUpNameET.setVisibility(VISIBLE);
                                signUpPwET.setVisibility(VISIBLE);
                                signUpConfPwET.setVisibility(VISIBLE);
                                signUpPhoneNoET.setVisibility(VISIBLE);
                                userType.setVisibility(VISIBLE);
                                signUp.setVisibility(VISIBLE);
                                signUpGoogle.setVisibility(VISIBLE);
                                signUpFB.setVisibility(VISIBLE);
                            }
                            else{
                                Toast.makeText(SignUpActivity.this, "You have already registered with that email ID. Please log in.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    signUpEmailET.setError("Please enter a valid Email ID.");
                }
            }
        });

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
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(signUp.getWindowToken(), 0);
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
                googleSignInClient.signOut();       // To make the dialog box for selecting Google account pop up everytime
                Intent googleSignInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(googleSignInIntent, RC_SIGN_IN);
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
            // Toast.makeText(this, "Signed in via Google.", Toast.LENGTH_SHORT).show();
            AuthCredential cred = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){
            Toast.makeText(this, "Couldn't sign in using Google. Try again.", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    /*private boolean userExists(final String emailIDs){
        CollectionReference usersRef = db.collection("Users");
        Query query = usersRef.whereEqualTo("email", emailIDs);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        String user = documentSnapshot.getString("email");

                        if(user.equals(emailIDs)){
                            userExistsFlag = 1;
                            break;
                        }
                    }
                }

                if(task.getResult().size() == 0 ){
                    Toast.makeText(SignUpActivity.this, "First time login.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (userExistsFlag == 1){
            return true;
        }
        else{
            return false;
        }
    }*/

    private void FirebaseGoogleAuth(final GoogleSignInAccount acct){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential).addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SignUpActivity.this, "Signed In. (Google)", Toast.LENGTH_SHORT).show();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String nameGoogle = acct.getDisplayName();
                String emailIDGoogle = acct.getEmail();

                Map<String, Object> usersMap = new HashMap<>();
                // Toast.makeText(SignUpActivity.this, "EmailAuth Success", Toast.LENGTH_SHORT).show();
                usersMap.put(KEY_NAME, nameGoogle);
                usersMap.put(KEY_EMAIL, emailIDGoogle);

                db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getUid()).set(usersMap)
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
                updateUI(user);
            }
        }).addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        });
    }

    private void handleFacebookToken(final AccessToken token){
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Toast.makeText(SignUpActivity.this, "Logged in via Facebook.", Toast.LENGTH_SHORT).show();
                String emailIDFB = token.getUserId();
                Map<String, Object> usersMap = new HashMap<>();
                // Toast.makeText(SignUpActivity.this, "EmailAuth Success", Toast.LENGTH_SHORT).show();
                usersMap.put(KEY_EMAIL, emailIDFB);

                db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getUid()).set(usersMap)
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
                updateUI(user);
            }
        }).addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void updateUI(FirebaseUser user){
        // Update UI after login
        if (user != null){
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
        final String name = signUpNameET.getText().toString().trim();
        // Checking if the fields are empty
        if (TextUtils.isEmpty(emailID)) {
            signUpEmailET.setError("You've not entered your Email ID.");
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
        else if (userType.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Select your account type.", Toast.LENGTH_SHORT).show();
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
                                            usersMap.put(KEY_USERTYPE, userTypeDB);
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
                                            db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getUid()).set(usersMap)
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