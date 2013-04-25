/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.androidasyntask;

import android.util.Log;
import com.example.api.ApiRequest;
import com.example.api.JsonApiRespone;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author tinvo
 */
public class SigninRequest extends ApiRequest<JsonApiRespone>{

    String username;
    String password;
    
    public SigninRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    @Override
    public String getUrlString() {
        return "https://connect-test.4impact.net.au/Connect4impact/api/v1/login/loginUser";
    }

    @Override
    public JsonApiRespone createResponse(int statusCode) {
        Log.d("Debug-statuscode", ""+statusCode);
       return new JsonApiRespone(statusCode);
    }  

    @Override
    public Map<String, String> getParameters() {
        return super.getParameters(); 
    }

    @Override
    public String getMethod() {
        return HttpPost.METHOD_NAME;
    }

    @Override
    public List getPostBody() {
        List<NameValuePair>body = new ArrayList<NameValuePair>();
        
        body.add(new BasicNameValuePair("email", username));
        body.add(new BasicNameValuePair("password", password));
        body.add(new BasicNameValuePair("deviceUUID", "123456"));
        return body;
    }
    
}
