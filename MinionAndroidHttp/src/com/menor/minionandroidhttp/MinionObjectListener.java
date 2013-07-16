package com.menor.minionandroidhttp;

import java.util.List;
import java.util.Map;

public interface MinionObjectListener extends DespicableListener {

    public void onSuccess(Object response);

    public void onSuccess(int statusCode, Object content);

    public void onSuccess(int statusCode, Map<String, List<String>> headers, Object content);

    public void onFailure(Throwable error, Object errorResponse);

}
