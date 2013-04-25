/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.api;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 *
 * @author tinvo
 */
public class JsonApiRespone extends ApiResponse{
    
    String jsonString;
    
    public JsonApiRespone(int httpCode){
        super(httpCode);
    }
    @Override
    void consumeResponse(HttpEntity entity) throws Exception {
        
        if(isSuccess()){
            jsonString = EntityUtils.toString(entity);
        }
    }
    
    public JSONObject getResponeJson() throws Exception{
        return new JSONObject(jsonString);
    }
    
}
