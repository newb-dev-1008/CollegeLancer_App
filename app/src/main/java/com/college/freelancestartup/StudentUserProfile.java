package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.util.Map;

import bolts.Capture;

class StudentUserProfile extends AppCompatActivity {

    private EditText nameET, phoneNumberET, departmentET, semesterET, emailET, DOBET, universityET, bioET;
    private MaterialButton applyChanges, cancelChanges;
    private Toolbar studentProfileToolbar;
    private ImageView idImage;
    private String KEY_BIO = "userBio";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_user_profile_settings);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        nameET = findViewById(R.id.student_profileName);
        phoneNumberET = findViewById(R.id.student_profilePhNo);
        departmentET = findViewById(R.id.student_profileDepartment);
        semesterET = findViewById(R.id.student_profileSemester);
        emailET = findViewById(R.id.student_profileEmail);
        DOBET = findViewById(R.id.student_profileDOB);
        universityET = findViewById(R.id.student_profileUniversity);
        bioET = findViewById(R.id.student_profileBio);
        idImage = findViewById(R.id.idVerification);

        applyChanges = findViewById(R.id.student_apply_changes);
        cancelChanges = findViewById(R.id.student_cancel_changes);

        studentProfileToolbar = findViewById(R.id.student_profileToolbar);
        setSupportActionBar(studentProfileToolbar);

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String dbName = documentSnapshot.get("name").toString();
                                String dbPhoneNumber = documentSnapshot.get("phoneNumber").toString();
                                String dbDepartment = documentSnapshot.get("department").toString();
                                String dbSemester = documentSnapshot.get("studentSemester").toString();
                                String dbEmail = documentSnapshot.get("emailID").toString();
                                String dbDOB = documentSnapshot.get("dateOfBirth").toString();
                                String dbUniversity = documentSnapshot.get("university").toString();
                                String dbBio = documentSnapshot.get("userBio").toString();

                                // Add if statements to first check if the inputs are invalid, and then nest the below ifs within
                                // the else (when inputs are valid)

                                if (!dbName.equals(nameET.getText().toString()) ||
                                        !dbDepartment.equals(departmentET.getText().toString()) ||
                                        !dbDOB.equals(DOBET.getText().toString()) ||
                                        !dbUniversity.equals(universityET.getText().toString())){
                                    // change the field in Firestore after confirmation with ID
                                    final CharSequence[] options = {"Take a Photo", "Choose from Gallery", "Cancel"};
                                    MaterialAlertDialogBuilder confirmEditWithID = new MaterialAlertDialogBuilder(StudentUserProfile.this)
                                            .setTitle("Please upload an ID Proof for confirmation.")
                                            .setMessage("You seem to have edited some of your essential background details (Name, Department, Date of Birth or University). Please upload a valid ID proof for verification.")
                                            .setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    if (options[i].equals("Take a Photo")){
                                                        Intent takePicture = new Intent(StudentUserProfile.this, CaptureImageActivity.class);
                                                        startActivity(takePicture);
                                                    } else if (options[i].equals("Choose from Gallery")){
                                                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                        startActivityForResult(pickPhoto, 0);
                                                    } else if (options[i].equals("Cancel")){
                                                        dialogInterface.dismiss();
                                                    }
                                                }
                                            });
                                    confirmEditWithID.setCancelable(false);
                                    confirmEditWithID.show();

                                } else if (!dbSemester.equals(semesterET.getText().toString())) {
                                    // change the field in Firestore

                                } else if (!dbEmail.equals(emailET.getText().toString())) {
                                    // Do not change the login credentials. Inform that the email ID will be used only for communication purposes.
                                } else if (!dbBio.equals(bioET.getText().toString())) {
                                    // change the field in Firestore
                                    db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail()).update(
                                            KEY_BIO, bioET.getText().toString());
                                } else if (!dbPhoneNumber.equals(phoneNumberET.getText().toString())) {
                                    // change the field in Firestore after sending OTP
                                    AlertDialog confirmEditPhoneNo = new MaterialAlertDialogBuilder(StudentUserProfile.this)
                                            .setTitle("Confirm your phone number.")
                                            .setMessage("We'll send an OTP to your phone number for confirmation.")
                                            .setPositiveButton("Send OTP", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    // Add Firebase OTP Verification
                                                    Intent OTPVerificationIntent = new Intent(StudentUserProfile.this, UpdatePhoneNumberOTP.class);
                                                    OTPVerificationIntent.putExtra("phoneNo", phoneNumberET.getText());
                                                    startActivity(OTPVerificationIntent);
                                                }
                                            })
                                            .setNegativeButton("Cancel", null)
                                            .create();
                                    confirmEditPhoneNo.setCanceledOnTouchOutside(false);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentUserProfile.this, e.getMessage() + "Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cancelChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Write this stuff
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_user_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                idImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                                cursor.close();

                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.editProfile) {
            Toast.makeText(StudentUserProfile.this, "Edit your profile. You can now change the contents.", Toast.LENGTH_LONG).show();

            phoneNumberET.setFocusable(true);
            phoneNumberET.setFocusableInTouchMode(true);
            phoneNumberET.setClickable(true);
            phoneNumberET.setCursorVisible(true);

            departmentET.setFocusable(true);
            departmentET.setFocusableInTouchMode(true);
            departmentET.setClickable(true);
            departmentET.setCursorVisible(true);

            semesterET.setFocusable(true);
            semesterET.setFocusableInTouchMode(true);
            semesterET.setClickable(true);
            semesterET.setCursorVisible(true);

            emailET.setFocusable(true);
            emailET.setFocusableInTouchMode(true);
            emailET.setClickable(true);
            emailET.setCursorVisible(true);

            DOBET.setFocusable(true);
            DOBET.setFocusableInTouchMode(true);
            DOBET.setClickable(true);
            DOBET.setCursorVisible(true);

            universityET.setFocusable(true);
            universityET.setFocusableInTouchMode(true);
            universityET.setClickable(true);
            universityET.setCursorVisible(true);

            bioET.setFocusable(true);
            bioET.setFocusableInTouchMode(true);
            bioET.setClickable(true);
            bioET.setCursorVisible(true);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void verifyID(Bitmap bitmapID){
        TextRecognizer recognizer = TextRecognition.getClient();

        int rotationDegree = 0;
        InputImage image = InputImage.fromBitmap(bitmapID, rotationDegree);

        Task<Text> result =
                recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                AlertDialog textRegistered = new MaterialAlertDialogBuilder(StudentUserProfile.this)
                                        .setTitle("ID Registered")
                                        .setMessage("We have extracted the necessary information for verification from the provided ID card. " +
                                                "Your details shall be verified and, if found valid, shall be updated shortly." +
                                                "In case of data inconsistency, mismatch or an invalid ID, we will immediately notify you via e-mail.")
                                        .setPositiveButton("Okay", null)
                                        .create();
                                textRegistered.show();
                                textRegistered.setCanceledOnTouchOutside(false);
                                textRegistered.setCancelable(false);
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });
    }
}
