package com.rishtey;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class UploadTaskServerCommunicator implements ITask {

    static class UploadData {
        Uri bioData;
        Uri picture1;
        Uri picture2;
        Uri picture3;
        Uri picture4;
        Uri picture5;
        String fromID;
    }

    private Context context;
    private String url;
    private UploadData uploadData;
    private Response.Listener<JSONObject> taskCompletedResponseListener;
    private Response.ErrorListener errorListener;
    private IntermediateResponseListener intermediateResponseListener;
    private Boolean isUploadCancelled;

    public interface IntermediateResponseListener {
        void onPercentageChange(int percentage);

        void onBioDataUpload();

        void onPictureUpload(int pictureNumber);
    }

    public UploadTaskServerCommunicator(Context context, String url, UploadData uploadData, Response.Listener<JSONObject> taskCompletedResponseListener, Response.ErrorListener errorListener, IntermediateResponseListener intermediateResponseListener) {
        this.context = context;
        this.url = url;
        this.uploadData = uploadData;
        this.taskCompletedResponseListener = taskCompletedResponseListener;
        this.errorListener = errorListener;
        this.intermediateResponseListener = intermediateResponseListener;
        this.isUploadCancelled = false;
    }

    public void setIsUploadCancelled() {
        isUploadCancelled = true;
    }

    @Override
    public void trigger() throws IOException, NullPointerException, JSONException {
        Volley.newRequestQueue(context).add(new JsonObjectRequest(Request.Method.POST, url, createJsonObject(uploadData), taskCompletedResponseListener, errorListener));
    }

    private JSONObject createJsonObject(UploadData uploadData) throws JSONException, IOException {
        return new JSONObject()
                .put("input", new JSONObject()
                        .put("task", "upload")
                        .put("data", new JSONObject()
                                .put("fromID", uploadData.fromID)
                                .put("stringImageBioData", Utilities.getStringImage(context, uploadData.bioData))
                                .put("stringImagePicture1", Utilities.getStringImage(context, uploadData.picture1))
                                .put("stringImagePicture2", Utilities.isNullOrEmpty(uploadData.picture2) ? null : Utilities.getStringImage(context, uploadData.picture2))
                                .put("stringImagePicture3", Utilities.isNullOrEmpty(uploadData.picture3) ? null : Utilities.getStringImage(context, uploadData.picture3))
                                .put("stringImagePicture4", Utilities.isNullOrEmpty(uploadData.picture4) ? null : Utilities.getStringImage(context, uploadData.picture4))
                                .put("stringImagePicture5", Utilities.isNullOrEmpty(uploadData.picture5) ? null : Utilities.getStringImage(context, uploadData.picture5))
                                .put("extensionBioData", Utilities.getMimeType(context, uploadData.bioData))
                                .put("extensionPicture1", Utilities.getMimeType(context, uploadData.picture1))
                                .put("extensionPicture2", Utilities.isNullOrEmpty(uploadData.picture2) ? null : Utilities.getMimeType(context, uploadData.picture2))
                                .put("extensionPicture3", Utilities.isNullOrEmpty(uploadData.picture3) ? null : Utilities.getMimeType(context, uploadData.picture3))
                                .put("extensionPicture4", Utilities.isNullOrEmpty(uploadData.picture4) ? null : Utilities.getMimeType(context, uploadData.picture4))
                                .put("extensionPicture5", Utilities.isNullOrEmpty(uploadData.picture5) ? null : Utilities.getMimeType(context, uploadData.picture5))));
    }
}
