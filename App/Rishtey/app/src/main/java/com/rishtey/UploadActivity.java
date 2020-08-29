package com.rishtey;

import android.os.Bundle;
import android.os.Build;
import android.graphics.PorterDuff;
import android.view.Window;
import android.view.WindowManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import com.rishtey.data.UploadData;
import com.rishtey.fragments.SelectBiodataFragment;
import com.rishtey.listeners.OnBackPressListener;
import com.rishtey.util.Utilities;
import android.view.MenuItem;

public class UploadActivity extends AppCompatActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
	
		Toolbar toolbar = findViewById(R.id.toolbar);
	
        setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorOnSurface), PorterDuff.Mode.SRC_ATOP);
		
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
	
	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}
}
