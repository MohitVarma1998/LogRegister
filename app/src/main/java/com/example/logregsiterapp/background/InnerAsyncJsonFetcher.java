package com.example.logregsiterapp.background;

import android.os.AsyncTask;

import com.example.logregsiterapp.interfaces.AsyncCallBack;
import com.example.logregsiterapp.utils.Constants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InnerAsyncJsonFetcher extends AsyncTask<String, Void, String> implements AsyncCallBack {


    static StringBuilder stringBuilder = new StringBuilder();
    HttpURLConnection httpURLConnection = null;
    InputStream inputStream;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;

    public InnerAsyncJsonFetcher() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String singleitem = strings[0];
        try {
            httpURLConnection = (HttpURLConnection) new URL(singleitem).openConnection();
            httpURLConnection.setRequestMethod(Constants.HTTP_METHOD);
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return stringBuilder.toString();
    }

    @Override
    public String onResponse() {
        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}
