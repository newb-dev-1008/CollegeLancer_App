package com.college.freelancestartup;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

class UpdatePhoneNumberOTP extends AppCompatActivity {

    private String phoneNumber;
    private Integer realPhoneNo;
    private FirebaseAuth firebaseAuth;
    private OtpTextView phoneNumberOTP;
    private TextView phoneNumberOTPTextView;
    private MaterialButton phoneVerificationOTPButton;
    private ProgressBar phoneVerificationProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number_verification);

        firebaseAuth = FirebaseAuth.getInstance();

        phoneNumberOTP = findViewById(R.id.phoneVerificationOTPView);
        phoneNumberOTPTextView = findViewById(R.id.phoneVerificationTV);
        phoneVerificationOTPButton = findViewById(R.id.phoneVerificationOTPButton);
        phoneVerificationProgressBar = findViewById(R.id.phoneVerificationProgressBar);

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

        verifyPhoneNumber();
    }

    private void verifyPhoneNumber(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                realPhoneNo.toString(),
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        String verificationID = s;
                        Toast.makeText(UpdatePhoneNumberOTP.this, "Your verification code has been sent.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {

                        AlertDialog retryOTP = new MaterialAlertDialogBuilder(UpdatePhoneNumberOTP.this)
                                .setTitle("Your code hasn't arrived yet.")
                                .setMessage("Your current request for a verification code timed out. Please check your Internet connection/ mobile network and retry.")
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        verifyPhoneNumber();
                                    }
                                })
                    }
                });
    }
}
