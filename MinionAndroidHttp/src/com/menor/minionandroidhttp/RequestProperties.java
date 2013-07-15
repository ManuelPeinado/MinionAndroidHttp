package com.menor.minionandroidhttp;

import android.util.Base64;

import java.util.HashMap;

public class RequestProperties extends HashMap<String, String> {

    private static final long serialVersionUID = 4086166906613458962L;
    private static final String VERSION = "1.0.0";
    private static String mUserAgent = String.format("Minion Android Http Client/%s", VERSION);

    public static String PROPERTY_AGENT = "User-Agent";
    public static String PROPERTY_CONTENT_TYPE = "Content-Type";
    public static String PROPERTY_CONTENT_LENGTH = "Content-Type";
    public static String PROPERTY_CONTENT_LANGUAGE = "Content-Type";
    public static String PROPERTY_AUTHORIZATION= "Authorizattion";


    public RequestProperties() {
        //default properties
        setAgent(mUserAgent);
    }

    /**
     * Users can set an Authorization for their calls
     * @param userCredentials user:password string which is converted to Base64
     */
    public void setAuthorization(String userCredentials) {
        String encodedAuth = Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);
        put(PROPERTY_AUTHORIZATION, encodedAuth);
    }

    public void setAgent(String userAgent) {
        put(PROPERTY_AGENT, userAgent);
    }

    public void setContentType(String contentType) {
        put(PROPERTY_CONTENT_TYPE, contentType);
    }

    public void setContentLength(String length) {
        put(PROPERTY_CONTENT_LENGTH, length);
    }

    public void setContentLanguage(String length) {
        put(PROPERTY_CONTENT_LANGUAGE, length);
    }



}
