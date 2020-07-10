package com.college.freelancestartup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class UpdatePhoneNumberOTP extends AppCompatActivity {

    private String phoneNumber;
    private Integer realPhoneNo;
    private OtpTextView phoneNumberOTP;
    private TextView phoneNumberOTPTextView, waiting1, waiting2;
    private MaterialButton phoneVerificationOTPButton;
    private ProgressBar phoneVerificationProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number_verification);

        phoneNumberOTP = findViewById(R.id.phoneVerificationOTPView);
        phoneNumberOTPTextView = findViewById(R.id.phoneVerificationTV);
        phoneVerificationOTPButton = findViewById(R.id.phoneVerificationOTPButton);
        phoneVerificationProgressBar = findViewById(R.id.phoneVerificationProgressBar);
        waiting1 = findViewById(R.id.waitingSMSTV1);
        waiting2 = findViewById(R.id.waitingSMSTV2);

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
                        phoneVerificationProgressBar.setVisibility(View.GONE);
                        waiting1.setVisibility(View.GONE);
                        waiting2.setVisibility(View.GONE);
                        Toast.makeText(UpdatePhoneNumberOTP.this, "Your phone number has been verified and updated.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdatePhoneNumberOTP.this, StudentUserProfile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        phoneVerificationProgressBar.setVisibility(View.GONE);
                        waiting1.setVisibility(View.GONE);
                        waiting2.setVisibility(View.GONE);
                        AlertDialog retryOTP = new MaterialAlertDialogBuilder(UpdatePhoneNumberOTP.this)
                                .setTitle("Verification failed.")
                                .setMessage(e.getMessage() + " Would you like to retry?")
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        verifyPhoneNumber();
                                    }
                                })
                                .setNegativeButton("Cancel Verification", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(UpdatePhoneNumberOTP.this, StudentUserProfile.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .create();
                        retryOTP.setCanceledOnTouchOutside(false);
                        retryOTP.setCancelable(false);
                        retryOTP.show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // String verificationID = s;
                        Toast.makeText(UpdatePhoneNumberOTP.this, "Your verification code has been sent.", Toast.LENGTH_SHORT).show();
                        phoneVerificationProgressBar.setVisibility(View.VISIBLE);
                        waiting1.setVisibility(View.VISIBLE);
                        waiting2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                        phoneVerificationProgressBar.setVisibility(View.GONE);
                        waiting1.setVisibility(View.GONE);
                        waiting2.setVisibility(View.GONE);
                        AlertDialog retryOTP = new MaterialAlertDialogBuilder(UpdatePhoneNumberOTP.this)
                                .setTitle("Your code hasn't arrived yet.")
                                .setMessage("Your current request for a verification code timed out. Please check your Internet connection/ mobile network and retry.")
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        verifyPhoneNumber();
                                    }
                                })
                                .setNegativeButton("Cancel Verification", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(UpdatePhoneNumberOTP.this, StudentUserProfile.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .create();
                        retryOTP.setCanceledOnTouchOutside(false);
                        retryOTP.setCancelable(false);
                        retryOTP.show();
                    }
                });
    }
}
