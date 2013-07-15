package com.menor.minionandroidhttp;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MinionHttpRequest implements Runnable {

    private final OkHttpClient mOkHttpClient;
    private final MinionHttpResponseHandler mResponseHandler;
    private final String mUrl;
    private final RequestParams mRequestParams;
    private final RequestProperties mGlobalProperties;
    private final RequestProperties mRequestProperties;
    private final int mSocketTimeOut;
    private final String mCharset;
    private final RequestMethod mRequestMethod;

    public MinionHttpRequest(OkHttpClient okHttpClient, MinionHttpResponseHandler responseHandler, String url, RequestParams requestParams, RequestProperties globalProperties, RequestProperties requestProperties, int socketTimeOut, String charset, RequestMethod requestMethod) {
        mOkHttpClient = okHttpClient;
        mResponseHandler = responseHandler;
        mUrl = url;
        mRequestParams = requestParams;
        mGlobalProperties = globalProperties;
        mRequestProperties = requestProperties;
        mSocketTimeOut = socketTimeOut;
        mCharset = charset;
        mRequestMethod = requestMethod;
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
            OutputStream out = null;
            try {

                HttpURLConnection connection = mOkHttpClient.open(new URL(mUrl));
                connection.setDoOutput(true);

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








//                conn.setReadTimeout(10000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//                writer.write(getQuery(params));
//                writer.close();
//                os.close();
//
//                conn.connect();



//                DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
//                wr.writeBytes(urlParameters);
//                wr.flush();
//                wr.close();


//                myURLConnection.setUseCaches(false);
//                myURLConnection.setDoInput(true);
//                myURLConnection.setDoOutput(true);
//                connection.setUseCaches (false);

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
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }



}
