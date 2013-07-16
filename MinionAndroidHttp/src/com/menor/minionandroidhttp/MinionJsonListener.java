package com.menor.minionandroidhttp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface MinionJsonListener extends DespicableListener {

    public void onSuccess(JSONObject response);

    public void onSuccess(int statusCode, JSONObject content);

    public void onSuccess(int statusCode, Map<String, List<String>> headers, JSONObject content);

    public void onSuccess(JSONArray response);

    public void onSuccess(int statusCode, JSONArray content);

    public void onSuccess(int statusCode, Map<String, List<String>> headers, JSONArray content);

    public void onFailure(Throwable error, JSONObject errorResponse);

    public void onFailure(Throwable error, JSONArray errorResponse);

}
