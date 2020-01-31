package com.example.logregsiterapp.activities;

import com.example.logregsiterapp.interfaces.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientActivity {

    private static Retrofit getRetrofitInstance() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static ApiService getApiSerive() {
        return getRetrofitInstance().create(ApiService.class);
    }

}
