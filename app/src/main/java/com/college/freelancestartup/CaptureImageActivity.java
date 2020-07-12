package com.college.freelancestartup;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CaptureImageActivity extends AppCompatActivity {

    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private Executor executor = Executors.newSingleThreadExecutor();

    private PreviewView cameraView;
    private ImageView captureButton, flash_on, flash_off, flash_auto, capturedImageView;
    private MaterialButton cancelCapture, submitCapture, retryCapture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.capture_image);
        cameraView = findViewById(R.id.camera);
        capturedImageView = findViewById(R.id.capturedImage);
        captureButton = findViewById(R.id.captureImg);
        flash_on = findViewById(R.id.flash_on);
        flash_off = findViewById(R.id.flash_off);
        flash_auto = findViewById(R.id.flash_auto);
        cancelCapture = findViewById(R.id.cancelCapturedVerification);
        retryCapture = findViewById(R.id.retryCapturedVerification);
        submitCapture = findViewById(R.id.submitCapturedVerification);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }


        cancelCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog confirmCancelVerification = new MaterialAlertDialogBuilder(CaptureImageActivity.this)
                        .setTitle("Cancel Verification")
                        .setMessage("Are you sure you want to cancel ID Verification?")
                        .setPositiveButton("Yes, Cancel Verification", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(CaptureImageActivity.this, StudentUserProfile.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No, continue with verification", null)
                        .create();
                confirmCancelVerification.setCanceledOnTouchOutside(false);
                confirmCancelVerification.show();
            }
        });

    }

/*
    private void setFlash(int flashFlag){
        switch (flashFlag){
            case 1:
                flashMode = 1;
                startCamera();
                break;
            case 0:
                flashMode = 0;
                startCamera();
                break;
            default:
                flashMode = 2;
                startCamera();
                break;
        }
    }
    */


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
                .setFlashMode(ImageCapture.FLASH_MODE_OFF)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build();
        preview.setSurfaceProvider(cameraView.createSurfaceProvider());
        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis, imageCapture);

        retryCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelCapture.setVisibility(View.GONE);
                retryCapture.setVisibility(View.GONE);
                submitCapture.setVisibility(View.GONE);
                capturedImageView.setVisibility(View.GONE);

                cameraView.setVisibility(View.VISIBLE);
                captureButton.setVisibility(View.VISIBLE);
                startCamera();
            }
        });

        flash_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flash_off.setVisibility(View.GONE);
                flash_on.setVisibility(View.VISIBLE);

                imageCapture.setFlashMode(ImageCapture.FLASH_MODE_ON);
                Toast.makeText(CaptureImageActivity.this, "Flash: ON", Toast.LENGTH_SHORT).show();
                // setFlash(1);
            }
        });

        flash_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flash_on.setVisibility(View.GONE);
                flash_auto.setVisibility(View.VISIBLE);

                imageCapture.setFlashMode(ImageCapture.FLASH_MODE_AUTO);
                Toast.makeText(CaptureImageActivity.this, "Flash: AUTO", Toast.LENGTH_SHORT).show();
                // setFlash(0);
            }
        });

        flash_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flash_auto.setVisibility(View.GONE);
                flash_off.setVisibility(View.VISIBLE);

                imageCapture.setFlashMode(ImageCapture.FLASH_MODE_OFF);
                Toast.makeText(CaptureImageActivity.this, "Flash: OFF", Toast.LENGTH_SHORT).show();
                // setFlash(2);
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCapture.takePicture(executor, new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                        // super.onCaptureSuccess(image);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cameraView.setVisibility(View.GONE);
                                captureButton.setVisibility(View.GONE);

                                capturedImageView.setVisibility(View.VISIBLE);
                                cancelCapture.setVisibility(View.VISIBLE);
                                retryCapture.setVisibility(View.VISIBLE);
                                submitCapture.setVisibility(View.VISIBLE);
                            }
                        });

                        submitCapture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            @ExperimentalGetImage
                            public void onClick(View view) {
                                // Finish this
                                Image mediaImage = image.getImage();
                                if (mediaImage != null){
                                    InputImage inputImage = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());
                                    verifyID(inputImage, image);
                                    // Still some work left (Handling images from gallery, defining a function to automatically find name)
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        AlertDialog captureFailed = new MaterialAlertDialogBuilder(CaptureImageActivity.this)
                                .setTitle("Unable to capture photos")
                                .setMessage("There seems to be a problem with capturing the photo:\n" + exception.getMessage())
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startCamera();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(CaptureImageActivity.this, StudentUserProfile.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .create();
                        captureFailed.show();
                        captureFailed.setCanceledOnTouchOutside(false);
                        captureFailed.setCancelable(false);
                    }
                });

                /*
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
                 */
            }
        });
    }

    /*public String getBatchDirectoryName() {
        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {
            Toast.makeText(this, "Unable to save photo. Please report a bug in this regard.", Toast.LENGTH_LONG).show();
            finish();
        }

        return app_folder_path;
    }*/

    // To be deleted
    private void test_text(Text textTask){
        String textResult = textTask.getText();
        Intent intent = new Intent(CaptureImageActivity.this, IDVerificationActivity.class);
        intent.putExtra("test_text", textResult);
        startActivity(intent);
    }


    private void verifyID(InputImage IDImage, ImageProxy proxyImage){
        TextRecognizer recognizer = TextRecognition.getClient();

        Task<Text> result = recognizer.process(IDImage).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                AlertDialog textRegistered = new MaterialAlertDialogBuilder(CaptureImageActivity.this)
                .setTitle("ID Registered")
                .setMessage("We have extracted the necessary information for verification from the provided ID card. " +
                        "Your details shall be verified and, if found valid, shall be updated shortly." +
                        "In case of data inconsistency, mismatch or an invalid ID, we will immediately notify you via e-mail.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                proxyImage.close();
                                /*Intent intent = new Intent(CaptureImageActivity.this, StudentUserProfile.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);*/

                                // To be deleted
                                test_text(text);
                            }
                        })
                        .create();
                textRegistered.show();
                textRegistered.setCanceledOnTouchOutside(false);
                textRegistered.setCancelable(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AlertDialog textRegisteredFailed = new MaterialAlertDialogBuilder(CaptureImageActivity.this)
                        .setTitle("Failed to register your ID")
                        .setMessage(e.getMessage() + "\nYour image seems to be out of focus and not clear enough. Please retry with a clearer picture.")
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                proxyImage.close();
                                cameraView.setVisibility(View.VISIBLE);
                                captureButton.setVisibility(View.VISIBLE);

                                capturedImageView.setVisibility(View.GONE);
                                cancelCapture.setVisibility(View.GONE);
                                retryCapture.setVisibility(View.GONE);
                                submitCapture.setVisibility(View.GONE);
                                startCamera();
                            }
                        })
                        .setNegativeButton("Cancel Verification", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                proxyImage.close();
                                Toast.makeText(CaptureImageActivity.this, "Cancelled Verification.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CaptureImageActivity.this, StudentUserProfile.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .create();
                textRegisteredFailed.show();
                textRegisteredFailed.setCancelable(false);
                textRegisteredFailed.setCanceledOnTouchOutside(false);
            }
        });
    }
}

/*
class YourAnalyzer implements ImageAnalysis.Analyzer {
    @Override
    public void analyze(ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
        }
    }
}
*/