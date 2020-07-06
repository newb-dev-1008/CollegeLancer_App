package com.college.freelancestartup;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import in.aabhasjindal.otptextview.OtpTextView;

class UpdatePhoneNumberOTP extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number_verification);

        OtpTextView phoneNumberOTP;
        TextView phoneNumberOTPTextView = findViewById(R.id.phoneVerificationTV);
        MaterialButton phoneVerificationOTPButton;
        ProgressBar phoneVerificationProgressBar;

        String phoneNumber = getIntent().getExtras().getString("phoneNo");

        phoneNumberOTPTextView.setText("Enter the OTP sent to your phone number: \n+91-" + phoneNumber);
    }
}
