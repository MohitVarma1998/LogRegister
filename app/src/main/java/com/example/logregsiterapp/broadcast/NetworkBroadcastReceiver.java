package com.example.logregsiterapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.logregsiterapp.R;
import com.example.logregsiterapp.background.InnerAsyncJsonFetcher;
import com.example.logregsiterapp.background.OkhttpAsyncTask;
import com.example.logregsiterapp.utils.Actions;
import com.example.logregsiterapp.utils.WebService;

public class NetworkBroadcastReceiver extends BroadcastReceiver {

    public SnackbarCallback callback;

    public NetworkBroadcastReceiver(SnackbarCallback callback) {
        this.callback = callback;
    }

    public NetworkBroadcastReceiver() {

    }

    public static final String TAG = NetworkBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Actions.INTENT_FILTER_CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                if (callback != null) {
                    callback.setResponse(context.getResources().getString(R.string.internet_connected_msg));
                } else {
                    Log.d(TAG, "callback is not registered");
                }
                new InnerAsyncJsonFetcher().execute(WebService.URL);
                new OkhttpAsyncTask().execute(WebService.URL);
            } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                callback.setResponse(context.getResources().getString(R.string.internet_not_connected_msg));
            }
        }
    }

    public interface SnackbarCallback {
         void setResponse(String result);
    }

}
