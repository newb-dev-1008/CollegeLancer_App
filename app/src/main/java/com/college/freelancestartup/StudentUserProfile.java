package com.college.freelancestartup;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.college.freelancestartup.GFBDetailsActivity.getCalendar;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;


public class StudentUserProfile extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText nameET, phoneNumberET, departmentET, semesterET, emailET, DOBET, universityET, bioET;
    private int flagApplyChangesPressed;
    private MaterialButton applyChanges, cancelChanges;
    private Toolbar studentProfileToolbar;
    private ImageView idImage, editProf, editName, editPhoneNumber, editDepartment, editSemester, editEmail, editDOB, editUniversity, editBio;
    private String KEY_BIO = "userBio";
    private String dbName, dbPhoneNumber, dbDepartment, dbSemester, dbEmail, dbDOB, dbUniversity, dbBio, DOB;
    private ImageView[] editImages;
    private Date DOBDate, currentDate;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_user_profile_settings);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        nameET = findViewById(R.id.student_profileName);
        phoneNumberET = findViewById(R.id.student_profilePhNo);
        departmentET = findViewById(R.id.student_profileDepartment);
        semesterET = findViewById(R.id.student_profileSemester);
        emailET = findViewById(R.id.student_profileEmail);
        DOBET = findViewById(R.id.student_profileDOB);
        universityET = findViewById(R.id.student_profileUniversity);
        bioET = findViewById(R.id.student_profileBio);
        // idImage = findViewById(R.id.idVerification);

        applyChanges = findViewById(R.id.student_apply_changes);
        cancelChanges = findViewById(R.id.student_cancel_changes);

        editProf = findViewById(R.id.editProfile);

        editName = findViewById(R.id.editName);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editDepartment = findViewById(R.id.editDepartment);
        editSemester = findViewById(R.id.editSemester);
        editEmail = findViewById(R.id.editEmail);
        editDOB = findViewById(R.id.editDOB);
        editUniversity = findViewById(R.id.editUniversity);
        editBio = findViewById(R.id.editBio);

        currentDate = Calendar.getInstance().getTime();

        editImages = new ImageView[]{editName, editPhoneNumber, editDepartment, editSemester, editEmail, editDOB, editUniversity, editBio};

        studentProfileToolbar = findViewById(R.id.student_profileToolbar);
        setSupportActionBar(studentProfileToolbar);

        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        dbName = documentSnapshot.get("name").toString();
                        dbPhoneNumber = documentSnapshot.get("phoneNumber").toString();
                        dbDepartment = documentSnapshot.get("department").toString();
                        dbSemester = documentSnapshot.get("studentSemester").toString();
                        dbEmail = documentSnapshot.get("email").toString();
                        dbDOB = documentSnapshot.get("dateOfBirth").toString();
                        dbUniversity = documentSnapshot.get("university").toString();
                        dbBio = documentSnapshot.get("userBio").toString();

                        nameET.setText(dbName);
                        phoneNumberET.setText(dbPhoneNumber);
                        departmentET.setText(dbDepartment);
                        semesterET.setText(dbSemester);
                        emailET.setText(dbEmail);
                        DOBET.setText(dbDOB);
                        universityET.setText(dbUniversity);
                        bioET.setText(dbBio);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentUserProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                applyChanges();
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
                                verifyID(bitmap);
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
            allowEdit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void allowEdit(){
        editProf.setVisibility(View.GONE);
        editName.setVisibility(View.VISIBLE);
        editPhoneNumber.setVisibility(View.VISIBLE);
        editDepartment.setVisibility(View.VISIBLE);
        editSemester.setVisibility(View.VISIBLE);
        editEmail.setVisibility(View.VISIBLE);
        editDOB.setVisibility(View.VISIBLE);
        editUniversity.setVisibility(View.VISIBLE);
        editBio.setVisibility(View.VISIBLE);

        selectEditObject();
    }

    private void applyChanges(){
        flagApplyChangesPressed = 1;
        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Add if statements to first check if the inputs are invalid, and then nest the below ifs within
                        // the else (when inputs are valid), also check if the user entered the same values again
                        if (!dbName.equals(nameET.getText().toString()) ||
                                !dbDepartment.equals(departmentET.getText().toString()) ||
                                !dbUniversity.equals(universityET.getText().toString())){
                            // change the field in Firestore after confirmation with ID
                            selectImageInputMode();
                        } else if (!dbSemester.equals(semesterET.getText().toString())) {
                            // change the field in Firestore

                        } else if (!dbDOB.equals(DOBET.getText().toString())) {
                            if (((checkDOBValidity(currentDate, DOBDate) > 0) && (checkDOBValidity(currentDate, DOBDate) < 18))){
                                // This is proper
                                selectImageInputMode();
                            } else if (checkDOBValidity(currentDate, DOBDate) <= 0) {
                                Toast.makeText(StudentUserProfile.this, "Please select a valid date of birth. You're too young to be able to even use this app.", Toast.LENGTH_LONG).show();
                                DOBET.setError("Please set your actual date of birth");
                                DOBET.setText(dbDOB);
                            } else if (checkDOBValidity(currentDate, DOBDate) >= 27) {
                                Toast.makeText(StudentUserProfile.this, "", Toast.LENGTH_SHORT).show();
                                DOBET.setText(dbDOB);
                            }
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
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            phoneNumberET.setText(dbPhoneNumber);
                                        }
                                    })
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

    private void selectEditObject(){
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideOtherEdits(editName);
                setSelectedEditable(editName);
            }
        });
        editBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideOtherEdits(editBio);
                setSelectedEditable(editBio);
            }
        });
        editUniversity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideOtherEdits(editUniversity);
                setSelectedEditable(editUniversity);
            }
        });
        editSemester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideOtherEdits(editSemester);
                setSelectedEditable(editSemester);
            }
        });
        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideOtherEdits(editDOB);
                setSelectedEditable(editDOB);
            }
        });
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideOtherEdits(editEmail);
                setSelectedEditable(editEmail);
            }
        });
        editDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideOtherEdits(editDepartment);
                setSelectedEditable(editDepartment);
            }
        });
        editPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideOtherEdits(editPhoneNumber);
                setSelectedEditable(editPhoneNumber);
            }
        });
    }

    private void hideOtherEdits(ImageView imageViewInstance){
        for (ImageView editImage : editImages) {
            if (!editImage.equals(imageViewInstance)) {
                editImage.setVisibility(View.GONE);
            }
        }
    }

    private void setSelectedEditable(ImageView imageViewObject){
        if (imageViewObject.equals(editName)){
            nameET.setFocusable(true);
            nameET.setClickable(true);
            nameET.setText("");
            nameET.setHint("Enter your new name");
        } else if (imageViewObject.equals(editPhoneNumber)) {
            phoneNumberET.setFocusable(true);
            phoneNumberET.setClickable(true);
            phoneNumberET.setText("");
            phoneNumberET.setHint("Enter your new 10 digit phone number");
        } else if (imageViewObject.equals(editSemester)) {
            semesterET.setFocusable(true);
            semesterET.setClickable(true);
            semesterET.setText("");
            semesterET.setHint("Enter your current semester");
        } else if (imageViewObject.equals(editDepartment)) {
            departmentET.setFocusable(true);
            departmentET.setClickable(true);
            departmentET.setText("");
            departmentET.setHint("Enter your current Department");
        } else if (imageViewObject.equals(editDOB)) {
            DOBET.setFocusable(true);
            DOBET.setClickable(true);
            DOBET.setText("");
            DOBET.setHint("Enter your Date of Birth");
            DOBET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment datePicker = new DatePickerFragment();
                    datePicker.show(getSupportFragmentManager(), "DatePicker");
                }
            });
        } else if (imageViewObject.equals(editEmail)) {
            emailET.setFocusable(true);
            emailET.setClickable(true);
            emailET.setText("");
            emailET.setHint("Enter your new Email ID");
        } else if (imageViewObject.equals(editUniversity)) {
            universityET.setFocusable(true);
            universityET.setClickable(true);
            universityET.setText("");
            universityET.setHint("Enter your University Name");
        } else if (imageViewObject.equals(editBio)) {
            bioET.setFocusable(true);
            bioET.setClickable(true);
            bioET.setText("");
            bioET.setEnabled(true);
            bioET.setHint("Enter a short bio");
        }
        applyChanges.setVisibility(View.VISIBLE);
        cancelChanges.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(YEAR, year);
        c.set(MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        DOBDate = c.getTime();
        DOB = DateFormat.getDateInstance(DateFormat.FULL).format(DOBDate);
        DOBET.setText(DOB);
    }

    private int checkDOBValidity(Date cDate, Date bDate){
        Calendar a = getCalendar(bDate);
        Calendar b = getCalendar(cDate);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(Calendar.DAY_OF_MONTH) > b.get(Calendar.DAY_OF_MONTH))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTime(date);
        return cal;
    }

    private void selectImageInputMode(){
        /*final CharSequence[] options = {"Take a Photo", "Choose from Gallery", "Cancel"};
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
        confirmEditWithID.show();*/

        AlertDialog options = new MaterialAlertDialogBuilder(this)
                .setTitle("Please upload an ID Proof for verification.")
                .setMessage("You seem to have edited some of your essential background details (Name, Department, Date of Birth or University). Please upload a valid ID proof for verification.")
                .setPositiveButton("Click Picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent takePicture = new Intent(StudentUserProfile.this, CaptureImageActivity.class);
                        startActivity(takePicture);
                    }
                })
                .setNegativeButton("Choose from Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 0);
                    }
                })
                .create();
        options.show();
        options.setCanceledOnTouchOutside(false);
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
                                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                test_text(visionText);      // To be deleted (and replace DialogInterface.OnClickListener with null)
                                            }
                                        })
                                        .create();
                                textRegistered.show();
                                textRegistered.setCanceledOnTouchOutside(false);
                                textRegistered.setCancelable(false);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        AlertDialog textRegisteredFailed = new MaterialAlertDialogBuilder(StudentUserProfile.this)
                                                .setTitle("Failed to register your ID")
                                                .setMessage(e.getMessage() + "\nPlease retry.")
                                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        selectImageInputMode();
                                                    }
                                                })
                                                .setNegativeButton("Cancel Verification", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Toast.makeText(StudentUserProfile.this, "Cancelled Verification.", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .create();
                                        textRegisteredFailed.show();
                                        textRegisteredFailed.setCancelable(false);
                                        textRegisteredFailed.setCanceledOnTouchOutside(false);
                                    }
                                });
    }

    // To be deleted
    private void test_text(Text textTask){
        String textResult = textTask.getText();
        Intent intent = new Intent(StudentUserProfile.this, IDVerificationActivity.class);
        intent.putExtra("test_text", textResult);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (flagApplyChangesPressed == 1){
            nameET.setText(dbName);
            phoneNumberET.setText(dbPhoneNumber);
            departmentET.setText(dbDepartment);
            semesterET.setText(dbSemester);
            emailET.setText(dbEmail);
            DOBET.setText(dbDOB);
            universityET.setText(dbUniversity);
            bioET.setText(dbBio);
        }
    }
}
