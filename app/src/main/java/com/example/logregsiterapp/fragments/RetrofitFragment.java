package com.example.logregsiterapp.fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logregsiterapp.R;
import com.example.logregsiterapp.activities.RetrofitClientActivity;
import com.example.logregsiterapp.adapters.RetrofitAdapter;
import com.example.logregsiterapp.interfaces.ApiService;
import com.example.logregsiterapp.model.Title;
import com.example.logregsiterapp.utils.GlobalDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RetrofitFragment extends Fragment {

    public static final String TAG = RetrofitFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private RetrofitAdapter retrofitAdapter;


    String value;

    public RetrofitFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retrofit__json, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.retrofit_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if (checkConnection(getActivity())) {
            getresult();
        } else {
            GlobalDialog.createDialogwithtitle(getActivity(), getStringfromResource(R.string.internet_not_connected_msg));
        }
        return view;
    }

    private String getStringfromResource(int internet_not_connected_msg) {
        return getActivity().getResources().getString(internet_not_connected_msg);
    }

    public void getresult() {


        ApiService apiService = RetrofitClientActivity.getApiSerive();

        Call<List<Title>> call = apiService.getTitle();

        call.enqueue(new Callback<List<Title>>() {
            @Override
            public void onResponse(Call<List<Title>> call, Response<List<Title>> response) {

                if (response.isSuccessful()) {
                    List<Title> titles = response.body();
                    retrofitAdapter = new RetrofitAdapter(titles, getActivity());
                    recyclerView.setAdapter(retrofitAdapter);
                } else {
                    Log.d(TAG, "Unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<List<Title>> call, Throwable t) {

            }
        });


    }

    public boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }

}
