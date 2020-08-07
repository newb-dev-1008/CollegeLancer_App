package com.college.freelancestartup;

import android.graphics.Bitmap;

public class BugReport {
    private Bitmap bitmap;
    private String filePath;

    public BugReport(Bitmap bitmap, String filePath) {
        this.bitmap = bitmap;
        this.filePath = filePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getFilePath() {
        return filePath;
    }

}
