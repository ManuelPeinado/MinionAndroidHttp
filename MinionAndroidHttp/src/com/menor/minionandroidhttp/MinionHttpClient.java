package com.menor.minionandroidhttp;

import android.content.Context;
import com.squareup.okhttp.OkHttpClient;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HTTP;

import java.lang.ref.WeakReference;
import java.net.URL;
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
    private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
    private final OkHttpClient mOkHttpClient;
    private final Map<Context, List<WeakReference<Future<?>>>> requestMap;
    private final RequestProperties mGlobalProperties;



    // *******************************
    // ********** Variables **********
    // *******************************
    private ThreadPoolExecutor mThreadPool;
    private static int mSocketTimeOut = DEFAULT_SOCKET_TIMEOUT;
    private static String mCharset = "UTF-8";

    // *******************************
    // ******** Constructors *********
    // *******************************

    public MinionHttpClient() {
        mOkHttpClient = new OkHttpClient();
        mThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        requestMap = new WeakHashMap<Context, List<WeakReference<Future<?>>>>();
        mGlobalProperties = new RequestProperties();
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
     * @param socketTimeOut time of waiting, in milliseconds.
     */
    public void setSocketTimeout(int socketTimeOut) {
        mSocketTimeOut = socketTimeOut;
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
    public void get(String url, MinionHttpResponseHandler responseHandler) {
        get(null, url, null, responseHandler);
    }

    /**
     * Perform a HTTP GET request, without any parameters.
     *
     * @param url             the URL to send the request to.
     * @param params          additional GET parameters to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void get(String url, RequestParams params, MinionHttpResponseHandler responseHandler) {
        get(null, url, params, responseHandler);
    }

    /**
     * Perform a HTTP GET request without any parameters and track the Android Context which initiated the request.
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void get(Context context, String url, MinionHttpResponseHandler responseHandler) {
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
    public void get(Context context, String url, RequestParams params, MinionHttpResponseHandler responseHandler) {
        sendRequest(mOkHttpClient, getUrlWithQueryString(url, params), context, responseHandler, null, RequestMethod.GET);
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
    public void get(Context context, String url, RequestProperties headers, RequestParams params, MinionHttpResponseHandler responseHandler) {
        sendRequest(mOkHttpClient, getUrlWithQueryString(url, params), context, responseHandler, headers, RequestMethod.GET);
    }

    /**
     * Perform a HTTP POST request, without any parameters.
     * @param url the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(String url, MinionHttpResponseHandler responseHandler) {
        post(null, url, null, responseHandler);
    }

    /**
     * Perform a HTTP POST request with parameters.
     * @param url the URL to send the request to.
     * @param params additional POST parameters or files to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(String url, RequestParams params, MinionHttpResponseHandler responseHandler) {
        post(null, url, params, responseHandler);
    }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated the request.
     * @param context the Android Context which initiated the request.
     * @param url the URL to send the request to.
     * @param params additional POST parameters or files to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(Context context, String url, RequestParams params, MinionHttpResponseHandler responseHandler) {
        post(context, url, paramsToEntity(params), null, responseHandler);
    }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated the request.
     * @param context the Android Context which initiated the request.
     * @param url the URL to send the request to.
     * @param entity a raw {@link org.apache.http.HttpEntity} to send with the request, for example, use this to send string/json/xml payloads to a server by passing a {@link org.apache.http.entity.StringEntity}.
     * @param contentType the content type of the payload you are sending, for example application/json if sending a json payload.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(Context context, String url, HttpEntity entity, String contentType, MinionHttpResponseHandler responseHandler) {
        sendRequest(httpClient, httpContext, addEntityToRequestBase(new HttpPost(url), entity), contentType, responseHandler, context);
    }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated
     * the request. Set headers only for this request
     *
     * @param context the Android Context which initiated the request.
     * @param url the URL to send the request to.
     * @param headers set headers only for this request
     * @param entity a raw {@link HttpEntity} to send with the request, for example, use this to send string/json/xml payloads to a server by passing a {@link org.apache.http.entity.StringEntity}.
     * @param contentType the content type of the payload you are sending, for example application/json if sending a json payload.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(Context context, String url, Header[] headers, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPost(url), entity);
        if(headers != null) request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, contentType, responseHandler, context);
    }


    // *******************************
    // ****** Protected Methods ******
    // *******************************
    protected void sendRequest(OkHttpClient mOkHttpClient, String url, Context context, MinionHttpResponseHandler responseHandler, RequestProperties requestProperties, RequestMethod method) {
        Future<?> request = mThreadPool.submit(new MinionHttpRequest(mOkHttpClient, responseHandler, url, method, mUserAgent, mSocketTimeOut, mCharset));
        if (context != null) {
            // Add request to request map
            List<WeakReference<Future<?>>> requestList = requestMap.get(context);
            if (requestList == null) {
                requestList = new LinkedList<WeakReference<Future<?>>>();
                requestMap.put(context, requestList);
            }
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
