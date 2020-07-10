package com.college.freelancestartup;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


class IDVerificationActivity extends AppCompatActivity {

    TextView testTextview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_text_recognition);
        testTextview = findViewById(R.id.text_test);

        String testingText = getIntent().getExtras().getString("test_text");
        testTextview.setText(testingText);
    }
}
