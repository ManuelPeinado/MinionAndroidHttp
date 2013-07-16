package com.menor.minionandroidhttp;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MinionHttpRequest implements Runnable {






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
            OutputStream out = null;
            try {
                HttpURLConnection connection = mOkHttpClient.open(new URL(mUrl));
                connection.setConnectTimeout(mConnectionTimeOut);
                connection.setReadTimeout(mReadTimeOut);
                connection.setDoInput(true);
                connection.setDoOutput(true);
//                connection.setUseCaches(false);

                //set connection params
                for (Map.Entry<String, String> entry : mGlobalProperties.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }

                if (mRequestProperties != null) {
                    for (Map.Entry<String, String> entry : mRequestProperties.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }

                switch (mRequestMethod) {
                    case GET:
                    case DELETE:
                        connection.setRequestMethod(mRequestMethod.toString());
                        break;
                    case POST:
                    case PUT:
                        connection.setRequestMethod(mRequestMethod.toString());
                        if (mRequestParams != null) {
                            out = connection.getOutputStream();
                            out.write(mRequestParams.getParamString().getBytes("UTF-8"));
                        }
                        break;
                    default:
                        break;
                }

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
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }



}
