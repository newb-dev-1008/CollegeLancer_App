package com.college.freelancestartup;

import android.content.Context;
import android.content.Intent;
import android.content.LocusId;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity {

    private Button signUpBtn, signInBtn, enterEmailCheck;
    private SignInButton signInGoogleBtn;
    private LoginButton signInFBBtn;
    private EditText signInEmailET, signInPwET;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private LoginManager fbLoginManager;
    private AccessTokenTracker accessTokenTracker;
    private String EmailID;
    private final String KEY_NAME = "name";
    private final String KEY_EMAIL = "emailID";
    private int RC_SIGN_IN = 1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String UIDEmailID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);

        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        firebaseAuth = FirebaseAuth.getInstance();
        fbLoginManager = LoginManager.getInstance();
        signUpBtn = findViewById(R.id.welcomeSignUpButton);
        signInBtn = findViewById(R.id.welcomeSignInButton);
        enterEmailCheck = findViewById(R.id.welcomeEnterEmailButton);
        signInFBBtn = findViewById(R.id.welcomeSignInFBButton);
        signInGoogleBtn = findViewById(R.id.welcomeSignInGoogleButton);

        signInEmailET = findViewById(R.id.welcomeEmailEditText);
        signInPwET = findViewById(R.id.welcomePasswordEditText);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(v);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login(view);

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(signInBtn.getWindowToken(), 0);
            }
        });

        enterEmailCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(enterEmailCheck.getWindowToken(), 0);
                EmailID = signInEmailET.getText().toString().trim();
                UIDEmailID = EmailID;
                if (isEmailValid(EmailID) == true){
                    firebaseAuth.fetchSignInMethodsForEmail(EmailID).addOnCompleteListener(signInMethodsTask -> {
                        if (signInMethodsTask.isSuccessful()) {
                            List<String> signInMethods = signInMethodsTask.getResult().getSignInMethods();
                            if (signInMethods.size() != 0) {
                                for (String signInMethod : signInMethods) {
                                    switch (signInMethod) {
                                        case GoogleAuthProvider.PROVIDER_ID:
                                            signInGoogleBtn.setVisibility(VISIBLE);
                                            signInFBBtn.setVisibility(GONE);
                                            signUpBtn.setVisibility(GONE);
                                            signInBtn.setVisibility(GONE);
                                            enterEmailCheck.setVisibility(GONE);
                                            signInEmailET.setVisibility(GONE);
                                            Toast.makeText(LoginActivity.this, "Your account was linked with Google. Sign In with Google.", Toast.LENGTH_LONG).show();
                                            break;
                                        case FacebookAuthProvider.PROVIDER_ID:
                                            enterEmailCheck.setVisibility(GONE);
                                            signInEmailET.setVisibility(GONE);
                                            signInGoogleBtn.setVisibility(GONE);
                                            signUpBtn.setVisibility(GONE);
                                            signInFBBtn.setVisibility(VISIBLE);
                                            signInBtn.setVisibility(GONE);
                                            Toast.makeText(LoginActivity.this, "Your account was linked with Facebook. Sign In with Facebook.", Toast.LENGTH_LONG).show();
                                            break;
                                        case EmailAuthProvider.PROVIDER_ID:
                                            signUpBtn.setVisibility(GONE);
                                            enterEmailCheck.setVisibility(GONE);
                                            signInGoogleBtn.setVisibility(GONE);
                                            signInFBBtn.setVisibility(GONE);
                                            signInBtn.setVisibility(VISIBLE);
                                            signInEmailET.setVisibility(GONE);
                                            signInPwET.setVisibility(VISIBLE);
                                            Toast.makeText(LoginActivity.this, "Enter your password.", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "You haven't registered with us. Sign up now!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else{
                    signInEmailET.setError("Please enter a valid Email ID.");
                }
            }
        });

        signInFBBtn.setPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signInGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignInClient.signOut();                 // To make the dialog box for selecting Google account pop up everytime
                Intent googleSignInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(googleSignInIntent, RC_SIGN_IN);
                Toast.makeText(LoginActivity.this, "Select your Google account.", Toast.LENGTH_SHORT).show();;
            }
        });

        signInFBBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Cancelled login. (Facebook)", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Failed to connect to Facebook. Try again.", Toast.LENGTH_SHORT).show();
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
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()){
                handleSignInResult(task);
            }
            else{
                Toast.makeText(LoginActivity.this, "Cancelled Google Login.", Toast.LENGTH_SHORT).show();
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
        }
        catch (ApiException e){
            Toast.makeText(this, "Couldn't sign in using Google. Try again.", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(final GoogleSignInAccount acct){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential).addOnSuccessListener(LoginActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Signed In. (Google)", Toast.LENGTH_SHORT).show();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String nameGoogle = acct.getDisplayName();
                String emailIDGoogle = acct.getEmail();
                UIDEmailID = emailIDGoogle;
                updateUI(user);
            }
        }).addOnFailureListener(LoginActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        });
    }

    private void handleFacebookToken(AccessToken token){
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(LoginActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Toast.makeText(LoginActivity.this, "Logged in via Facebook.", Toast.LENGTH_SHORT).show();
                String emailIDFB = token.getUserId();
                UIDEmailID = emailIDFB;
                updateUI(user);
            }
        }).addOnFailureListener(LoginActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void updateUI(FirebaseUser user){
        // Update UI after login
        if (user != null) {
            db.collection("Users").document("Users" + UIDEmailID).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.get("phoneNumber") != null) {
                                Toast.makeText(LoginActivity.this, "Welcome.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, WelcomeTestScreen.class);
                                finish();
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "We need some additional details before we go ahead.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, GFBDetailsActivity.class);
                                finish();
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            //} else {
                            //    Toast.makeText(SignUpActivity.this, "User doesn't seem to have logged in before.", Toast.LENGTH_SHORT).show();
                            // }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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
        // FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void signUp(View view){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void Login(View view){
        String emailID = signInEmailET.getText().toString().trim();
        UIDEmailID = emailID;
        String password = signInPwET.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(emailID, password)
                .addOnSuccessListener(LoginActivity.this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        updateUI(firebaseAuth.getCurrentUser());
                    }
                })
                .addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Function to check if entered Email IDs are valid
    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}