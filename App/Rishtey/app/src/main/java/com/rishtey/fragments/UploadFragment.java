package com.rishtey.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.rishtey.R;
import com.rishtey.data.UploadData;
import com.rishtey.servercommunicator.UploadTaskServerCommunicator;
import com.rishtey.util.Utilities;
import com.rishtey.listeners.OnBackPressListener;
import com.rishtey.listeners.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadFragment extends Fragment implements View.OnClickListener, ResponseListener, OnBackPressListener {

    private UploadTaskServerCommunicator mUploadTaskServerCommunicator = null;
    private TextView mNumericPercentageTextView = null;
    private TextView mUploadHeadingTextView = null;
    private TextView mUploadSubHeadingTextView = null;
    private ProgressBar mProgressBar = null;
    private View mBiodataTickView = null;
    private View mImg1TickView = null;
    private View mImg2TickView = null;
    private View mImg3TickView = null;
    private View mImg4TickView = null;
    private View mImg5TickView = null;
    private static final String SOCKET_CLOSED = "Socket closed";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        init(view);

        updateFileNames(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (R.id.floatingActionButtonUpload == view.getId()) {

            view.findViewById(R.id.floatingActionButtonUpload).setVisibility(View.INVISIBLE);
            mUploadHeadingTextView.setText(R.string.We_Are_Uploading_Your_Files);
            mUploadSubHeadingTextView.setText(R.string.Please_wait);

            mUploadTaskServerCommunicator = new UploadTaskServerCommunicator(view.getContext(), UploadData.getInstance(), this);
            mUploadTaskServerCommunicator.trigger();
        }
    }

    @Override
    public void onFileUpload(final int fileNumber) {

        updatePercentage(fileNumber);

        updateTick(fileNumber);
    }

    @Override
    public void onSuccess(final JSONObject jsonObject) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (jsonObject.getInt("code")) {
                        case 3003:
                            mProgressBar.setProgress(100);
                            mNumericPercentageTextView.setText(R.string._100);
                            mUploadHeadingTextView.setText(R.string.Files_Uploaded_Successfully);
                            mUploadSubHeadingTextView.setText(R.string.Biodata_under_review_and_will_take_couple_of_days_to_get_reflected);
                            break;
                        case 2005:
                            mUploadHeadingTextView.setText(R.string.upload_limit_reached);
                            mUploadSubHeadingTextView.setText(R.string.please_upload_after_an_hour);
                            break;
                        default:
                            mUploadHeadingTextView.setText(R.string.upload_failure);
                            mUploadSubHeadingTextView.setText(R.string.please_try_again);
                            requireActivity().findViewById(R.id.floatingActionButtonUpload).setVisibility(View.VISIBLE);
                            resetTicks();
                            break;
                    }
                } catch (JSONException e) {
                    Log.d(this.getClass().toString(), e.toString());
                }
            }
        });
    }

    @Override
    public void onFailure(final String errMsg) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (errMsg.equalsIgnoreCase(SOCKET_CLOSED)) {
                    mUploadSubHeadingTextView.setText(R.string.upload_process_was_interrupted);
                } else {
                    mUploadSubHeadingTextView.setText(R.string.No_internet_connection);
                }
                mUploadHeadingTextView.setText(R.string.upload_failure);
                requireActivity().findViewById(R.id.floatingActionButtonUpload).setVisibility(View.VISIBLE);
                resetTicks();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        boolean shouldBackBePressed = true;
        if (null != mUploadTaskServerCommunicator && mUploadTaskServerCommunicator.isTaskAlive()) {
            mUploadTaskServerCommunicator.onCancel();
            shouldBackBePressed = false;
        }
        return shouldBackBePressed;
    }


    private void init(@NonNull View view) {
        view.findViewById(R.id.floatingActionButtonUpload).setOnClickListener(this);
        mNumericPercentageTextView = view.findViewById(R.id.numericPercentage);
        mProgressBar = view.findViewById(R.id.progressBar);
        mBiodataTickView = view.findViewById(R.id.biodataTick);
        mImg1TickView = view.findViewById(R.id.imgOneTick);
        mImg2TickView = view.findViewById(R.id.imgTwoTick);
        mImg3TickView = view.findViewById(R.id.imgThreeTick);
        mImg4TickView = view.findViewById(R.id.imgFourTick);
        mImg5TickView = view.findViewById(R.id.imgFiveTick);
        mUploadHeadingTextView = view.findViewById(R.id.uploadHeading);
        mUploadSubHeadingTextView = view.findViewById(R.id.uploadSubHeading);
    }

    private void updateFileNames(@NonNull View view) {
        TextView biodataTextView = view.findViewById(R.id.biodataFileName);
        TextView picture1TextView = view.findViewById(R.id.imgOneFileName);
        TextView picture2TextView = view.findViewById(R.id.imgTwoFileName);
        TextView picture3TextView = view.findViewById(R.id.imgThreeFileName);
        TextView picture4TextView = view.findViewById(R.id.imgFourFileName);
        TextView picture5TextView = view.findViewById(R.id.imgFiveFileName);

        biodataTextView.setText(Utilities.getRelativeName(this.requireContext(), UploadData.getInstance().mBioData));
        for (int i = 0; i < UploadData.getInstance().mPictures.size(); ++i) {
            if (0 == i) {
                picture1TextView.setText(Utilities.getRelativeName(this.requireContext(), UploadData.getInstance().mPictures.get(i)));
            } else if (1 == i) {
                view.findViewById(R.id.imgTwoThumbnail).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bottomLine3).setVisibility(View.VISIBLE);
                picture2TextView.setText(Utilities.getRelativeName(this.requireContext(), UploadData.getInstance().mPictures.get(i)));
            } else if (2 == i) {
                view.findViewById(R.id.imgThreeThumbnail).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bottomLine4).setVisibility(View.VISIBLE);
                picture3TextView.setText(Utilities.getRelativeName(this.requireContext(), UploadData.getInstance().mPictures.get(i)));
            } else if (3 == i) {
                view.findViewById(R.id.imgFourThumbnail).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bottomLine5).setVisibility(View.VISIBLE);
                picture4TextView.setText(Utilities.getRelativeName(this.requireContext(), UploadData.getInstance().mPictures.get(i)));
            } else if (4 == i) {
                view.findViewById(R.id.imgFiveThumbnail).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bottomLine6).setVisibility(View.VISIBLE);
                picture5TextView.setText(Utilities.getRelativeName(this.requireContext(), UploadData.getInstance().mPictures.get(i)));
            }
        }
    }

    private void resetTicks() {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBiodataTickView.setVisibility(View.INVISIBLE);
                mImg1TickView.setVisibility(View.INVISIBLE);
                mImg2TickView.setVisibility(View.INVISIBLE);
                mImg3TickView.setVisibility(View.INVISIBLE);
                mImg4TickView.setVisibility(View.INVISIBLE);
                mImg5TickView.setVisibility(View.INVISIBLE);

            }
        });
    }

    private void updateTick(final int fileNumber) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (fileNumber) {
                    case 1:
                        mBiodataTickView.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mImg1TickView.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        mImg2TickView.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        mImg3TickView.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        mImg4TickView.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        mImg5TickView.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void updatePercentage(final int fileNumber) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int percentage = (fileNumber * 100) / (2 /*biodata + mysql (extra)*/ + UploadData.getInstance().mPictures.size());
                mProgressBar.setProgress(percentage);
                String percent = percentage + " %";
                mNumericPercentageTextView.setText(percent);
            }
        });
    }
}
