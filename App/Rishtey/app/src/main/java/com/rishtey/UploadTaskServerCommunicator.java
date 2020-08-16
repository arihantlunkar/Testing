package com.rishtey;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class UploadTaskServerCommunicator implements ITask {

    static class UploadData {
        Uri bioData;
        String fromID;
        ArrayList<Uri> pictures;
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

        Log.d("Arihant", createJsonObject(uploadData).toString());
        //Volley.newRequestQueue(context).add(new JsonObjectRequest(Request.Method.POST, url, createJsonObject(uploadData), taskCompletedResponseListener, errorListener));
    }

    private JSONObject createJsonObject(UploadData uploadData) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject()
                .put("fromID", uploadData.fromID)
                //.put("stringImageBioData", Utilities.getStringImage(context, uploadData.bioData))
                .put("extensionBioData", Utilities.getMimeType(context, uploadData.bioData));

        Log.d("Arihant", uploadData.pictures.size() + " => size");

        for (int i = 0; i < uploadData.pictures.size(); ++i) {
            //jsonObject.put("stringImagePicture" + (i + 1), Utilities.getStringImage(context, uploadData.pictures.get(i)));
            jsonObject.put("extensionPicture" + (i + 1), Utilities.getMimeType(context, uploadData.pictures.get(i)));
        }

        return new JSONObject()
                .put("input", new JSONObject()
                        .put("task", "upload")
                        .put("data", jsonObject));
    }
}
