package com.rishtey.servercommunicator;

import android.content.Context;

import com.rishtey.data.UploadData;
import com.rishtey.util.Utilities;
import com.rishtey.listeners.ResponseListener;
import com.rishtey.listeners.TaskListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UploadTaskServerCommunicator implements TaskListener, Runnable {

    private Context mContext;
    private UploadData mUploadData;
    private ResponseListener mResponseListener;
    private Thread mThread = new Thread(this);
    private OkHttpClient mClient = new OkHttpClient();
    private final static String TAG = UploadTaskServerCommunicator.class.toString();

    public UploadTaskServerCommunicator(@NotNull Context context, @NotNull UploadData uploadData, @NotNull ResponseListener responseListener) {
        mContext = context;
        mUploadData = uploadData;
        mResponseListener = responseListener;
    }

    @Override
    public void run() {
        MultipartBody.Builder multipartBody = createMultipartBody();

        Request request = createRequest(multipartBody);

        try {
            Response response = mClient.newCall(request).execute();
            mResponseListener.onSuccess(new JSONObject(Objects.requireNonNull(response.body()).string()));
        } catch (IOException | JSONException e) {
            mResponseListener.onFailure(e.getMessage());
        }
    }

    @Override
    public void trigger() {
        this.mThread.start();
    }

    @Override
    public void onCancel() {
        if (null != mThread && mThread.isAlive()) {
            for (Call call : mClient.dispatcher().queuedCalls()) {
                if (Objects.equals(call.request().tag(), TAG))
                    call.cancel();
            }
            for (Call call : mClient.dispatcher().runningCalls()) {
                if (Objects.equals(call.request().tag(), TAG))
                    call.cancel();
            }
            mThread.interrupt();
        }
    }

    @Override
    public boolean isTaskAlive() {
        return null != mThread && mThread.isAlive();
    }

    private Request createRequest(@NotNull MultipartBody.Builder multipartBody) {
        return new Request.Builder()
                .url(Utilities.getRootURL())
                .post(multipartBody.build())
                .tag(TAG)
                .build();
    }

    private MultipartBody.Builder createMultipartBody() {
        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multipartBody.addFormDataPart("task", "upload");
        multipartBody.addFormDataPart("fromID", mUploadData.mFromID);
        multipartBody.addFormDataPart("biodata", "biodata", new ContentUriRequestBody(mContext.getContentResolver(), mUploadData.mBioData, mResponseListener, 1));

        for (int i = 0; i < mUploadData.mPictures.size(); ++i) {
            multipartBody.addFormDataPart("picture" + (i + 1), "picture" + (i + 1), new ContentUriRequestBody(mContext.getContentResolver(), mUploadData.mPictures.get(i), mResponseListener, i + 2));
        }
        return multipartBody;
    }
}
