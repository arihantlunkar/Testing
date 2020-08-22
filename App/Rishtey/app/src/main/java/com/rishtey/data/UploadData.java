package com.rishtey.data;

import android.net.Uri;

import java.util.ArrayList;

public class UploadData {
    public Uri mBioData;
    public String mFromID;
    public ArrayList<Uri> mPictures;

    private static UploadData mUploadData = new UploadData();

    private UploadData() {
    }

    public static UploadData getInstance() {
        return mUploadData;
    }
}
