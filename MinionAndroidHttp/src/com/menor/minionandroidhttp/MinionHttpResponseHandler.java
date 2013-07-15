package com.menor.minionandroidhttp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import org.apache.http.client.HttpResponseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;

public class MinionHttpResponseHandler implements MinionListener {

    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int START_MESSAGE = 2;
    protected static final int FINISH_MESSAGE = 3;

    private Handler handler;
    private String mTag;

    public MinionHttpResponseHandler() {
        this(null);
    }

    public MinionHttpResponseHandler(String tag) {
        // Set up a handler to post events back to the correct thread if possible
        mTag = tag;
        if (Looper.myLooper() != null) {
            handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    MinionHttpResponseHandler.this.handleMessage(msg);
                }

            };
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    public void onStart() { }

    @Override
    public void onStart(String tag) { }

    @Override
    public void onFinish() { }

    @Override
    public void onFinish(String tag) { }

    @Override
    public void onSuccess(SuccessResponse response) { }

    @Override
    public void onSuccess(String tag, SuccessResponse response) { }

    @Override
    public void onFailure(Throwable error, String content) { }

    @Override
    public void onFailure(String tag, Throwable error, String content) { }

    //Interface to MinionHttpRequest
    public void sendResponseMessage(HttpURLConnection connection, String charset) {
        String responseBody;
        InputStream in = null;
        try {
            int status = connection.getResponseCode();
            // Read the response.
            if (status == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
            } else {
                in = connection.getErrorStream();
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(in, Charset.forName(charset)));
            responseBody = readAll(rd);
            if (connection.getResponseCode() >= 300) {
                sendFailureMessage(new HttpResponseException(connection.getResponseCode(), connection.getResponseMessage()), responseBody);
            } else {
                sendSuccessMessage(connection.getResponseCode(), connection.getHeaderFields(), responseBody);
            }
            in.close();
        } catch (IOException e) {
            //file not found es 404 from server
            sendFailureMessage(e, null);
        } catch (UnsupportedCharsetException e) {
            //file not found es 404 from server
            sendFailureMessage(e, null);
        }

    }

    // Methods which emulate android's Handler and Message methods
    protected void handleMessage(Message msg) {
        Object[] response;
        switch (msg.what) {
            case SUCCESS_MESSAGE:
                response = (Object[]) msg.obj;
                handleSuccessMessage(((Integer) response[0]).intValue(), (Map<String, List<String>>) response[1], (String) response[2]);
                break;
            case FAILURE_MESSAGE:
                response = (Object[]) msg.obj;
                handleFailureMessage((Throwable) response[0], (String) response[1]);
                break;
            case START_MESSAGE:
                onStart(mTag);
                break;
            case FINISH_MESSAGE:
                onFinish(mTag);
                break;
        }
    }

    protected void sendMessage(Message msg) {
        if (handler != null) {
            handler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
    }

    protected Message obtainMessage(int responseMessage, Object response) {
        Message msg = null;
        if (handler != null) {
            msg = this.handler.obtainMessage(responseMessage, response);
        } else {
            msg = Message.obtain();
            msg.what = responseMessage;
            msg.obj = response;
        }
        return msg;
    }

    //Pre-processing of messages (in original calling thread, typically the UI thread)
    protected void handleSuccessMessage(int statusCode, Map<String, List<String>> headers, String responseBody) {
        onSuccess(new SuccessResponse(statusCode, headers, responseBody));
    }

    protected void handleFailureMessage(Throwable e, String responseBody) {
        onFailure(e, responseBody);
    }

    //Pre-processing of messages (executes in background threadpool thread)
    protected void sendStartMessage() {
        sendMessage(obtainMessage(START_MESSAGE, null));
    }

    protected void sendFinishMessage() {
        sendMessage(obtainMessage(FINISH_MESSAGE, null));
    }

    protected void sendFailureMessage(Throwable e, String responseBody) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{e, responseBody}));
    }

    protected void sendSuccessMessage(int statusCode, Map<String, List<String>> headers, String responseBody) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]{new Integer(statusCode), headers, responseBody}));
    }

}
