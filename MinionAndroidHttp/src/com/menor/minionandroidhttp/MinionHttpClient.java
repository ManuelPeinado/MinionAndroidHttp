package com.menor.minionandroidhttp;

import android.content.Context;
import com.squareup.okhttp.OkHttpClient;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * The MinionHttpClient can be used to make GET, POST, PUT and DELETE HTTP
 * requests in your Android applications. Requests can be made with additional
 * URL parameters by passing {@link RequestParams} instance, and {@link  RequestParams}
 * for POST methods.
 */
public class MinionHttpClient {

    // *******************************
    // ********** Constants **********
    // *******************************

    private final OkHttpClient mOkHttpClient;
    private final Map<Context, List<WeakReference<Future<?>>>> requestMap;
    private final RequestProperties mGlobalProperties;
    private static final String VERSION = "1.0.0";
    private static final String AGENT = String.format("Minion Android Http Client/%s", VERSION);


    // *******************************
    // ********** Variables **********
    // *******************************

    private ThreadPoolExecutor mThreadPool;
    private static int mConnectionTimeOut = Defaults.CONNECT_TIMEOUT;
    private static int mReadTimeOut = Defaults.READ_TIMEOUT;
    private static String mCharset = Defaults.CHARSET_UTF8;


    // *******************************
    // ******** Constructors *********
    // *******************************

    public MinionHttpClient() {
        mOkHttpClient = new OkHttpClient();
        mThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        requestMap = new WeakHashMap<Context, List<WeakReference<Future<?>>>>();
        mGlobalProperties = new RequestProperties();
        mGlobalProperties.setAgent(AGENT);
    }


    // *******************************
    // ******* Public Methods ********
    // *******************************

    /**
     * Get the underlying OkHttpClient instance. This is useful for setting
     * additional fine-grained settings.
     */
    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * Sets the Charset used for parsing body data,
     *
     * @param charset used for encoding response data
     */
    public void setEncondingCharset(String charset) {
        mCharset = charset;
    }

    /**
     * Sets the request waiting max time for connections.
     *
     * @param connectionTimeOut time of connecting wait, in milliseconds.
     */
    public void setConnectionTimeOut(int connectionTimeOut) {
        mConnectionTimeOut = connectionTimeOut;
    }

    /**
     * Sets the request waiting max time for connections.
     *
     * @param readTimeOut time of reading wait, in milliseconds.
     */
    public void setReadTimeOut(int readTimeOut) {
        mReadTimeOut = readTimeOut;
    }

    /**
     * Overrides the threadpool implementation used when queuing/pooling
     * requests. By default, Executors.newCachedThreadPool() is used.
     *
     * @param threadPool an instance of {@link ThreadPoolExecutor} to use for queuing/pooling requests.
     */
    public void setThreadPool(ThreadPoolExecutor threadPool) {
        mThreadPool = threadPool;
    }

    /**
     * Sets the User-Agent header to be sent with each request. By default,
     * Minion Http Client/VERSION is used.
     *
     * @param userAgent the string to use in the User-Agent header.
     */
    public void setUserAgent(String userAgent) {
        mGlobalProperties.setAgent(userAgent);
    }

    /**
     * Sets headers that will be added to all requests this client makes (before sending).
     * @param header the name of the header
     * @param value the contents of the header
     */
    public void addRequestProperty(String header, String value) {
        mGlobalProperties.put(header, value);
    }

    /**
     * Sets basic authentication for the request.
     * @param username
     * @param password
     */
    public void setBasicAuth(String username, String password){
        mGlobalProperties.setAuthorization(username + ":" + password);
    }

    /**
     * Cancels any pending (or potentially active) requests associated with the passed Context.
     * <p/>
     * <b>Note:</b> This will only affect requests which were created with a non-null
     * android Context. This method is intended to be used in the onDestroy
     * method of your android activities to destroy all requests which are no
     * longer required.
     *
     * @param context               the android Context instance associated to the request.
     * @param mayInterruptIfRunning specifies if active requests should be cancelled along with pending requests.
     */
    public void cancelRequests(Context context, boolean mayInterruptIfRunning) {
        List<WeakReference<Future<?>>> requestList = requestMap.get(context);
        if (requestList != null) {
            for (WeakReference<Future<?>> requestRef : requestList) {
                Future<?> request = requestRef.get();
                if (request != null) {
                    request.cancel(mayInterruptIfRunning);
                }
            }
        }
        requestMap.remove(context);
    }

