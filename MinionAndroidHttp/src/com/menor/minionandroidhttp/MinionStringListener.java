package com.menor.minionandroidhttp;

import java.util.List;
import java.util.Map;

public class MinionStringListener implements DespicableListener {

    /**
     * Fired when a request returns successfully
     *  @param content the body of the HTTP response from the server
     */
    public void onSuccess(String content) { }

    /**
     * Fired when a request returns successfully, override to handle in your own code
     * @param statusCode the status code of the response
     * @param content the body of the HTTP response from the server
     */
    public void onSuccess(int statusCode, String content) {
        onSuccess(content);
    }

    /**
     * Fired when a request returns successfully, override to handle in your own code
     * @param statusCode the status code of the response
     * @param headers the headers of the HTTP response
     * @param content the body of the HTTP response from the server
     */
    public void onSuccess(int statusCode, Map<String, List<String>> headers, String content) {
        onSuccess(statusCode, content);
    }

    @Override
    public void onPreExecute() { }

    @Override
    public void onPostExecute() { }

    @Override
    public void onFailure(Throwable error, String content) { }
}
