package com.rishtey;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.rishtey.fragments.SentFragment;
import com.rishtey.fragments.WishlistFragment;
import com.rishtey.fragments.BinFragment;
import com.rishtey.fragments.PrivacyPolicyFragment;
import com.rishtey.ui.home.HomeFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar = null;
    private DrawerLayout mDrawerLayout = null;
    private NavigationView mNavigationView = null;
    private ActionBarDrawerToggle mActionBarDrawerToggle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);

        setSupportActionBar(mToolbar);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace, new HomeFragment());
        fragmentTransaction.commit();
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.menu_home));
        mNavigationView.setCheckedItem(R.id.nav_home);

        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		if(R.id.nav_upload == item.getItemId()) {
			startActivity(new Intent(MainActivity.this, UploadActivity.class));
			mDrawerLayout.closeDrawer(GravityCompat.START, false);
		}
		else if(R.id.nav_logout == item.getItemId()) {
			Toast.makeText(this, "You are now logged out.", Toast.LENGTH_LONG).show();
		}
		else {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			switch(item.getItemId()) {
				case R.id.nav_home:
					fragmentTransaction.replace(R.id.fragmentPlace, new HomeFragment());
					break;
				case R.id.nav_sent:
					fragmentTransaction.replace(R.id.fragmentPlace, new SentFragment());
					break;
				case R.id.nav_wishlist:
					fragmentTransaction.replace(R.id.fragmentPlace, new WishlistFragment());
					break;
				case R.id.nav_bin:
					fragmentTransaction.replace(R.id.fragmentPlace, new BinFragment());
					break;
				case R.id.nav_privacy_policy:
					fragmentTransaction.replace(R.id.fragmentPlace, new PrivacyPolicyFragment());
					break;
				default:
					break;
			}
			fragmentTransaction.commit();
			Objects.requireNonNull(getSupportActionBar()).setTitle(item.getTitle());
			mDrawerLayout.closeDrawer(GravityCompat.START);
		}
        return true; // true will highlight the selected item
    }
}