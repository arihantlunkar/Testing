package com.rishtey.listeners;

import org.json.JSONObject;

public interface ResponseListener {
    void onFileUpload(int fileNumber);

    void onSuccess(JSONObject jsonObject);

    void onFailure(String errMsg);
}
