package com.example.api;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static com.example.util.Strings.isEmptyOrWhitespace;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

public class Http {

    private DefaultHttpClient httpClient;

    public Response get(String url, Map<String, String> headers, String username, String password)
            throws IOException, URISyntaxException {
        URI uri = new URI(url);
        return makeRequest(headers, username, password, new HttpGet(uri), uri.getHost());
    }

    public Response post(String url, Map<String, String> headers, List<NameValuePair> postBody, String username, String password)
            throws IOException, URISyntaxException {
        URI uri = new URI(url);
        HttpPost method = new HttpPost(uri);
        method.setEntity(new UrlEncodedFormEntity(postBody, "UTF-8"));
        return makeRequest(headers, username, password, method, uri.getHost());
    }

    private Response makeRequest(Map<String, String> headers, String username, String password, HttpRequestBase method, String host) {
        try {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                method.setHeader(entry.getKey(), entry.getValue());
            }
            DefaultHttpClient client = getHttpClient();
            addBasicAuthCredentials(client, host, username, password);
            return new Response(client.execute(method));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addBasicAuthCredentials(DefaultHttpClient client, String domainName, String username, String password) {
        if (!isEmptyOrWhitespace(username) || !isEmptyOrWhitespace(password)) {
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            client.getCredentialsProvider().setCredentials(new AuthScope(domainName, 443), credentials);
        }
    }

    private DefaultHttpClient getHttpClient() {
        if (httpClient == null) {
            HttpParams parameters = new BasicHttpParams();
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(parameters, schemeRegistry), parameters);
        }
        return httpClient;
    }

    public static class Response {
        private int statusCode;
        private HttpEntity entity;

        public Response(HttpResponse httpResponse) throws IOException {
            
            statusCode = httpResponse.getStatusLine().getStatusCode();
            entity = httpResponse.getEntity();
        }

        public int getStatusCode() {
            Log.d("Debug-statuscode-http", ""+statusCode);
            return statusCode;
        }

        public HttpEntity getResponseBody() {
            return entity;
        }
    }
}
