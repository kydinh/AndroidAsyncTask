package com.example.api;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.HttpEntity;

public class ApiGateway {

    private final Http http = new Http();

    public <T extends ApiResponse> void makeRequest(ApiRequest<T> apiRequest, final ApiResponseCallbacks<T> responseCallbacks) {
        new RemoteCallTask(responseCallbacks).execute(apiRequest);
    }

    protected void dispatch(ApiResponse apiResponse, ApiResponseCallbacks responseCallbacks) {
        if (apiResponse.isSuccess()) {
            try {
                responseCallbacks.onSuccess(apiResponse);
            } catch (Exception e) {
                Log.e(ApiGateway.class.getName(), "Error proccessing response", e);
                responseCallbacks.onFailure(apiResponse);
            }
        } else {
            responseCallbacks.onFailure(apiResponse);
        }
        responseCallbacks.onComplete();
    }

    private class RemoteCallTask extends AsyncTask<ApiRequest, Void, ApiResponse> {
        private final ApiResponseCallbacks responseCallbacks;

        public RemoteCallTask(ApiResponseCallbacks responseCallbacks) {
            this.responseCallbacks = responseCallbacks;
        }

        @Override
        protected void onPreExecute() {
            responseCallbacks.onStart();
        }

        @Override
        protected ApiResponse doInBackground(ApiRequest... apiRequests) {
            ApiRequest apiRequest = apiRequests[0];
            Log.d("Debug", apiRequest.getMethod());
            HttpEntity entity = null;
            try {
                Http.Response response;
                if (HttpPost.METHOD_NAME.equals(apiRequest.getMethod())) {
                    Log.d("Debug-url", apiRequest.getUrlString());
                    Log.d("Debug-header", apiRequest.getHeaders().toString());
                    Log.d("Debug-body", apiRequest.getPostBody().toString());
                    response = http.post(apiRequest.getUrlString(), apiRequest.getHeaders(), apiRequest.getPostBody(), apiRequest.getUsername(), apiRequest.getPassword());
                    
                } else if (HttpGet.METHOD_NAME.equals(apiRequest.getMethod())) {
                    response = http.get(apiRequest.getUrlString(), apiRequest.getHeaders(), apiRequest.getUsername(), apiRequest.getPassword());
                } else {
                    throw new RuntimeException("Unsupported Http Method!");
                }
                Log.d("Debug-exception", ""+response.getStatusCode()+" "+response.getResponseBody().toString());
                entity = response.getResponseBody();
                ApiResponse apiResponse = apiRequest.createResponse(response.getStatusCode());
                apiResponse.consumeResponse(entity);
                return apiResponse;
            } catch (Exception e) {
                Log.d("Debug-exception", e.getMessage());
                return apiRequest.createResponse(-1);
            } 
        }

        @Override
        protected void onPostExecute(ApiResponse apiResponse) {
            dispatch(apiResponse, responseCallbacks);
        }
    }
}
