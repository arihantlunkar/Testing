package com.rishtey;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class SelectPicturesFragment extends Fragment implements View.OnClickListener {

    public static final int MAXIMUM_FILE_SIZE_ALLOWED = 4; // in MB
    private static final int GALLERY_REQUEST_PERMISSIONS_CODE = 1;
    public static final String[] PICTURES_ALLOWED_MIME_TYPES = {"image/*"};
    public static final int MAX_NO_PICTURES_ALLOWED = 5;
    private UploadTaskServerCommunicator.UploadData uploadData = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_pictures, container, false);
        view.findViewById(R.id.floatingActionButton).setOnClickListener(this);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (GALLERY_REQUEST_PERMISSIONS_CODE != requestCode) {
            return;
        }
        if (0 < grantResults.length && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
            openGallery();
        } else {
            Toast.makeText(getActivity(), getString(R.string.onGalleryDeniedAccessMsg), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode || GALLERY_REQUEST_PERMISSIONS_CODE != requestCode || null == data || null == this.getContext()) {
            return;
        }
        if (areAllPicturesSizesValid(data)) {
            gotoNextFragment();
        }
    }

    @Override
    public void onClick(View view) {
        if (R.id.floatingActionButton == view.getId()) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_PERMISSIONS_CODE);
        }
    }

    public void setArguments(@NonNull UploadTaskServerCommunicator.UploadData uploadData) {
        this.uploadData = uploadData;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*")
                .addCategory(Intent.CATEGORY_OPENABLE)
                .putExtra(Intent.EXTRA_MIME_TYPES, PICTURES_ALLOWED_MIME_TYPES)
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.onSelectPicturesGalleryTitleMsg)), GALLERY_REQUEST_PERMISSIONS_CODE);
    }

    private void gotoNextFragment() {
        UploadFragment fragment = new UploadFragment();
        fragment.setArguments(uploadData);

        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.fragmentPlace, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private boolean areAllPicturesSizesValid(@NonNull Intent data) {
        boolean areAllPicturesSizesValid = true;
        ClipData clipData = data.getClipData();
        uploadData.pictures = new ArrayList<>();

        if (null != data.getData()) {
            if (Utilities.isFileSizeValid(this.requireContext(), data.getData(), MAXIMUM_FILE_SIZE_ALLOWED)) {
                uploadData.pictures.add(data.getData());
            } else {
                Toast.makeText(getActivity(), "Size of " + Utilities.getRelativeName(this.requireContext(), data.getData()) + " is more than " + MAXIMUM_FILE_SIZE_ALLOWED + " MB.", Toast.LENGTH_LONG).show();
                areAllPicturesSizesValid = false;
            }
        } else if (null != clipData) {
            for (int i = 0; i < clipData.getItemCount() && i < MAX_NO_PICTURES_ALLOWED; i++) {
                Uri uri = clipData.getItemAt(i).getUri();
                if (Utilities.isFileSizeValid(this.requireContext(), uri, MAXIMUM_FILE_SIZE_ALLOWED)) {
                    uploadData.pictures.add(clipData.getItemAt(i).getUri());
                } else {
                    Toast.makeText(getActivity(), "Size of " + Utilities.getRelativeName(this.requireContext(), uri) + " is more than " + MAXIMUM_FILE_SIZE_ALLOWED + " MB.", Toast.LENGTH_LONG).show();
                    areAllPicturesSizesValid = false;
                    break;
                }
            }
        }
        return areAllPicturesSizesValid;
    }
}
