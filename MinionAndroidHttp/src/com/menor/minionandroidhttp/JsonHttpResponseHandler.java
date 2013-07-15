package com.menor.minionandroidhttp;

import android.os.Message;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;
import java.util.Map;

public class JsonHttpResponseHandler extends MinionHttpResponseHandler {

    protected static final int SUCCESS_JSON_MESSAGE = 100;

    /**
     * Fired when a request returns successfully and contains a json object
     * at the base of the response string. Override to handle in your
     * own code.
     *
     * @param response the parsed json object found in the server response (if any)
     */
    public void onSuccess(JSONObject response) {}

    /**
     * Fired when a request returns successfully and contains a json object
     * at the base of the response string. Override to handle in your
     * own code.
     *
     * @param tag the associated id to the call
     * @param response the parsed json object found in the server response (if any)
     */
    public void onSuccess(String tag, JSONObject response) {}

    /**
     * Fired when a request returns successfully and contains a json array
     * at the base of the response string. Override to handle in your
     * own code.
     *
     * @param response the parsed json array found in the server response (if any)
     */
    public void onSuccess(JSONArray response) {}

    /**
     * Fired when a request returns successfully and contains a json array
     * at the base of the response string. Override to handle in your
     * own code.
     *
     * @param tag the associated id to the call
     * @param response the parsed json array found in the server response (if any)
     */
    public void onSuccess(String tag, JSONArray response) {}

    /**
     * Fired when a request returns successfully and contains a json object
     * at the base of the response string. Override to handle in your
     * own code.
     *
     * @param statusCode the status code of the response
     * @param headers the headers of the HTTP response
     * @param response the parsed json object found in the server response (if any)
     */
    public void onSuccess(int statusCode, Map<String, List<String>> headers, JSONObject response) {
        onSuccess(statusCode, response);
    }

    /**
     * Fired when a request returns successfully and contains a json object
     * at the base of the response string. Override to handle in your
     * own code.
     *
     * @param tag the associated id to the call
     * @param statusCode the status code of the response
     * @param headers the headers of the HTTP response
     * @param response the parsed json object found in the server response (if any)
     */
    public void onSuccess(String tag, int statusCode, Map<String, List<String>> headers, JSONObject response) {
        onSuccess(tag, statusCode, response);
    }

    /**
     * Fired when a request returns successfully and contains a json object
     * at the base of the response string. Override to handle in your
     * own code.
     *
     * @param statusCode the status code of the response
     * @param response the parsed json object found in the server response (if any)
     */
    public void onSuccess(int statusCode, JSONObject response) {
        onSuccess(response);
    }

    /**
     * Fired when a request returns successfully and contains a json object
     * at the base of the response string. Override to handle in your
     * own code.
     *
     * @param tag the associated id to the call
     * @param statusCode the status code of the response
     * @param response the parsed json object found in the server response (if any)
     */
    public void onSuccess(String tag, int statusCode, JSONObject response) {
        onSuccess(tag, response);
    }


    /**
     * Fired when a request returns successfully and contains a json array
     * at the base of the response string. Override to handle in your
     * own code.
     * @param statusCode the status code of the response
     * @param headers the headers of the HTTP response
     * @param response the parsed json array found in the server response (if any)
     */
    public void onSuccess(int statusCode, Map<String, List<String>> headers, JSONArray response) {
        onSuccess(statusCode, response);
    }

    /**
     * Fired when a request returns successfully and contains a json array
     * at the base of the response string. Override to handle in your
     * own code.
     *
     * @param tag the associated id to the call
     * @param statusCode the status code of the response
     * @param headers the headers of the HTTP response
     * @param response the parsed json array found in the server response (if any)
     */
    public void onSuccess(String tag, int statusCode, Map<String, List<String>> headers, JSONArray response) {
        onSuccess(tag, statusCode, response);
    }

    /**
     * Fired when a request returns successfully and contains a json array
     * at the base of the response string. Override to handle in your
     * own code.
     *
     * @param statusCode the status code of the response
     * @param response the parsed json array found in the server response (if any)
     */
    public void onSuccess(int statusCode,  JSONArray response) {
        onSuccess(response);
    }

    /**
     * Fired when a request returns successfully and contains a json array
     * at the base of the response string. Override to handle in your
     * own code.
     *
     * @param tag the associated id to the call
     * @param statusCode the status code of the response
     * @param response the parsed json array found in the server response (if any)
     */
    public void onSuccess(String tag, int statusCode,  JSONArray response) {
        onSuccess(tag, response);
    }

    public void onFailure(Throwable error, JSONObject errorResponse) {}
    public void onFailure(Throwable error, JSONArray errorResponse) {}
    public void onFailure(String tag, Throwable error, JSONObject errorResponse) {}
    public void onFailure(String tag, Throwable error, JSONArray errorResponse) {}



    @Override
    protected void sendSuccessMessage(int statusCode, Map<String, List<String>> headers, String responseBody) {
        if (statusCode != HttpStatus.SC_NO_CONTENT){
            try {
                Object jsonResponse = parseResponse(responseBody);
                sendMessage(obtainMessage(SUCCESS_JSON_MESSAGE, new Object[]{statusCode, headers, jsonResponse}));
            } catch(JSONException e) {
                sendFailureMessage(e, responseBody);
            }
        } else {
            sendMessage(obtainMessage(SUCCESS_JSON_MESSAGE, new Object[]{statusCode, new JSONObject()}));
        }
    }

    protected Object parseResponse(String responseBody) throws JSONException {
        Object result = null;
        //trim the string to prevent start with blank, and test if the string is valid JSON, because the parser don't do this :(. If Json is not valid this will return null
        responseBody = responseBody.trim();
        if(responseBody.startsWith("{") || responseBody.startsWith("[")) {
            result = new JSONTokener(responseBody).nextValue();
        }
        if (result == null) {
            result = responseBody;
        }
        return result;
    }

    @Override
    protected void handleMessage(Message msg) {
        switch(msg.what){
            case SUCCESS_JSON_MESSAGE:
                Object[] response = (Object[]) msg.obj;
                handleSuccessJsonMessage(((Integer) response[0]).intValue(), (Map<String, List<String>>) response[1], response[2]);
                break;
            default:
                super.handleMessage(msg);
        }
    }

    protected void handleSuccessJsonMessage(int statusCode, Map<String, List<String>> headers, Object jsonResponse) {
        if(jsonResponse instanceof JSONObject) {
            onSuccess(statusCode, headers, (JSONObject)jsonResponse);
        } else if(jsonResponse instanceof JSONArray) {
            onSuccess(statusCode, headers, (JSONArray)jsonResponse);
        } else {
            onFailure(new JSONException("Unexpected type " + jsonResponse.getClass().getName()), (JSONObject)null);
        }
    }

    @Override
    protected void handleFailureMessage(Throwable e, String responseBody) {
        try {
            if (responseBody != null) {
                Object jsonResponse = parseResponse(responseBody);
                if(jsonResponse instanceof JSONObject) {
                    onFailure(e, (JSONObject)jsonResponse);
                } else if(jsonResponse instanceof JSONArray) {
                    onFailure(e, (JSONArray)jsonResponse);
                } else {
                    onFailure(e, responseBody);
                }
            }else {
                onFailure(e, "");
            }
        }catch(JSONException ex) {
            onFailure(e, responseBody);
        }
    }

}
