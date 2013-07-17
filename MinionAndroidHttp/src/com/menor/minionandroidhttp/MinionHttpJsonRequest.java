package com.menor.minionandroidhttp;

import android.os.Message;
import com.squareup.okhttp.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class MinionHttpJsonRequest extends DespicableHttpRequest {

    private MinionJsonListener mListener;

    public MinionHttpJsonRequest(OkHttpClient okHttpClient, String url, RequestParams requestParams, RequestProperties globalProperties, RequestProperties requestProperties, String charset, RequestMethod requestMethod, int connectionTimeOut, int readTimeOut, MinionJsonListener listener) {
        super(okHttpClient, url, requestParams, globalProperties, requestProperties, charset, requestMethod, connectionTimeOut, readTimeOut);
        mListener = listener;
    }

    // Methods which emulate android's Handler and Message methods
    @Override
    protected void handleMessage(Message msg) {
        Object[] response;
        switch (msg.what) {
            case START_MESSAGE:
                if (mListener != null) {
                    mListener.onPreExecute();
                }
                break;
            case FINISH_MESSAGE:
                if (mListener != null) {
                    mListener.onPostExecute();
                }
                break;
            case SUCCESS_MESSAGE:
                response = (Object[]) msg.obj;
                if (mListener != null) {
                    if (response[2] instanceof JSONObject) {
                        mListener.onSuccess((Integer) response[0], (Map<String, List<String>>) response[1], (JSONObject) response[2]);
                    } else if (response[2] instanceof JSONArray) {
                        mListener.onSuccess((Integer) response[0], (Map<String, List<String>>) response[1], (JSONArray) response[2]);
                    } else {
                        mListener.onFailure(new JSONException("Unexpected type " + response[2].getClass().getName()), (String)null);
                    }
                }
                break;
            case FAILURE_MESSAGE:
                response = (Object[]) msg.obj;
                if (mListener != null) {
                    if (response[2] instanceof JSONObject) {
                        mListener.onFailure((Throwable) response[0], (JSONObject)response[1]);
                    } else if (response[2] instanceof JSONArray) {
                        mListener.onFailure((Throwable) response[0], (JSONArray)response[1]);
                    } else {
                        mListener.onFailure((Throwable) response[0], (String)response[1]);
                    }
                }
                break;
        }
    }

    @Override
    protected Object parseResponse(InputStream is) throws Exception {
        Object result = null;
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String response = readAll(rd);
//        JSONObject json = new JSONObject(jsonText);

        //trim the string to prevent start with blank, and test if the string is valid JSON, because the parser don't do this :(. If Json is not valid this will return null
        response = response.trim();
        if(response.startsWith("{") || response.startsWith("[")) {
            result = new JSONTokener(response).nextValue();
        }
        if (result == null) {
            return response;
        }
        return result;
    }
}
