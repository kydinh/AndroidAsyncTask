package com.example.api;

import android.util.Log;
import org.apache.http.HttpEntity;

public abstract class ApiResponse {
    protected int httpResponseCode;

    public ApiResponse(int httpCode) {
        this.httpResponseCode = httpCode;
    }

    abstract void consumeResponse(HttpEntity entity) throws Exception;

    public int getResponseCode() {
        return httpResponseCode;
    }

    public boolean isSuccess() {
        Log.d("Debug-responsecode", ""+httpResponseCode);
        return httpResponseCode == 200;
    }

    public boolean isUnauthorized() {
        return httpResponseCode == 401;
    }
}
