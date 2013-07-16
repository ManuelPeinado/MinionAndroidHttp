package com.menor.minionandroidhttp;

public interface DespicableListener {

    /**
     * Fired when the request is started
     */
    public void onPreExecute();

    /**
     * Fired in all cases when the request is finished, after both success and failure
     */
    public void onPostExecute();

    /**
     * Fired when a request fails to complete
     * @param error the underlying cause of the failure
     * @param content the response body, if any
     */
    public void onFailure(Throwable error, String content);

}
