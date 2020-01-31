package com.example.logregsiterapp.interfaces;

import com.example.logregsiterapp.model.Title;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @GET("/posts")
    Call<List<Title>> getTitle();

}
