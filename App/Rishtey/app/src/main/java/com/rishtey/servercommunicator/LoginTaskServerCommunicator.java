package com.rishtey.servercommunicator;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rishtey.listeners.TaskListener;

import org.json.JSONException;
import org.json.JSONObject;

/*
*
    LoginTaskServerCommunicator.LoginData loginData = new LoginTaskServerCommunicator.LoginData();
    loginData.socialMedia = "Facebook";
    loginData.socialMediaID = "123";
    loginData.name = "Arihant";
    loginData.email = "arihantlunkar1993@gmail.com";
    loginData.gender = "Male";

    try {
        new LoginTaskServerCommunicator(this, ROOT_URL, loginData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
            }
        }).trigger();
    } catch (JSONException e) {
        e.printStackTrace();
    }
*
* */

public class LoginTaskServerCommunicator implements TaskListener {

    static class LoginData {
        public String socialMedia;
        public String socialMediaID;
        public String name;
        public String email;
        public String birthday;
        public String gender;
        public String pictureURL;
    }

    private Context context;
    private String url;
    private LoginData loginData;
    private Response.Listener<JSONObject> taskCompletedResponseListener;
    private Response.ErrorListener errorListener;

    public LoginTaskServerCommunicator(Context context, String url, LoginData loginData, Response.Listener<JSONObject> taskCompletedResponseListener, Response.ErrorListener errorListener) {
        this.context = context;
        this.url = url;
        this.loginData = loginData;
        this.taskCompletedResponseListener = taskCompletedResponseListener;
        this.errorListener = errorListener;
    }

    @Override
    public void trigger() throws JSONException {
        Volley.newRequestQueue(context).add(new JsonObjectRequest(Request.Method.POST, url, createJsonObject(loginData), taskCompletedResponseListener, errorListener));
    }

    @Override
    public void onCancel() {

    }

    @Override
    public boolean isTaskAlive() {
        return false;
    }

    private JSONObject createJsonObject(LoginData loginData) throws JSONException {
        return new JSONObject()
                .put("input", new JSONObject()
                        .put("task", "login")
                        .put("data", new JSONObject()
                                .put("socialMedia", loginData.socialMedia)
                                .put("socialMediaID", loginData.socialMediaID)
                                .put("name", loginData.name)
                                .put("email", loginData.email)
                                .put("birthday", loginData.birthday)
                                .put("gender", loginData.gender)
                                .put("pictureURL", loginData.pictureURL)));
    }
}
