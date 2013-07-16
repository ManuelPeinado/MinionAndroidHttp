package com.menor.minionandroidhttp;

import android.os.Message;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.Reader;

public class MinionHttpStringRequest extends DespicableHttpRequest implements Runnable {

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
        }
    }


    //TODO ES MUY PROBABLE QUE EL RUN LO TENGA QUE PONER EN EL PADRE!!!
    @Override
    public void run() {
        sendStartMessage();
        makeRequest();
    }














    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }




}
