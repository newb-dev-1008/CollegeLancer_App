package com.college.freelancestartup;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

class UpdatePhoneNumberOTP extends AppCompatActivity {

    String phoneNumber;
    Integer realPhoneNo;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number_verification);

        firebaseAuth = FirebaseAuth.getInstance();

        OtpTextView phoneNumberOTP = findViewById(R.id.phoneVerificationOTPView);
        TextView phoneNumberOTPTextView = findViewById(R.id.phoneVerificationTV);
        MaterialButton phoneVerificationOTPButton = findViewById(R.id.phoneVerificationOTPButton);
        ProgressBar phoneVerificationProgressBar;

        phoneNumber = getIntent().getExtras().getString("phoneNo");
        realPhoneNo = Integer.parseInt("+91" + phoneNumber);
        String displayPhoneNo = "+91 - " + phoneNumber;
        String textViewDisplay = "Enter the OTP sent to your phone number:\n" + displayPhoneNo;

        phoneNumberOTPTextView.setText(textViewDisplay);

        phoneNumberOTP.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                phoneNumberOTP.requestFocusOTP();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(phoneNumberOTP, 0);
            }

            @Override
            public void onOTPComplete(String otp) {
                phoneVerificationOTPButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void verifyPhoneNumber(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                realPhoneNo,
                60,
                TimeUnit.SECONDS;
                this,
                mCallbacks);
        );
    }
}
