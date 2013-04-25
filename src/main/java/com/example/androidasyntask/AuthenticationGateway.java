/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.androidasyntask;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.api.ApiGateway;
import com.example.api.ApiResponse;
import com.example.api.ApiResponseCallbacks;
import com.example.api.JsonApiRespone;
import java.io.IOException;

/**
 *
 * @author tinvo
 */
public class AuthenticationGateway {
    static final String PROFILE_KEY = "profile";
    static final String SESSION_TOKEN = "sessionToken";
    public ApiGateway apiGateway;
    private SharedPreferences preferences;
    
    public AuthenticationGateway(ApiGateway apiGateway, Context context){
        this.apiGateway = apiGateway;
        this.preferences = context.getSharedPreferences(PROFILE_KEY, Context.MODE_PRIVATE);
    }
    
    public void signin(String username, String password, Callbacks callbacks){
        apiGateway.makeRequest(new SigninRequest(username, password), new AuthenticationApiResponseCallbacks(callbacks, preferences));
    }
    
    private static class AuthenticationApiResponseCallbacks implements ApiResponseCallbacks<JsonApiRespone> {
        private Callbacks callbacks;
        private SharedPreferences sharedPreferences;

        public AuthenticationApiResponseCallbacks(Callbacks callbacks, SharedPreferences sharedPreferences) {
            this.callbacks = callbacks;
            this.sharedPreferences = sharedPreferences;
        }
        
        public void onStart() {
            callbacks.onStart();
        }
        
        public void onSuccess(JsonApiRespone response) throws IOException {
            try {
                String sessionToken = response.getResponeJson().getString("sessionToken");
                sharedPreferences.edit().putString(SESSION_TOKEN, sessionToken).commit();
                callbacks.onSuccess();
            } catch (Exception e) {
            }
        }

        public void onFailure(ApiResponse response) {
            callbacks.onFailure();
        }

        public void onComplete() {
            callbacks.onComplete();
        }

        
    }
}
