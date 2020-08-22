package com.rishtey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

public class UploadFragment extends Fragment implements View.OnClickListener, UploadTaskServerCommunicator.ResponseListener {

    private UploadTaskServerCommunicator.UploadData uploadData = null;
    private TextView numericPercentageTextView = null;
    private TextView uploadHeadingTextView = null;
    private TextView uploadSubHeadingTextView = null;
    private ProgressBar progressBar = null;
    private View biodataTickView = null;
    private View img1TickView = null;
    private View img2TickView = null;
    private View img3TickView = null;
    private View img4TickView = null;
    private View img5TickView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        init(view);

        updateFileNames(view);

        return view;
    }

    private void init(View view) {
        view.findViewById(R.id.floatingActionButtonUpload).setOnClickListener(this);
        numericPercentageTextView = view.findViewById(R.id.numericPercentage);
        progressBar = view.findViewById(R.id.progressBar);
        biodataTickView = view.findViewById(R.id.biodataTick);
        img1TickView = view.findViewById(R.id.imgOneTick);
        img2TickView = view.findViewById(R.id.imgTwoTick);
        img3TickView = view.findViewById(R.id.imgThreeTick);
        img4TickView = view.findViewById(R.id.imgFourTick);
        img5TickView = view.findViewById(R.id.imgFiveTick);
        uploadHeadingTextView = view.findViewById(R.id.uploadHeading);
        uploadSubHeadingTextView = view.findViewById(R.id.uploadSubHeading);
    }

    private void updateFileNames(View view) {
        TextView biodataTextView = view.findViewById(R.id.biodataFileName);
        TextView picture1TextView = view.findViewById(R.id.imgOneFileName);
        TextView picture2TextView = view.findViewById(R.id.imgTwoFileName);
        TextView picture3TextView = view.findViewById(R.id.imgThreeFileName);
        TextView picture4TextView = view.findViewById(R.id.imgFourFileName);
        TextView picture5TextView = view.findViewById(R.id.imgFiveFileName);

        biodataTextView.setText(Utilities.getRelativeName(this.requireContext(), uploadData.bioData));
        for (int i = 0; i < uploadData.pictures.size(); ++i) {
            if (0 == i) {
                picture1TextView.setText(Utilities.getRelativeName(this.requireContext(), uploadData.pictures.get(i)));
            } else if (1 == i) {
                view.findViewById(R.id.imgTwoThumbnail).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bottomLine3).setVisibility(View.VISIBLE);
                picture2TextView.setText(Utilities.getRelativeName(this.requireContext(), uploadData.pictures.get(i)));
            } else if (2 == i) {
                view.findViewById(R.id.imgThreeThumbnail).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bottomLine4).setVisibility(View.VISIBLE);
                picture3TextView.setText(Utilities.getRelativeName(this.requireContext(), uploadData.pictures.get(i)));
            } else if (3 == i) {
                view.findViewById(R.id.imgFourThumbnail).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bottomLine5).setVisibility(View.VISIBLE);
                picture4TextView.setText(Utilities.getRelativeName(this.requireContext(), uploadData.pictures.get(i)));
            } else if (4 == i) {
                view.findViewById(R.id.imgFiveThumbnail).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bottomLine6).setVisibility(View.VISIBLE);
                picture5TextView.setText(Utilities.getRelativeName(this.requireContext(), uploadData.pictures.get(i)));
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (R.id.floatingActionButtonUpload == view.getId()) {

            view.findViewById(R.id.floatingActionButtonUpload).setVisibility(View.INVISIBLE);
            uploadHeadingTextView.setText(R.string.We_Are_Uploading_Your_Files);
            uploadSubHeadingTextView.setText(R.string.Please_wait);

            new UploadTaskServerCommunicator(view.getContext(), uploadData, this).trigger();
        }
    }

    public void setArguments(UploadTaskServerCommunicator.UploadData uploadData) {
        this.uploadData = uploadData;
    }

    @Override
    public void onFileUpload(final int fileNumber) {

        updatePercentage(fileNumber);

        updateTick(fileNumber);
    }

    private void updateTick(final int fileNumber) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (fileNumber) {
                    case 1:
                        biodataTickView.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        img1TickView.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        img2TickView.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        img3TickView.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        img4TickView.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        img5TickView.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void updatePercentage(final int fileNumber) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int percentage = (fileNumber * 100) / (2 /*biodata + mysql (extra)*/ + uploadData.pictures.size());
                progressBar.setProgress(percentage);
                String percent = percentage + " %";
                numericPercentageTextView.setText(percent);
            }
        });
    }

    @Override
    public void onSuccess(final JSONObject jsonObject) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(100);
                numericPercentageTextView.setText(R.string._100);
                uploadHeadingTextView.setText(R.string.Files_Uploaded_Successfully);
                uploadSubHeadingTextView.setText(R.string.Biodata_under_review_and_will_take_couple_of_days_to_get_reflected);
                Toast.makeText(getContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFailure(String errMsg) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), R.string.No_internet_connection, Toast.LENGTH_LONG).show();
            }
        });
    }
}
