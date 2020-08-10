package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ReportBugFragment extends Fragment {

    private EditText bugLocationET, bugDescriptionET;
    private RadioGroup bugFrequencyRadioGroup, bugStallRadioGroup;
    private View root;
    private MaterialButton addScreenshots, reportBugButton;
    private String bugFreq, bugStalled, reportTime;
    private int flag;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private Calendar c;
    private RecyclerView recyclerView;
    private ImageView idImage, deleteImage;
    private ArrayList<BugReport> bugReport;
    private ArrayList<URL> bugScreenshotURLs;
    private ArrayList<Bitmap> bugPictures;
    private RecyclerView.Adapter bugReportAdapter;
    private RecyclerView.LayoutManager bugReportLayoutManager;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.report_bug_frag, container, false);
        bugLocationET = root.findViewById(R.id.bugLocationEditText);
        bugDescriptionET = root.findViewById(R.id.bugDescriptionET);
        flag = 0;

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        recyclerView = root.findViewById(R.id.encloseSSRecyclerView);
        bugFrequencyRadioGroup = root.findViewById(R.id.bugFrequencyRadioGroup);
        deleteImage = root.findViewById(R.id.bug_remove_ss);

        bugReport = new ArrayList<>();
        bugPictures = new ArrayList<>();
        bugScreenshotURLs = new ArrayList<>();

        bugStallRadioGroup = root.findViewById(R.id.bugStallRadioGroup);
        c = Calendar.getInstance();
        reportTime = c.getTime().toString();

        addScreenshots = root.findViewById(R.id.addSSButton);
        reportBugButton = root.findViewById(R.id.reportBugButton);

        bugStallRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedButton = radioGroup.findViewById(i);
                String bugStalled1 = checkedButton.getText().toString();
                if (bugStalled1.equals("Yes, and I want to lodge a complaint and retrieve my lost work as soon as possible.")) {
                    String s = "Report Bug and lodge complaint";
                    flag = 1;
                    bugStalled = bugStalled1;
                    reportBugButton.setText(s);
                } else {
                    String s1 = "Report Bug";
                    bugStalled = bugStalled1;
                    reportBugButton.setText(s1);
                }
            }
        });

        bugFrequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedButton1 = radioGroup.findViewById(i);
                bugFreq = checkedButton1.getText().toString();
            }
        });

        reportBugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(bugLocationET.getText().toString()) || TextUtils.isEmpty(bugDescriptionET.getText().toString()) || TextUtils.isEmpty(bugStalled) || TextUtils.isEmpty(bugFreq)) {
                    Toast.makeText(getContext(), "Please enter all the details.", Toast.LENGTH_SHORT).show();
                } else {
                    reportBug();
                }
            }
        });

        addScreenshots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 0);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                                bugPictures.add(bitmap);
                                populateRecyclerView(bitmap, picturePath);
                                cursor.close();

                            }
                        }
                    }
                    break;
            }
        }
    }

    private void populateRecyclerView(Bitmap bitmapImage, String path) {
        bugReport.add(new BugReport(bitmapImage, path));

        recyclerView.setVisibility(View.VISIBLE);

        bugReportLayoutManager = new LinearLayoutManager(getContext());
        bugReportAdapter = new BugReportAdapter(bugReport);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(bugReportLayoutManager);
        recyclerView.setAdapter(bugReportAdapter);
    }

    private void reportBug() {
        if (flag == 1) {
            androidx.appcompat.app.AlertDialog reportBugDialog = new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Submit bug report?")
                    .setMessage("Thank you for contributing towards making the app bug-free. We will submit your bug report and " +
                            "redirect you to our complaints section.\n" +
                            "Please try to be as detailed as possible about the unsaved progress you have lost.\n" +
                            "While we assure you that we will do everything we can to help retrieve your lost data, we cannot fully guarantee " +
                            "it's existence on our servers. Either way, we will get back to you, over e-mail.")
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Map<String, Object> bugReportMap = new HashMap<>();
                            bugReportMap.put("bugLocation", bugLocationET.getText().toString());
                            bugReportMap.put("bugDescription", bugDescriptionET.getText().toString());
                            bugReportMap.put("bugFrequency", bugFreq);
                            bugReportMap.put("bugStalledWork", bugStalled);
                            // bugReportMap.put("bugPictures", bugPictures);
                            bugReportMap.put("bugReporterEmail", firebaseAuth.getCurrentUser().getEmail());
                            bugReportMap.put("reportTime", reportTime);
                            bugReportMap.put("resolved", "No");
                            db.collection("BugReports").document(reportTime).set(bugReportMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Your bug report has been submitted. Please lodge your complaint with as much detail as possible.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getContext(), StudentMainActivity.class);
                                    intent.putExtra("Go_to_fragment_NewComplaint", "New Complaint");
                                    startActivity(intent);
                                }
                            });

                            Integer x = 1;
                            for (Bitmap imgUpload : bugPictures) {
                                StorageReference imgStorage = storageReference.child(reportTime).child(x.toString());
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                imgUpload.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();

                                UploadTask uploadTask = imgStorage.putBytes(data);
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        try {
                                            bugScreenshotURLs.add(new URL(imgStorage.getDownloadUrl().toString()));
                                            Toast.makeText(getContext(), "Your photos have been uploaded.", Toast.LENGTH_SHORT).show();
                                        } catch (MalformedURLException e) {
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();;
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                x++;
                                // Add path of all images to firestore as well
                            }
                        }
                    }).setNegativeButton("Cancel", null)
                    .create();
            reportBugDialog.show();
        } else {
            AlertDialog reportBugDialog = new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Submit bug report?")
                    .setMessage("Thank you for contributing towards making the app bug-free. We will submit your bug report and get " +
                            "back to you about it over e-mail.")
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Map<String, Object> bugReportMap = new HashMap<>();
                            bugReportMap.put("bugLocation", bugLocationET.getText().toString());
                            bugReportMap.put("bugDescription", bugDescriptionET.getText().toString());
                            bugReportMap.put("bugFrequency", bugFreq);
                            bugReportMap.put("bugStalledWork", bugStalled);
                            bugReportMap.put("bugReporterEmail", firebaseAuth.getCurrentUser().getEmail());
                            bugReportMap.put("bugPictures", bugPictures);
                            bugReportMap.put("reportTime", reportTime);
                            bugReportMap.put("resolved", "No");
                            db.collection("BugReports").document(reportTime).set(bugReportMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Your bug report has been submitted!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getContext(), StudentMainActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }).setNegativeButton("Cancel", null)
                    .create();
            reportBugDialog.show();
        }
    }

}
