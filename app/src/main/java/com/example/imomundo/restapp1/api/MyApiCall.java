package com.example.imomundo.restapp1.api;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by imomundo on 4/16/2016.
 */

public class MyApiCall {

    public static final String BASE_URL = "https://Your Base URL Goes Here/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void put(String headerValue, Context context, String url, HttpEntity params, String dataType, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", "Bearer " + headerValue);
        client.put(context, getAbsoluteUrl(url), params, dataType, responseHandler);
    }

    public static void get(String headerValue, Context context, String url, HttpEntity params, String dataType, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", "Bearer " + headerValue);
        client.get(context, getAbsoluteUrl(url), params, dataType, responseHandler);
    }

    public static void post(String headerValue, Context context, String url, HttpEntity params, String dataType, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", "Bearer " + headerValue);
        client.post(context, getAbsoluteUrl(url), params, dataType, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        System.out.println("SERVER URL : " + BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }

}
