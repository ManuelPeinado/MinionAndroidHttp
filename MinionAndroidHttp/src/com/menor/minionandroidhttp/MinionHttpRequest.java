package com.menor.minionandroidhttp;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MinionHttpRequest implements Runnable {



    private final String mUserAgent;
    private final int mSocketTimeOut;
    private final String mCharset;


    private final OkHttpClient mOkHttpClient;
    private final MinionHttpResponseHandler mResponseHandler;
    private final String mUrl;

    public MinionHttpRequest(OkHttpClient okHttpClient, MinionHttpResponseHandler responseHandler, String url, String userAgent, int socketTimeOut, String charset) {
        mOkHttpClient = okHttpClient;
        mUserAgent = userAgent;
        mResponseHandler = responseHandler;
        mUrl = url;
        mSocketTimeOut = socketTimeOut;
        mCharset = charset;
    }

    @Override
    public void run() {
        try {
            if (mResponseHandler != null) {
                mResponseHandler.sendStartMessage();
            }
            makeRequest();
            if(mResponseHandler != null) {
                mResponseHandler.sendFinishMessage();
            }
        } catch (IOException e) {
            if(mResponseHandler != null) {
                mResponseHandler.sendFinishMessage();
                mResponseHandler.sendFailureMessage(e, null);
            }
        }
    }

    private void makeRequest() throws IOException {
        if(!Thread.currentThread().isInterrupted()) {
            try {

                HttpURLConnection connection = mOkHttpClient.open(new URL(mUrl));

                //set connection params
                connection.setRequestProperty(PROPERTY_AGENT, mUserAgent);

//                connection.setConnectTimeout(); //creo que no funciona. El valido seria setSoTimeOut
//                HttpConnectionParams.setConnectionTimeout(this.client.getParams(), 3000);
//                HttpConnectionParams.setSoTimeout(this.client.getParams(), 5000);
//                connection.setConnectTimeout();
//                connection.setReadTimeout();


                if(!Thread.currentThread().isInterrupted()) {
                    if(mResponseHandler != null) {
                        mResponseHandler.sendResponseMessage(connection, mCharset);
                    }
                } else {
                    //TODO: should raise InterruptedException? this block is reached whenever the request is cancelled before its response is received
                }
            } catch (IOException e) {
                if(!Thread.currentThread().isInterrupted()) {
                    throw e;
                }
            }
        }
    }



}
