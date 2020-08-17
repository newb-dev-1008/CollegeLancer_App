package com.college.freelancestartup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StudentAddProjectForCollab extends AppCompatActivity {

    private EditText projectTitle, projectOpenFor, projectDesc;
    private AutoCompleteTextView projectSkills;
    private String posterTitle, posterEmail, postDate, projectID, projectStatus;
    private Calendar calendar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private int numberPicked, numberApps, endVotes;
    private MaterialButton addExistingButton, addNewButton;
    private ChipGroup chipGroup;
    private String[] allSkillsArray;
    private ArrayList<String> skillset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_addnew_project_forcollab);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        projectTitle = findViewById(R.id.projectTitleEditText);
        projectOpenFor = findViewById(R.id.projectOpenForEditText);
        projectDesc = findViewById(R.id.projDescEditText);
        projectSkills = findViewById(R.id.proj_Skills);
        addExistingButton = findViewById(R.id.addExistingProjectButton);
        addNewButton = findViewById(R.id.addNewProjectButton);
        chipGroup = findViewById(R.id.addProjSkillsChipGroup);
        numberApps = 0;
        numberPicked = 0;
        endVotes = 0;
        calendar = Calendar.getInstance();

        skillset = new ArrayList<>();
        allSkillsArray = getResources().getStringArray(R.array.skills);

        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                posterTitle = documentSnapshot.get("name").toString();
                posterEmail = documentSnapshot.get("email").toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentAddProjectForCollab.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> skillsAdapter = new ArrayAdapter<String>(StudentAddProjectForCollab.this, android.R.layout.simple_dropdown_item_1line, allSkillsArray);

        projectSkills.setAdapter(skillsAdapter);
        projectSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectSkills.showDropDown();
            }
        });

        projectSkills.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                projectSkills.showDropDown();
                return false;
            }
        });

        projectSkills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                projectSkills.setText(null);
                String skillName = adapterView.getItemAtPosition(i).toString();
                addSkills(skillName);
            }
        });

        projectSkills.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String skillName = projectSkills.getText().toString();
                    projectSkills.setText(null);
                    addSkills(skillName);
                    return true;
                } else {
                    return false;
                }
            }
        });


        projectSkills.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = String.valueOf(charSequence);
                if (s.equals(",") || s.equals(" ")) {
                    String skillName = projectSkills.getText().subSequence(0, (projectSkills.getText().length()-1)).toString();
                    addSkills(skillName);
                    projectSkills.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do nothing
            }
        });

        addExistingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // finish this
            }
        });

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (projectTitle.getText().toString().equals("") || projectOpenFor.getText().toString().equals("") || projectDesc.getText().toString().equals("") || (skillset.size() == 0)) {
                    Toast.makeText(StudentAddProjectForCollab.this, "Please enter all the above details.", Toast.LENGTH_SHORT).show();
                } else {
                    addNewProjectFunction();
                }
            }
        });

    }

    private void addSkills(String name) {
        if (!name.isEmpty() && !skillset.contains(name)) {
            addChipToGroup(name, chipGroup, skillset);
            skillset.add(name);
        } else {
            Toast.makeText(this, "Enter a valid skill/ a skill you haven't already entered.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addChipToGroup(String name1, ChipGroup chipGroup1, ArrayList<String> arrayList) {
        Chip chip = new Chip(this);
        chip.setText(name1);
        chip.setClickable(false);
        chip.setCheckable(false);
        chip.setCloseIconVisible(true);

        chipGroup1.addView(chip);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroup1.removeView(chip);
                arrayList.remove(name1);
            }
        });
    }

    private void addNewProjectFunction() {
        AlertDialog addProjectDialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Add project?")
                .setMessage("Your project will be out in the open for developers to see and apply for collaboration.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkProjectIDValidFunction();
                        // addNewProjectRealFunction();
                    }
                }).setNegativeButton("Cancel", null)
                .create();
        addProjectDialog.show();
    }

    private void checkProjectIDValidFunction() {
        postDate = calendar.getTime().toString();
        projectStatus = "Open";
        String projeccID = generateProjectID();
        db.collection("CollabProjects").document(projeccID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            checkProjectIDValidFunction();
                        } else {
                            projectID = projeccID;
                            addNewProjectRealFunction();
                            // Toast.makeText(StudentAddProjectForCollab.this, projectID, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentAddProjectForCollab.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewProjectRealFunction() {
        Map<String, Object> addProjectMap = new HashMap<>();
        addProjectMap.put("numberApps", numberApps);
        addProjectMap.put("numberPicked", numberPicked);
        addProjectMap.put("endVotes", endVotes);
        addProjectMap.put("projectID", projectID);
        addProjectMap.put("posterTitle", posterTitle);
        addProjectMap.put("posterEmail", posterEmail);
        addProjectMap.put("postDate", postDate);
        addProjectMap.put("projectOpenFor", projectOpenFor.getText().toString());
        addProjectMap.put("projectSkills", skillset);
        addProjectMap.put("projectDesc", projectDesc.getText().toString());
        addProjectMap.put("projectTitle", projectTitle.getText().toString());
        addProjectMap.put("projectStatus", projectStatus);

        db.collection("CollabProjects").document(projectID).set(addProjectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                        .collection("Projects").document(projectID).set(addProjectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("Users").document("User " + firebaseAuth.getCurrentUser().getEmail())
                                .collection("MyCollabs").document(projectID).set(addProjectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(StudentAddProjectForCollab.this, "Your project has been added. Expect some calls and applications!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(StudentAddProjectForCollab.this, StudentMainActivity.class);
                                intent.putExtra("Go_to_fragment_addProj", "Go to fragment addProj");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }).addOnFailureListener()
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentAddProjectForCollab.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentAddProjectForCollab.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String generateProjectID() {
        StringBuilder sb = new StringBuilder(30);
        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new Random();
        for (int i = 0; i < 30; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }

}
