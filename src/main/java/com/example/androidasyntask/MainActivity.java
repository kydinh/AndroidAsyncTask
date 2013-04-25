package com.example.androidasyntask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;
import com.example.api.ApiGateway;

public class MainActivity extends Activity{

    ApiGateway apiGateway = new ApiGateway();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);
        new AuthenticationGateway(apiGateway, this).signin("tromano@4impact.com.au", "v15taSux", new SigninCallback());
    }
    
    private class SigninCallback extends Callbacks{
        ProgressDialog progressDialog;
        @Override
        public void onStart() {
            super.onStart(); 
            progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading...");
        }

        @Override
        public void onSuccess() {
            super.onSuccess(); 
            Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure() {
            super.onFailure(); 
        }

        @Override
        public void onComplete() {
            super.onComplete(); 
            progressDialog.dismiss();
        }
        
    }

}
