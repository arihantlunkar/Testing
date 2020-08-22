package com.rishtey;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class UploadTaskServerCommunicator implements ITask, Runnable {

    static class UploadData {
        Uri bioData;
        String fromID;
        ArrayList<Uri> pictures;
    }

    static class ContentUriRequestBody extends RequestBody {

        ContentResolver contentResolver;
        Uri uri;
        int totalFilesToUpload;
        int currentFileNoUploading;
        ResponseListener responseListener;

        ContentUriRequestBody(ContentResolver contentResolver, Uri uri, int totalFilesToUpload, int currentFileNoUploading, ResponseListener responseListener) {
            this.contentResolver = contentResolver;
            this.uri = uri;
            this.totalFilesToUpload = totalFilesToUpload;
            this.currentFileNoUploading = currentFileNoUploading;
            this.responseListener = responseListener;
        }

        @Nullable
        @Override
        public MediaType contentType() {
            return MediaType.parse(Objects.requireNonNull(contentResolver.getType(uri)));
        }

        @Override
        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
            InputStream inputStream = Objects.requireNonNull(contentResolver.openInputStream(uri));
            try (Source source = Okio.source(inputStream)) {
                bufferedSink.writeAll(source);
            }
            responseListener.onFileUpload(currentFileNoUploading);
        }
    }

    private Context context;
    private UploadData uploadData;
    private ResponseListener responseListener;
    private Thread thread;

    public interface ResponseListener {
        void onFileUpload(int fileNumber);

        void onSuccess(JSONObject jsonObject);

        void onFailure(String errMsg);
    }

    public UploadTaskServerCommunicator(@NotNull Context context, @NotNull UploadData uploadData, @NotNull ResponseListener responseListener) {
        this.context = context;
        this.uploadData = uploadData;
        this.responseListener = responseListener;
        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder multipartBody = createMultipartBody();

        Request request = createRequest(multipartBody);

        try {
            Response response = client.newCall(request).execute();
            responseListener.onSuccess(new JSONObject(Objects.requireNonNull(response.body()).string()));
        } catch (IOException | JSONException e) {
            responseListener.onFailure(e.getMessage());
        }
    }

    private Request createRequest(@NotNull MultipartBody.Builder multipartBody) {
        return new Request.Builder()
                .url(Utilities.getRootURL())
                .post(multipartBody.build())
                .build();
    }

    private MultipartBody.Builder createMultipartBody() {
        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multipartBody.addFormDataPart("task", "upload");
        multipartBody.addFormDataPart("fromID", uploadData.fromID);
        multipartBody.addFormDataPart("biodata", "biodata", new ContentUriRequestBody(context.getContentResolver(), uploadData.bioData, 1 + uploadData.pictures.size(), 1, responseListener));

        for (int i = 0; i < uploadData.pictures.size(); ++i) {
            multipartBody.addFormDataPart("picture" + (i + 1), "picture" + (i + 1), new ContentUriRequestBody(context.getContentResolver(), uploadData.pictures.get(i), 1 + uploadData.pictures.size(), i + 2, responseListener));
        }
        return multipartBody;
    }

    @Override
    public void trigger() {
        this.thread.start();
    }
}
