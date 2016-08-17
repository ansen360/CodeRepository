package com.tomorrow_p.http;

import android.util.Log;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpRequest {
    private static final String TAG = "ansen";
    private static OkHttpClient mOkHttpClient;
    private static OkHttpRequest mOkHttpRequest;

    private OkHttpRequest() {
        mOkHttpClient = new OkHttpClient();
    }

    public static OkHttpRequest getInstance() {
        if (mOkHttpRequest == null) {
            synchronized (OkHttpRequest.class) {
                if (mOkHttpRequest == null) {
                    mOkHttpRequest = new OkHttpRequest();
                }
            }
        }
        return mOkHttpRequest;
    }

    /**
     * 同步请求
     */
    public String syncExecuteGET(String url) {
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = mOkHttpClient.newCall(request).execute();
            Log.d(TAG, "body: " + response.body().string());
            return response.body().toString();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public void asyncExecuteGET(String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * @addHeader Request request = new Request.Builder()
     * .url("https://api.github.com/repos/square/okhttp/issues")
     * .header("Accept", "qucii_api;application/json;v1.0")
     * .addHeader("Appkey", "P7Qdv7Xd018bFfp244t5")
     * .addHeader("Secretkey", "kzKt6pW1aaRSW1FjHYDjcosBx8OE95WI5TYDEiQ")
     * .build();
     * @addBody RequestBody formBody = new FormBody.Builder()
     * .add("loginName", "android")
     * .add("password", "android")
     * .add("deviceType", "1")
     * .build();
     */
    public String syncExecutePOST(String url, RequestBody body) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .header("Accept", "qucii_api;application/json;v1.0")
                    .addHeader("Appkey", "P7Qdv7Xd018bFfp244t5")
                    .addHeader("Secretkey", "kzKt6pW1aaRSW1FjHYDjcosBx8OE95WI5TYDEiQ")
                    .post(body)
                    .build();
            Response response = mOkHttpClient.newCall(request).execute();
            Log.d(TAG, "response body: " + response.body().string());
            return response.body().toString();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * @addHeader Request request = new Request.Builder()
     * .url("https://api.github.com/repos/square/okhttp/issues")
     * .header("Accept", "qucii_api;application/json;v1.0")
     * .addHeader("Appkey", "P7Qdv7Xd018bFfp244t5")
     * .addHeader("Secretkey", "kzKt6pW1aaRSW1FjHYDjcosBx8OE95WI5TYDEiQ")
     * .build();
     * @addBody RequestBody formBody = new FormBody.Builder()
     * .add("loginName", "android")
     * .add("password", "android")
     * .add("deviceType", "1")
     * .build();
     */
    public void asyncExecutePOST(String url, RequestBody body, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "qucii_api;application/json;v1.0")
                .addHeader("Appkey", "P7Qdv7Xd018bFfp244t5")
                .addHeader("Secretkey", "kzKt6pW1aaRSW1FjHYDjcosBx8OE95WI5TYDEiQ")
                .post(body)
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void setCache() {
    }

}
