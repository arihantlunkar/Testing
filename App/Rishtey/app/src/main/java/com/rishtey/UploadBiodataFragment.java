package com.rishtey;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class UploadBiodataFragment extends Fragment implements View.OnClickListener {

    private static final int GALLERY_REQUEST_PERMISSIONS_CODE = 1;
    public static final String NO_PERMISSIONS_TO_ACCESS_GALLERY = "No permissions to access gallery.";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_biodata, container, false);
        view.findViewById(R.id.floatingActionButton).setOnClickListener(this);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (GALLERY_REQUEST_PERMISSIONS_CODE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*")
                        .addCategory(Intent.CATEGORY_OPENABLE)
                        .putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "application/pdf"});
                startActivityForResult(Intent.createChooser(intent, "Select BioData"), GALLERY_REQUEST_PERMISSIONS_CODE);
            } else {
                Toast.makeText(getActivity(), NO_PERMISSIONS_TO_ACCESS_GALLERY, Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && null != data && requestCode == GALLERY_REQUEST_PERMISSIONS_CODE && null != data.getData()) {
            Uri uri = data.getData();
        }
    }

    @Override
    public void onClick(View view) {
        if (R.id.floatingActionButton == view.getId()) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_PERMISSIONS_CODE);
        }
    }
}