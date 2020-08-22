package com.rishtey.listeners;

import org.json.JSONException;

public interface TaskListener {
    void trigger() throws JSONException;

    void onCancel();

    boolean isTaskAlive();
}
