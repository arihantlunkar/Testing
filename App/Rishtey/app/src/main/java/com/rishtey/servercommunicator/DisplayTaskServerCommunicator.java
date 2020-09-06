package com.rishtey.servercommunicator;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.listeners.TaskListener;
import com.rishtey.util.Utilities;

import org.json.JSONException;
import org.json.JSONObject;
import org.jetbrains.annotations.NotNull;

public class DisplayTaskServerCommunicator implements TaskListener {

    public static class RequiredData {
        public int mCurrentPosition;
    }

    private Context mContext;
    private RequiredData mRequiredData;
    private Response.Listener<JSONObject> mTaskCompletedResponseListener;
    private Response.ErrorListener mErrorListener;

    public DisplayTaskServerCommunicator(@NotNull Context context, @NotNull RequiredData requiredData, @NotNull Response.Listener<JSONObject> taskCompletedResponseListener, @NotNull Response.ErrorListener errorListener) {
        mContext = context;
        mRequiredData = requiredData;
        mTaskCompletedResponseListener = taskCompletedResponseListener;
        mErrorListener = errorListener;
    }

    @Override
    public void trigger() throws JSONException {
        Volley.newRequestQueue(mContext).add(new JsonObjectRequest(Request.Method.POST, Utilities.getRootURL(false), createJsonObject(), mTaskCompletedResponseListener, mErrorListener));
    }

    @Override
    public void onCancel() {

    }

    @Override
    public boolean isTaskAlive() {
        return false;
    }

    private JSONObject createJsonObject() throws JSONException {
        return new JSONObject()
						.put("task", "display")
						.put("currentPosition", mRequiredData.mCurrentPosition);
    }
}
