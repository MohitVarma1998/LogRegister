package com.example.logregsiterapp.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logregsiterapp.R;
import com.example.logregsiterapp.adapters.OkHttpAdapter;
import com.example.logregsiterapp.background.OkhttpAsyncTask;
import com.example.logregsiterapp.utils.GlobalDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class OkHttpFragment extends Fragment {

    public static final String TAG = OkHttpFragment.class.getSimpleName();

    private List<String> titleList;

    private RecyclerView recyclerView;

    public OkHttpFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ok_http__json, container, false);

        titleList = new LinkedList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.okhttp_recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);


        if (checkConnection(getActivity())) {
            String data = new OkhttpAsyncTask().onResponse();
            GlobalDialog.createDialogwithtitle(getActivity(), data);
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString(getStringfromResource(R.string.json_object_key));
                    titleList.add(title);
                }
                OkHttpAdapter okHttpAdapter = new OkHttpAdapter(getActivity(), titleList);
                recyclerView.setAdapter(okHttpAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            GlobalDialog.createDialogwithtitle(getActivity(), getStringfromResource(R.string.internet_not_connected_msg));
        }

        return view;
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

    public String getStringfromResource(int id) {
        return getActivity().getResources().getString(id);
    }


}
