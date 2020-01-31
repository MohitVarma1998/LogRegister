package com.example.logregsiterapp.fragments;

import android.content.BroadcastReceiver;
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
import com.example.logregsiterapp.adapters.AsyncAdapter;
import com.example.logregsiterapp.background.InnerAsyncJsonFetcher;
import com.example.logregsiterapp.broadcast.NetworkBroadcastReceiver;
import com.example.logregsiterapp.utils.GlobalDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AsyncJsonFragment extends Fragment {

    private List<String> titleList;
    String data;

    private RecyclerView recyclerView;

    public AsyncJsonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_async_task__json, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.async_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        titleList = new LinkedList<>();

        if (checkConnection(getActivity())) {
            data = new InnerAsyncJsonFetcher().onResponse();
            GlobalDialog.createDialogwithtitle(getActivity(), data);
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString(getStringfromResource(R.string.json_object_key));
                    titleList.add(title);
                }
                AsyncAdapter customRecylerView = new AsyncAdapter(getActivity(), titleList);
                recyclerView.setAdapter(customRecylerView);

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

