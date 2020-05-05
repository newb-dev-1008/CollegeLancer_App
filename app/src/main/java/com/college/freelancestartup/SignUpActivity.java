package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private Button signUp, signUpFB, signUpGoogle;
    private EditText signUpEmailET, signUpPwET, signUpConfPwET, signUpPhoneNoET;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        firebaseAuth = FirebaseAuth.getInstance();

        signUp = findViewById(R.id.signupButton);
        signUpEmailET = findViewById(R.id.signupEmailEditText);
        signUpPwET = findViewById(R.id.signupPasswordEditText);
        signUpPhoneNoET = findViewById(R.id.signupPhoneNoEditText);
        signUpConfPwET = findViewById(R.id.signupConfPasswordEditText);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        // Receiving Email and other stuff from EditTexts
        String emailID = signUpEmailET.getText().toString().trim();
        int phoneNo = Integer.parseInt(signUpPhoneNoET.getText().toString().trim());
        String password = signUpPwET.getText().toString().trim();
        String confPassword = signUpConfPwET.getText().toString().trim();

        // Checking if the fields are empty
        if (TextUtils.isEmpty(emailID)) {
            Toast.makeText(this, "You've not entered your Email ID.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Checking if Email ID entered is valid using function defined below
        else if (!isEmailValid(emailID)) {
            Toast.makeText(this, "Please enter a valid Email ID.",Toast.LENGTH_SHORT).show();
            return;
        }
        //Submit Details to Firebase and receive OTP
        else {
            // Creating a dialog box for confirmation
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Sure you're good to go?")
                    .setMessage("We'll be sending a verification code to your given e-mail and phone.")
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // Check if Internet connection is established
                            // --

                            Intent intent = new Intent(SignUpActivity.this, OTPActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Go Back", null)
                    .show();
        }
    }

    // Function to check if entered Email IDs are valid
    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}