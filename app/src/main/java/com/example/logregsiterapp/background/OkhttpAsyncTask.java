package com.example.logregsiterapp.background;

import android.os.AsyncTask;

import com.example.logregsiterapp.interfaces.AsyncCallBack;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpAsyncTask extends AsyncTask<String, Void, String> implements AsyncCallBack {


    public static final String TAG = OkhttpAsyncTask.class.getSimpleName();

    static private OkHttpClient okHttpClient = new OkHttpClient();
    static private StringBuilder stringBuilder = new StringBuilder();

    @Override
    protected String doInBackground(String... strings) {

        String url = strings[0];
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            stringBuilder.append(response.body().string());
            return stringBuilder.toString();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String onResponse() {
        return stringBuilder.toString();
    }
}
