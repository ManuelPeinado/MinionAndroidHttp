package com.menor.minionandroidhttp.sample;

import com.menor.minionandroidhttp.MinionHttpClient;
import com.menor.minionandroidhttp.MinionHttpResponseHandler;
import com.menor.minionandroidhttp.RequestParams;

public class DespicableHttpClient {

    private static final String BASE_URL = "http://www.eatapp.es/servicios_app/";

    private static MinionHttpClient client = new MinionHttpClient();

    static {
        client.setEncondingCharset("ISO-8859-1");
    }

    public static void get(String url, RequestParams params, MinionHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, MinionHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
