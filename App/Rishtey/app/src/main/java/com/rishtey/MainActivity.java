package com.rishtey;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.rishtey.data.UploadData;
import com.rishtey.fragments.SelectBiodataFragment;
import com.rishtey.listeners.OnBackPressListener;
import com.rishtey.util.Utilities;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UploadData.getInstance().mFromID = Utilities.getUploadID();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace, new SelectBiodataFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentPlace);
        if (fragment instanceof OnBackPressListener && !((OnBackPressListener) fragment).onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
