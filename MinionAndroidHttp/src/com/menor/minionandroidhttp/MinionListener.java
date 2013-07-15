package com.menor.minionandroidhttp;

public interface MinionListener {

    /**
     * Fired when the request is started
     */
    public void onStart();

    /**
     * Fired when the request is started
     * @param tag the associated id to the call
     */
    public void onStart(String tag);

    /**
     * Fired in all cases when the request is finished, after both success and failure
     */
    public void onFinish();

    /**
     * Fired in all cases when the request is finished, after both success and failure
     * @param tag the associated id to the call
     */
    public void onFinish(String tag);

    /**
     * Fired when a request returns successfully
     * @param response the body, statuscode and headers of the HTTP response from the server
     */
    public void onSuccess(SuccessResponse response);

    /**
     * Fired when a request returns successfully
     * @param tag the associated id to the call
     * @param response the body, statuscode and headers of the HTTP response from the server
     */
    public void onSuccess(String tag, SuccessResponse response);

    /**
     * Fired when a request fails to complete
     * @param error the underlying cause of the failure
     * @param content the response body, if any
     */
    public void onFailure(Throwable error, String content);

    /**
     * Fired when a request fails to complete
     * @param tag the associated id to the call
     * @param error the underlying cause of the failure
     * @param content the response body, if any
     */
    public void onFailure(String tag, Throwable error, String content);

}
