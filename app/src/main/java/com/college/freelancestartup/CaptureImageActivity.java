package com.college.freelancestartup;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.hardware.Camera.Parameters.FLASH_MODE_ON;

class CaptureImageActivity extends AppCompatActivity {

    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private Executor executor = Executors.newSingleThreadExecutor();

    private PreviewView cameraView;
    private ImageView captureButton, flash_on, flash_off, flash_auto;
    private String flashMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraView = findViewById(R.id.camera);
        captureButton = findViewById(R.id.captureImg);
        flash_on = findViewById(R.id.flash_on);
        flash_off = findViewById(R.id.flash_off);
        flash_auto = findViewById(R.id.flash_auto);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        flash_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flash_off.setVisibility(View.GONE);
                flash_on.setVisibility(View.VISIBLE);

                Toast.makeText(CaptureImageActivity.this, "Flash: ON", Toast.LENGTH_SHORT).show();
                setFlash(1);
            }
        });

        flash_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flash_on.setVisibility(View.GONE);
                flash_auto.setVisibility(View.VISIBLE);

                Toast.makeText(CaptureImageActivity.this, "Flash: AUTO", Toast.LENGTH_SHORT).show();
                setFlash(2);
            }
        });

        flash_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flash_auto.setVisibility(View.GONE);
                flash_off.setVisibility(View.VISIBLE);

                Toast.makeText(CaptureImageActivity.this, "Flash: OFF", Toast.LENGTH_SHORT).show();
                setFlash(3);
            }
        });
    }

    private String setFlash(int flashFlag){
        switch (flashFlag){
            case 1:
                flashMode = "FLASH_MODE_ON";
                break;
            case 2:
                flashMode = "FLASH_MODE_AUTO";
                break;
            case 3:
                flashMode = "FLASH_MODE_OFF";
                break;
            default:
                flashMode = "FLASH_MODE_OFF";
                break;
        }
        return flashMode;
    }

    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(CaptureImageActivity.this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }

    private void startCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                    Toast.makeText(CaptureImageActivity.this, "Can't find a camera for preview.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CaptureImageActivity.this, StudentUserProfile.class);
                    finish();
                    startActivity(intent);
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        // Query if extension is available (optional).
        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        final ImageCapture imageCapture = builder
                .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                .build();
        preview.setSurfaceProvider(cameraView.createSurfaceProvider());
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis, imageCapture);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                String filePath = getBatchDirectoryName() + '/' + mDateFormat.format(new Date())+ ".jpg";
                File file = new File(filePath);

                ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
                imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback () {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CaptureImageActivity.this, "Image saved successfully.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CaptureImageActivity.this, StudentUserProfile.class);
                                intent.putExtra("imageFilePath", filePath);
                                finish();
                                startActivity(intent);
                            }
                        });
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException error) {
                        error.printStackTrace();
                        finish();
                    }
                });
            }
        });
    }

    public String getBatchDirectoryName() {
        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {
            Toast.makeText(this, "Unable to save photo. Please report a bug in this regard.", Toast.LENGTH_LONG).show();
            finish();
        }

        return app_folder_path;
    }
}
