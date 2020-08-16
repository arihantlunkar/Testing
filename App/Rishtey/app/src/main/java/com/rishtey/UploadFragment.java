package com.rishtey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class UploadFragment extends Fragment implements View.OnClickListener {

    private UploadTaskServerCommunicator.UploadData uploadData = null;
    private ProgressBar progressBar;
    private static final String ROOT_URL = "http://192.168.0.10:8081/Testing/Backend/rishtey/index.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        view.findViewById(R.id.floatingActionButtonUpload).setOnClickListener(this);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        TextView biodataTextView = (TextView) view.findViewById(R.id.biodataFileName);
        TextView picture1TextView = (TextView) view.findViewById(R.id.imgOneFileName);
        TextView picture2TextView = (TextView) view.findViewById(R.id.imgTwoFileName);
        TextView picture3TextView = (TextView) view.findViewById(R.id.imgThreeFileName);
        TextView picture4TextView = (TextView) view.findViewById(R.id.imgFourFileName);
        TextView picture5TextView = (TextView) view.findViewById(R.id.imgFiveFileName);

        biodataTextView.setText(Utilities.getRelativeName(Objects.requireNonNull(this.getContext()), uploadData.bioData));
        for (int i = 0; i < uploadData.pictures.size(); ++i) {
            if (0 == i) {
                picture1TextView.setText(Utilities.getRelativeName(Objects.requireNonNull(this.getContext()), uploadData.pictures.get(i)));
            } else if (1 == i) {
                view.findViewById(R.id.imgTwoThumbnail).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bottomLine3).setVisibility(View.VISIBLE);
                picture2TextView.setText(Utilities.getRelativeName(Objects.requireNonNull(this.getContext()), uploadData.pictures.get(i)));
            } else if (2 == i) {
                view.findViewById(R.id.imgThreeThumbnail).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bottomLine4).setVisibility(View.VISIBLE);
                picture3TextView.setText(Utilities.getRelativeName(Objects.requireNonNull(this.getContext()), uploadData.pictures.get(i)));
            } else if (3 == i) {
                view.findViewById(R.id.imgFourThumbnail).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bottomLine5).setVisibility(View.VISIBLE);
                picture4TextView.setText(Utilities.getRelativeName(Objects.requireNonNull(this.getContext()), uploadData.pictures.get(i)));
            } else if (4 == i) {
                view.findViewById(R.id.imgFiveThumbnail).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bottomLine6).setVisibility(View.VISIBLE);
                picture5TextView.setText(Utilities.getRelativeName(Objects.requireNonNull(this.getContext()), uploadData.pictures.get(i)));
            } else {
                break;
            }
        }
        return view;
    }

    @Override
    public void onClick(final View view) {
        if (R.id.floatingActionButtonUpload == view.getId()) {
            try {
                view.findViewById(R.id.floatingActionButtonUpload).setEnabled(false);
                new UploadTaskServerCommunicator(view.getContext(), ROOT_URL, uploadData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(view.getContext(), response.toString(), Toast.LENGTH_LONG).show();
                        //view.findViewById(R.id.floatingActionButtonUpload).setEnabled(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(view.getContext(), "No internet connection!", Toast.LENGTH_LONG).show();
                        //view.findViewById(R.id.floatingActionButtonUpload).setEnabled(true);
                    }
                }, new UploadTaskServerCommunicator.IntermediateResponseListener() {
                    @Override
                    public void onPercentageChange(int percentage) {

                    }

                    @Override
                    public void onBioDataUpload() {

                    }

                    @Override
                    public void onPictureUpload(int pictureNumber) {

                    }
                }).trigger();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setArguments(UploadTaskServerCommunicator.UploadData uploadData) {
        this.uploadData = uploadData;
    }
}
