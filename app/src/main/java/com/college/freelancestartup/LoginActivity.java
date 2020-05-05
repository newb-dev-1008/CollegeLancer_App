package com.college.freelancestartup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button signUpBtn, signInBtn, signInFBBtn, signInGoogleBtn;
    private EditText signInEmailET, signInPwET;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        signUpBtn = findViewById(R.id.welcomeSignUpButton);
        signInBtn = findViewById(R.id.welcomeSignInButton);
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
            }
        });
    }

    public void signUp(View view){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void Login(View view){
        String emailID = signInEmailET.getText().toString().trim();
        String password = signInPwET.getText().toString().trim();
    }
}