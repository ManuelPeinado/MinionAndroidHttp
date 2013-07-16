package com.menor.minionandroidhttp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.squareup.okhttp.OkHttpClient;

public abstract class DespicableHttpRequest {

    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int START_MESSAGE = 2;
    protected static final int FINISH_MESSAGE = 3;

    protected final OkHttpClient mOkHttpClient;
    protected final String mUrl;
    protected final RequestParams mRequestParams;
    protected final RequestProperties mGlobalProperties;
    protected final RequestProperties mRequestProperties;
    protected final String mCharset;
    protected final RequestMethod mRequestMethod;
    protected final int mConnectionTimeOut;
    protected final int mReadTimeOut;

    private Handler handler;

    public DespicableHttpRequest(OkHttpClient okHttpClient, String url, RequestParams requestParams, RequestProperties globalProperties, RequestProperties requestProperties, String charset, RequestMethod requestMethod, int connectionTimeOut, int readTimeOut) {
        mOkHttpClient = okHttpClient;
        mUrl = url;
        mRequestParams = requestParams;
        mGlobalProperties = globalProperties;
        mRequestProperties = requestProperties;
        mCharset = charset;
        mRequestMethod = requestMethod;
        mConnectionTimeOut = connectionTimeOut;
        mReadTimeOut = readTimeOut;

        // Set up a handler to post events back to the correct thread if possible
        if (Looper.myLooper() != null) {
            handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    DespicableHttpRequest.this.handleMessage(msg);
                }

            };
        }
    }

    protected abstract void handleMessage(Message msg);

    //Pre-processing of messages (executes in background threadpool thread)
    protected void sendStartMessage() {
        sendMessage(obtainMessage(START_MESSAGE, null));
    }

    protected void sendMessage(Message msg) {
        if (handler != null) {
            handler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
    }

    protected Message obtainMessage(int responseMessage, Object response) {
        Message msg;
        if (handler != null) {
            msg = this.handler.obtainMessage(responseMessage, response);
        } else {
            msg = Message.obtain();
            msg.what = responseMessage;
            msg.obj = response;
        }
        return msg;
    }




}
