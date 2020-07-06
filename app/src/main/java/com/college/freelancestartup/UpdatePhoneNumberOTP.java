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

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

class UpdatePhoneNumberOTP extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number_verification);

        OtpTextView phoneNumberOTP = findViewById(R.id.phoneVerificationOTPView);
        TextView phoneNumberOTPTextView = findViewById(R.id.phoneVerificationTV);
        MaterialButton phoneVerificationOTPButton = findViewById(R.id.phoneVerificationOTPButton);
        ProgressBar phoneVerificationProgressBar;

        String phoneNumber = getIntent().getExtras().getString("phoneNo");
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

    @Override
    public void onOTPComplete(String OTP) {
        // fired when user has entered the OTP fully.
    }
}
