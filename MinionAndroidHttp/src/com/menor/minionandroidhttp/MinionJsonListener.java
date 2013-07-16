package com.menor.minionandroidhttp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class MinionJsonListener implements DespicableListener {

    public void onSuccess(JSONObject content) {}

    public void onSuccess(int statusCode, JSONObject content) {
        onSuccess(content);
    }

    public void onSuccess(int statusCode, Map<String, List<String>> headers, JSONObject content) {
        onSuccess(statusCode, content);
    }

    public void onSuccess(JSONArray content) {}

    public void onSuccess(int statusCode, JSONArray content) {
        onSuccess(content);
    }

    public void onSuccess(int statusCode, Map<String, List<String>> headers, JSONArray content) {
        onSuccess(statusCode, content);
    }

    public void onFailure(Throwable error, JSONObject errorResponse) {}

    public void onFailure(Throwable error, JSONArray errorResponse) {}

    @Override
    public void onPreExecute() { }

    @Override
    public void onPostExecute() { }

    @Override
    public void onFailure(Throwable error, String content) { }

}
