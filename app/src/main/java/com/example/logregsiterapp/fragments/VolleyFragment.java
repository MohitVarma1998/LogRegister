package com.example.logregsiterapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.logregsiterapp.R;
import com.example.logregsiterapp.adapters.VolleyAdapter;
import com.example.logregsiterapp.utils.GlobalDialog;
import com.example.logregsiterapp.utils.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class VolleyFragment extends Fragment {


    List<String> titlesList;

    RequestQueue requestQueue;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_volley__json, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.volley_recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        //Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024);
        titlesList = new LinkedList<>();
        //Network network = new BasicNetwork(new HurlStack());
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        if (checkConnection(getActivity())) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, WebService.URL, null, new Response.Listener<JSONArray>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(JSONArray response) {
                    if (response != null) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String title = jsonObject.getString(getActivity().getResources().getString(R.string.json_object_key));
                                titlesList.add(title);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        VolleyAdapter volleyAdapter = new VolleyAdapter(getActivity(), titlesList);
                        recyclerView.setAdapter(volleyAdapter);
                    } else {
                        GlobalDialog.createDialogwithtitle(getActivity(), null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onErrorResponse(error);
                }
            })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//
//                return headers;
//            }
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return super.getParams();
//            }
//        }
                    ;
            requestQueue.add(jsonArrayRequest);
        } else {
            GlobalDialog.createDialogwithtitle(getActivity(),getActivity().getResources().getString(R.string.internet_not_connected_msg));
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
