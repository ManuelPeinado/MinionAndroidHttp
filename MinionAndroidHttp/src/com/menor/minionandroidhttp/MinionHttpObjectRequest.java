package com.menor.minionandroidhttp;

import android.os.Message;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class MinionHttpObjectRequest extends DespicableHttpRequest {

    private MinionObjectListener mListener;
    private static final Gson GSON = new Gson();

    public MinionHttpObjectRequest(OkHttpClient okHttpClient, String url, RequestParams requestParams, RequestProperties globalProperties, RequestProperties requestProperties, String charset, RequestMethod requestMethod, int connectionTimeOut, int readTimeOut, MinionObjectListener listener) {
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
                    mListener.onSuccess((Integer) response[0], (Map<String, List<String>>) response[1], response[2]);
                }
                break;
            case FAILURE_MESSAGE:
                response = (Object[]) msg.obj;
                if (mListener != null) {
                    mListener.onFailure((Throwable) response[0], response[1]);
                }
                break;
        }
    }

    @Override
    protected Object parseResponse(InputStream is) throws Exception {
        Reader reader = new InputStreamReader(is);
        if (mListener != null) {
            return GSON.fromJson(reader, mListener.getType());
        }
        return null;
    }
}
