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

public class MinionHttpResponseHandler {




    //Interface to MinionHttpRequest
    void sendResponseMessage(HttpURLConnection connection, String charset) {
        String responseBody;
        InputStream in;
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
            if (connection.getResponseCode() >= Defaults.REQUEST_CODE_OK) {
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

    //Pre-processing of messages (in original calling thread, typically the UI thread)
    protected void handleSuccessMessage(int statusCode, Map<String, List<String>> headers, String responseBody) {
        onSuccess(statusCode, headers, responseBody);
    }

    protected void handleFailureMessage(Throwable e, String responseBody) {
        onFailure(e, responseBody);
    }

    //Pre-processing of messages (executes in background threadpool thread)
    protected void sendFinishMessage() {
        sendMessage(obtainMessage(FINISH_MESSAGE, null));
    }

    protected void sendFailureMessage(Throwable e, String responseBody) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{e, responseBody}));
    }

    protected void sendSuccessMessage(int statusCode, Map<String, List<String>> headers, String responseBody) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]{new Integer(statusCode), headers, responseBody}));
    }

    // Methods which emulate android's Handler and Message methods
    protected void handleMessage(Message msg) {
        Object[] response;
        switch (msg.what) {
            case SUCCESS_MESSAGE:
                response = (Object[]) msg.obj;
                handleSuccessMessage((Integer) response[0], (Map<String, List<String>>) response[1], (String) response[2]);
                break;
            case FAILURE_MESSAGE:
                response = (Object[]) msg.obj;
                handleFailureMessage((Throwable) response[0], (String) response[1]);
                break;
            case START_MESSAGE:
                onPreExecute();
                break;
            case FINISH_MESSAGE:
                onPostExecute();
                break;
        }
    }







}
