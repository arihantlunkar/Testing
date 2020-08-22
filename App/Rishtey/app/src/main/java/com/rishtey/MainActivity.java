package com.rishtey;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UploadTaskServerCommunicator.UploadData uploadData = new UploadTaskServerCommunicator.UploadData();
        uploadData.fromID = Utilities.getUploadID();

        SelectBiodataFragment fragment = new SelectBiodataFragment();
        fragment.setArguments(uploadData);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace, fragment);
        fragmentTransaction.commit();
    }
}
