package com.menor.minionandroidhttp;

import java.util.List;
import java.util.Map;

public class SuccessResponse {

    private Integer mStatusCode;
    private Map<String, List<String>> mHeaders;
    private String mContent;

    public SuccessResponse(Integer statusCode, Map<String, List<String>> headers, String content) {
        mStatusCode = statusCode;
        mHeaders = headers;
        mContent = content;
    }

    public Integer getStatusCode() {
        return mStatusCode;
    }

    public Map<String, List<String>> getHeaders() {
        return mHeaders;
    }

    public String getContent() {
        return mContent;
    }
}
