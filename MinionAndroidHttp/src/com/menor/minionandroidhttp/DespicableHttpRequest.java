package com.menor.minionandroidhttp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.squareup.okhttp.OkHttpClient;
import org.apache.http.client.HttpResponseException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public abstract class DespicableHttpRequest implements Runnable {

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
    protected abstract Object parseResponse(InputStream is) throws Exception;

    @Override
    public void run() {
        sendStartMessage();
        try {
            makeRequest();
        } catch (Exception e) {
            sendFinishMessage();
            sendFailureMessage(e, null);
        }

    }

    private void makeRequest() throws Exception {
        if(!Thread.currentThread().isInterrupted()) {
            OutputStream out = null;
            try {
                HttpURLConnection connection = mOkHttpClient.open(new URL(mUrl));
                addConnectionParameters(connection);

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
                            out.write(mRequestParams.getParamString().getBytes(Defaults.CHARSET_UTF8));
                        }
                        break;
                }

                if(!Thread.currentThread().isInterrupted()) {
                    parseData(connection);
                }
            } catch (Exception e) {
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

    private void parseData(HttpURLConnection connection) throws Exception {
        InputStream in;

        int status = connection.getResponseCode();
        // Read the response.
        if (status == HttpURLConnection.HTTP_OK) {
            in = connection.getInputStream();
        } else {
            in = connection.getErrorStream();
        }
        Object responseBody = parseResponse(in);
        sendFinishMessage();
        if (connection.getResponseCode() >= Defaults.REQUEST_CODE_OK) {
            sendFailureMessage(new HttpResponseException(connection.getResponseCode(), connection.getResponseMessage()), responseBody);
        } else {
            sendSuccessMessage(connection.getResponseCode(), connection.getHeaderFields(), responseBody);
        }
        in.close();
    }

    private void addConnectionParameters(HttpURLConnection connection) throws Exception {
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
    }

    //Pre-processing of messages (executes in background threadpool thread)
    private void sendStartMessage() {
        sendMessage(obtainMessage(START_MESSAGE, null));
    }

    private void sendFinishMessage() {
        sendMessage(obtainMessage(FINISH_MESSAGE, null));
    }

    private void sendFailureMessage(Throwable e, Object responseBody) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{e, responseBody}));
    }

    private void sendSuccessMessage(int statusCode, Map<String, List<String>> headers, Object responseBody) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]{new Integer(statusCode), headers, responseBody}));
    }

    private void sendMessage(Message msg) {
        if (handler != null) {
            handler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
    }

    private Message obtainMessage(int responseMessage, Object response) {
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
