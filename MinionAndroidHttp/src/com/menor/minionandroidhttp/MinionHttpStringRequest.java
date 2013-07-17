package com.menor.minionandroidhttp;

import android.os.Message;
import com.squareup.okhttp.OkHttpClient;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class MinionHttpStringRequest extends DespicableHttpRequest {

    private MinionStringListener mListener;

    public MinionHttpStringRequest(OkHttpClient okHttpClient, String url, RequestParams requestParams, RequestProperties globalProperties, RequestProperties requestProperties, String charset, RequestMethod requestMethod, int connectionTimeOut, int readTimeOut, MinionStringListener listener) {
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
                    mListener.onSuccess((Integer) response[0], (Map<String, List<String>>) response[1], (String) response[2]);
                }
                break;
            case FAILURE_MESSAGE:
                response = (Object[]) msg.obj;
                if (mListener != null) {
                    mListener.onFailure((Throwable) response[0], (String) response[1]);
                }
                break;
        }
    }

    @Override
    protected Object parseResponse(InputStream is) throws Exception {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        return readAll(rd);
    }



}