    /**
     * Perform a HTTP GET request, without any parameters.
     *
     * @param url             the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void get(String url, DespicableListener responseHandler) {
        get(null, url, null, responseHandler);
    }

    /**
     * Perform a HTTP GET request, without any parameters.
     *
     * @param url             the URL to send the request to.
     * @param params          additional GET parameters to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void get(String url, RequestParams params, DespicableListener responseHandler) {
        get(null, url, params, responseHandler);
    }

    /**
     * Perform a HTTP GET request without any parameters and track the Android Context which initiated the request.
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void get(Context context, String url, DespicableListener responseHandler) {
        get(context, url, null, responseHandler);
    }

    /**
     * Perform a HTTP GET request and track the Android Context which initiated the request.
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param params          additional GET parameters to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void get(Context context, String url, RequestParams params, DespicableListener responseHandler) {
        sendRequest(getUrlWithQueryString(url, params), null, context, responseHandler, null, RequestMethod.GET);
    }

    /**
     * Perform a HTTP GET request and track the Android Context which initiated
     * the request with customized headers
     *
     * @param url the URL to send the request to.
     * @param headers set request properties only for this request
     * @param params additional GET parameters to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void get(Context context, String url, RequestProperties headers, RequestParams params, DespicableListener responseHandler) {
        sendRequest(getUrlWithQueryString(url, params), null, context, responseHandler, headers, RequestMethod.GET);
    }

    /**
     * Perform a HTTP POST request, without any parameters.
     * @param url the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(String url, DespicableListener responseHandler) {
        post(null, url, null, responseHandler);
    }

    /**
     * Perform a HTTP POST request with parameters.
     * @param url the URL to send the request to.
     * @param params additional POST parameters or files to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(String url, RequestParams params, DespicableListener responseHandler) {
        post(null, url, params, responseHandler);
    }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated the request.
     * @param context the Android Context which initiated the request.
     * @param url the URL to send the request to.
     * @param params additional POST parameters or files to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(Context context, String url, RequestParams params, DespicableListener responseHandler) {
        post(context, url, params, null, responseHandler);
    }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated the request.
     * @param context the Android Context which initiated the request.
     * @param url the URL to send the request to.
     * @param params additional POST parameters or files to send with the request.
     * @param contentType the content type of the payload you are sending, for example application/json if sending a json payload.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(Context context, String url, RequestParams params, String contentType, DespicableListener responseHandler) {
        post(context, url, params, new RequestProperties(), contentType, responseHandler);
    }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated
     * the request. Set headers only for this request
     *
     * @param context the Android Context which initiated the request.
     * @param url the URL to send the request to.
     * @param params additional POST parameters or files to send with the request.
     * @param requestProperties set headers only for this request
     * @param contentType the content type of the payload you are sending, for example application/json if sending a json payload.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(Context context, String url, RequestParams params, RequestProperties requestProperties, String contentType, DespicableListener responseHandler) {
        if (requestProperties != null && contentType != null) {
            requestProperties.setContentType(contentType);
        }
        sendRequest(url, params, context, responseHandler, requestProperties, RequestMethod.POST);
    }

    /**
     * Perform a HTTP PUT request, without any parameters.
     * @param url the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void put(String url, DespicableListener responseHandler) {
        put(null, url, null, responseHandler);
    }

    /**
     * Perform a HTTP PUT request with parameters.
     * @param url the URL to send the request to.
     * @param params additional PUT parameters or files to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void put(String url, RequestParams params, DespicableListener responseHandler) {
        put(null, url, params, responseHandler);
    }

    /**
     * Perform a HTTP PUT request and track the Android Context which initiated the request.
     * @param context the Android Context which initiated the request.
     * @param url the URL to send the request to.
     * @param params additional PUT parameters or files to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void put(Context context, String url, RequestParams params, DespicableListener responseHandler) {
        put(context, url, params, null, responseHandler);
    }

    /**
     * Perform a HTTP PUT request and track the Android Context which initiated the request.
     * And set one-time headers for the request
     * @param context the Android Context which initiated the request.
     * @param url the URL to send the request to.
     * @param params additional PUT parameters or files to send with the request.
     * @param contentType the content type of the payload you are sending, for example application/json if sending a json payload.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void put(Context context, String url, RequestParams params, String contentType, DespicableListener responseHandler) {
        put(context, url, params, new RequestProperties(), contentType, responseHandler);
    }

    /**
     * Perform a HTTP PUT request and track the Android Context which initiated the request.
     * And set one-time headers for the request
     * @param context the Android Context which initiated the request.
     * @param url the URL to send the request to.
     * @param params additional POST parameters or files to send with the request.
     * @param requestProperties set headers only for this request
     * @param contentType the content type of the payload you are sending, for example application/json if sending a json payload.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void put(Context context, String url, RequestParams params, RequestProperties requestProperties, String contentType, DespicableListener responseHandler) {
        if (requestProperties != null && contentType != null) {
            requestProperties.setContentType(contentType);
        }
        sendRequest(url, params, context, responseHandler, requestProperties,RequestMethod.PUT);
    }

    /**
     * Perform a HTTP DELETE request.
     * @param url the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void delete(String url, DespicableListener responseHandler) {
        delete(null, url, responseHandler);
    }

    /**
     * Perform a HTTP DELETE request.
     * @param context the Android Context which initiated the request.
     * @param url the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void delete(Context context, String url, DespicableListener responseHandler) {
        delete(context, url, null, responseHandler);
    }

    /**
     * Perform a HTTP DELETE request.
     * @param context the Android Context which initiated the request.
     * @param url the URL to send the request to.
     * @param requestProperties set headers only for this request
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void delete(Context context, String url, RequestProperties requestProperties, DespicableListener responseHandler) {
        sendRequest(url, null, context, null, requestProperties, RequestMethod.DELETE);
    }



    // *******************************
    // ****** Protected Methods ******
    // *******************************
    protected void sendRequest(String url, RequestParams params, Context context, DespicableListener responseHandler, RequestProperties requestProperties, RequestMethod method) {
        Future<?> request = null;
        if (responseHandler instanceof MinionJsonListener) {
            request = mThreadPool.submit(new MinionHttpJsonRequest(mOkHttpClient, url, params, mGlobalProperties, requestProperties, mCharset, method, mConnectionTimeOut, mReadTimeOut, (MinionJsonListener)responseHandler));
        } else if (responseHandler instanceof MinionStringListener) {
            request = mThreadPool.submit(new MinionHttpStringRequest(mOkHttpClient, url, params, mGlobalProperties, requestProperties, mCharset, method, mConnectionTimeOut, mReadTimeOut, (MinionStringListener)responseHandler));
        } else if (responseHandler instanceof  MinionObjectListener) {
            request = mThreadPool.submit(new MinionHttpObjectRequest(mOkHttpClient, url, params, mGlobalProperties, requestProperties, mCharset, method, mConnectionTimeOut, mReadTimeOut, (MinionObjectListener)responseHandler));
        } else {
            //User has passed DespicableListener
            //TODO Houston, we have a problem.
        }


        if (context != null) {
            // Add request to request map
            List<WeakReference<Future<?>>> requestList = requestMap.get(context);
            if (requestList == null) {
                requestList = new LinkedList<WeakReference<Future<?>>>();
                requestMap.put(context, requestList);
            }
            //TODO: the next "request" attribute could be null
            requestList.add(new WeakReference<Future<?>>(request));
            // TODO: Remove dead weakrefs from requestLists?
        }
    }

    // *******************************
    // ******* Private Methods *******
    // *******************************
    private static String getUrlWithQueryString(String url, RequestParams params) {
        if (params != null) {
            String paramString = params.getParamString();
            if (url.contains("?")) {
                url += "&" + paramString;
            } else {
                url += "?" + paramString;
            }
        }
        return url;
    }

}
