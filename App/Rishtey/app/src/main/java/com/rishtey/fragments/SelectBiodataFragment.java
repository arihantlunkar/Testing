package com.rishtey.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.rishtey.R;
import com.rishtey.data.UploadData;
import com.rishtey.util.Utilities;

import static android.app.Activity.RESULT_OK;

public class SelectBiodataFragment extends Fragment implements View.OnClickListener {

    public static final int MAXIMUM_FILE_SIZE_ALLOWED = 5; // in MB
    public static final String[] BIODATA_ALLOWED_MIME_TYPES = {"image/*", "application/pdf"};
    private static final int GALLERY_REQUEST_PERMISSIONS_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_biodata, container, false);
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
    public void onClick(View view) {
        if (R.id.floatingActionButton == view.getId()) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_PERMISSIONS_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode || GALLERY_REQUEST_PERMISSIONS_CODE != requestCode || null == data || null == this.getContext() || null == data.getData()) {
            return;
        }
        if (Utilities.isFileSizeValid(this.getContext(), data.getData(), MAXIMUM_FILE_SIZE_ALLOWED)) {
            UploadData.getInstance().mBioData = data.getData();
            gotoNextFragment();
        } else {
            Toast.makeText(getActivity(), "Size of " + Utilities.getRelativeName(this.getContext(), data.getData()) + " is more than " + MAXIMUM_FILE_SIZE_ALLOWED + " MB.", Toast.LENGTH_LONG).show();

        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*")
                .addCategory(Intent.CATEGORY_OPENABLE)
                .putExtra(Intent.EXTRA_MIME_TYPES, BIODATA_ALLOWED_MIME_TYPES);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.onSelectBiodataGalleryTitleMsg)), GALLERY_REQUEST_PERMISSIONS_CODE);
    }

    private void gotoNextFragment() {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.fragmentPlace, new SelectPicturesFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}