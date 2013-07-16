package com.menor.minionandroidhttp;

import java.util.List;
import java.util.Map;

public class MinionObjectListener implements DespicableListener {

    public void onSuccess(Object content) { }

    public void onSuccess(int statusCode, Object content) {
        onSuccess(content);
    }

    public void onSuccess(int statusCode, Map<String, List<String>> headers, Object content) {
        onSuccess(statusCode, content);
    }

    public void onFailure(Throwable error, Object errorResponse) { }

    @Override
    public void onPreExecute() { }

    @Override
    public void onPostExecute() { }

    @Override
    public void onFailure(Throwable error, String content) { }

}
